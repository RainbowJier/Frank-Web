package org.frank.client.serviceImpl.monitor;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogService;
import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysLogLogin;
import org.frank.domain.gateway.ISysLogLoginGateway;
import org.frank.shared.sysLog.req.LoginPageQueryReq;
import org.frank.shared.sysLog.resp.SysLogLoginResp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private ISysLogLoginGateway sysLogLoginGateway;

    @Override
    public PageResult selectLoginPage(LoginPageQueryReq req) {
        IPage<SysLogLogin> page = new Page<>(req.getPageNum(), req.getPageSize());

        LambdaQueryWrapper<SysLogLogin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(req.getUserName()), SysLogLogin::getUserName, req.getUserName())
                .like(StringUtils.hasText(req.getIpaddr()), SysLogLogin::getIpaddr, req.getIpaddr())
                .eq(StringUtils.hasText(req.getStatus()), SysLogLogin::getStatus, req.getStatus());
        if (req.getBeginTime() != null && req.getEndTime() != null) {
            queryWrapper.ge(SysLogLogin::getLoginTime, req.getBeginTime())
                    .le(SysLogLogin::getLoginTime, req.getEndTime());
        }

        IPage<SysLogLogin> pageRes = sysLogLoginGateway.page(page, queryWrapper);
        return PageResult.ok(pageRes, SysLogLoginResp.class);
    }

    /**
     * 异步保存登录日志
     * 使用指定的登录日志线程池执行
     */
    @Override
    @Async("loginLogExecutor")
    public void saveLoginLogAsync(SysLogLogin sysLogLogin) {
        if (sysLogLogin == null) {
            log.warn("Login object is empty, skip logging");
            return;
        }

        if (BooleanUtil.isTrue(sysLogLoginGateway.save(sysLogLogin))) {
            log.debug("Login log saved successfully, username: {}, IP: {}, status: {}",
                    sysLogLogin.getUserName(), sysLogLogin.getIpaddr(), sysLogLogin.getStatus());
        } else {
            log.error("Failed to save login log, username: {}, IP: {}",
                    sysLogLogin.getUserName(), sysLogLogin.getIpaddr());
        }
    }
}
