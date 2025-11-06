package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysUser.req.SysUserReq;
import org.frank.shared.sysUser.resp.SysUserResp;

public interface SysUserService {
    /**
     * Get user list through pagination.
     */
    PageResult selectUserList(SysUserReq req);

    /**
     * Get user info by ID
     */
    SysUserResp getById(Long userId);
}
