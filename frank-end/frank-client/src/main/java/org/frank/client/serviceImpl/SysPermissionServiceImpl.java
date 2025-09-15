package org.frank.client.serviceImpl;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.frank.app.service.SysPermissionService;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysRole;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysMenuGateway;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysRoleRelMenuGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private ISysRoleGateway sysRoleGateway;

    @Resource
    private ISysUserRelRoleGateway sysUserRelRoleGateway;

    @Resource
    private ISysRoleRelMenuGateway sysRoleRelMenuGateway;

    @Resource
    private ISysMenuGateway sysMenuGateway;


    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();

        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            List<SysRole> sysRoleList = getSysRoleList(user.getUserId());
            sysRoleList.forEach(sysRole -> roles.add(sysRole.getRoleKey()));
        }
        return roles;
    }

    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(getMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }


    private List<SysRole> getSysRoleList(Long userId) {
        List<Long> roleIds = sysUserRelRoleGateway.selectRoleIdsByUserId(userId);
        return sysRoleGateway.getBaseMapper().selectByIds(roleIds);
    }

    /**
     * get menu permission by user id.
     *
     * @param userId
     * @return menu permission list.
     */
    private List<String> getMenuPermsByUserId(Long userId) {
        List<Long> roleIds = sysUserRelRoleGateway.selectRoleIdsByUserId(userId);
        return getPermsByRoleIds(roleIds);
    }


    /**
     * get menu permission by role ids.
     *
     * @param roleIds
     * @return menu permission list.
     */
    private List<String> getPermsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleRelMenuGateway.selectMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        List<SysMenu> sysMenus = sysMenuGateway.selectByIds(menuIds);
        return sysMenus.stream()
                .map(SysMenu::getPerms)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }


}
