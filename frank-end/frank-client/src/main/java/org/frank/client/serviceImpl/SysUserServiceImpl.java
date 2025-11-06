package org.frank.client.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.frank.app.service.SysUserService;
import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.shared.sysUser.req.SysUserReq;
import org.frank.shared.sysUser.resp.SysUserResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private ISysUserGateway gateway;

    @Override
    public PageResult selectUserList(SysUserReq params) {
        IPage<SysUser> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(params.getUserId()), SysUser::getUserId, params.getUserId())
                .like(StringUtils.hasText(params.getUserName()), SysUser::getUserName, params.getUserName())
                .like(StringUtils.hasText(params.getPhoneNumber()), SysUser::getPhoneNumber, params.getPhoneNumber());

        if(params.getBeginTime() != null && params.getEndTime() != null){
            wrapper.between(SysUser::getCreateTime, params.getBeginTime(), params.getEndTime());
        }

        IPage<SysUser> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysUserResp.class);
    }
}
