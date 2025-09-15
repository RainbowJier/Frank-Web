package org.frank.app.service;

import org.frank.domain.entity.SysUser;

import java.util.Set;

public interface SysPermissionService {

    /**
     * get role permission
     * @param user
     * @return
     */
    Set<String> getRolePermission(SysUser user);


    /**
     * get menu permission
     * @param user
     * @return
     */
    Set<String> getMenuPermission(SysUser user);
}
