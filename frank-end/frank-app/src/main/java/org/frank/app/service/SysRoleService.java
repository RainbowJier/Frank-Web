package org.frank.app.service;

import org.frank.shared.sysRole.resp.SysRoleResp;

import java.util.List;

public interface SysRoleService {
    /**
     * Get role list by user id.
     */
    List<SysRoleResp> getRoleList(Long userId);




}
