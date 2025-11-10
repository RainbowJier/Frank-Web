package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysRole.req.SysRoleAddReq;
import org.frank.shared.sysRole.req.SysRoleQueryReq;
import org.frank.shared.sysRole.resp.SysRoleResp;

import java.util.List;

public interface SysRoleService {
    /**
     * Get role list by user id.
     */
    List<SysRoleResp> getRoleList(Long userId);

    /**
     * Get role list through pagination.
     */
    PageResult selectRoleList(SysRoleQueryReq req);

    /**
     * Get role info by ID
     */
    SysRoleResp getById(Long roleId);

    /**
     * Add new role.
     */
    void addRole(SysRoleAddReq req);
}
