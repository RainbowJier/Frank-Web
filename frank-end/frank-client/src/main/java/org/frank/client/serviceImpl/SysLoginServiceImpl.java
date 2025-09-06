package org.frank.client.serviceImpl;


import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysLoginService;
import org.frank.common.components.TokenService;
import org.frank.common.constant.CacheConstants;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.core.redis.RedisCache;
import org.frank.common.exception.AuthenticationException;
import org.frank.common.util.sign.BCryptUtils;
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

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Override
    public String login(LoginReq loginReq) {
        SysUser sysUser = checkExistUser(loginReq.getUsername());

        validatePassword(sysUser, loginReq.getPassword());

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysUser.getUserId())
                .setUserName(sysUser.getUserName());

        return tokenService.createToken(loginUser);
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
     * matches password - 使用BCryptUtils进行密码验证
     * 支持BCrypt加密方式，更安全
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
