package org.frank.client.serviceImpl.system;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogService;
import org.frank.app.service.system.SysLoginService;
import org.frank.common.components.TokenService;
import org.frank.common.constant.CacheConstants;
import org.frank.common.constant.Constants;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.core.redis.RedisCache;
import org.frank.common.exception.AuthenticationException;
import org.frank.common.util.IpUtil;
import org.frank.common.util.ServletUtil;
import org.frank.common.util.sign.BCryptUtils;
import org.frank.domain.entity.SysLogLogin;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.shared.sysLogin.req.LoginReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Resource
    private ISysUserGateway sysUserGateway;

    @Resource
    private RedisCache redisCache;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysLogService sysLogService;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Override
    public String login(LoginReq loginReq) {
        LoginUser loginUser = new LoginUser();

        try {
            SysUser sysUser = checkExistUser(loginReq.getUsername());
            validatePassword(sysUser, loginReq.getPassword());

            loginUser.setUserId(sysUser.getUserId())
                    .setUserName(sysUser.getUserName())
                    .setIpaddr(IpUtil.getIpAddr())
                    .setLoginLocation(IpUtil.getIpLocation(IpUtil.getIpAddr()))
                    .setBrowser(ServletUtil.getBrowser())
                    .setOs(ServletUtil.getOs());

            recordLoginLog(loginUser, Constants.SUCCESS);

            return tokenService.createToken(loginUser);
        } catch (Exception e) {
            recordLoginLog(loginUser, Constants.ERROR);
            throw new AuthenticationException("Failed to login.");
        }
    }

    /**
     * record login log asynchronously
     */
    private void recordLoginLog(LoginUser loginUser, String status) {
        SysLogLogin sysLogLogin = BeanUtil.copyProperties(loginUser, SysLogLogin.class);
        sysLogLogin.setStatus(status);
        sysLogService.saveLoginLogAsync(sysLogLogin);
    }


    private SysUser checkExistUser(String userName) {
        SysUser user = sysUserGateway.selectByUsername(userName);
        if (ObjectUtil.isNull(user)) {
            log.info("用户 {} 不存在", userName);
            throw new AuthenticationException("用户名或密码错误");
        }
        return user;
    }

    private void validatePassword(SysUser sysUser, String password) {

        Integer retryCount = redisCache.getCacheObject(getPasswordErrorCountKey(sysUser.getUserName()));
        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            log.warn("用户 {} 账户已被锁定", sysUser.getUserName());
            throw new AuthenticationException("账户已被锁定，请稍后再试");
        }

        boolean isMatch = matchesPassword(password, sysUser.getPassword());

        if (!isMatch) {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getPasswordErrorCountKey(sysUser.getUserName()), retryCount, lockTime, TimeUnit.MINUTES);
            log.warn("用户 {} 密码错误，已错误 {} 次", sysUser.getUserName(), retryCount);
            throw new AuthenticationException("用户名或密码错误");
        } else {
            clearLoginRecordCache(sysUser.getUserName());
        }
    }

    /**
     * get password error count key.
     */
    private String getPasswordErrorCountKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    /**
     * matches password
     */
    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return BCryptUtils.matchesPassword(rawPassword, encodedPassword);
    }

    /**
     * clear login record cache.
     */
    private void clearLoginRecordCache(String loginName) {
        String cacheKey = getPasswordErrorCountKey(loginName);
        if (redisCache.hasKey(cacheKey)) {
            redisCache.deleteObject(cacheKey);
        }
    }

}
