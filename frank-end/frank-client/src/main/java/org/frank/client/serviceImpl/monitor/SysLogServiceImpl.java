package org.frank.client.serviceImpl.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.frank.app.service.monitor.SysLogService;
import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysLogLogin;
import org.frank.domain.gateway.ISysLogLoginGateway;
import org.frank.shared.sysLog.req.LoginPageQueryReq;
import org.frank.shared.sysLog.resp.SysLogLoginResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
}
