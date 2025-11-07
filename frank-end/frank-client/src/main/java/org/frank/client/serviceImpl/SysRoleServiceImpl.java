package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.frank.app.service.SysRoleService;
import org.frank.domain.entity.SysRole;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.shared.sysRole.resp.SysRoleResp;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private ISysRoleGateway gateway;

    @Resource
    private ISysUserRelRoleGateway userRoleGateway;

    @Override
    public List<SysRoleResp> getRoleList(Long userId) {
        List<Long> roleIds = userRoleGateway.selectRoleIdsByUserId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<SysRole> sysRoles = gateway.selectListByIds(roleIds);
        if (CollUtil.isEmpty(sysRoles)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(sysRoles, SysRoleResp.class);

    }
}
