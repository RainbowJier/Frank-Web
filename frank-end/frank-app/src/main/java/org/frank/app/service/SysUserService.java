package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysUser.req.SysUserReq;
import org.frank.shared.sysUser.req.SysUserAddReq;
import org.frank.shared.sysUser.req.SysUserUpdateReq;
import org.frank.shared.sysUser.resp.SysUserResp;

import java.util.List;

public interface SysUserService {
    /**
     * Get user list through pagination.
     */
    PageResult selectUserList(SysUserReq req);

    /**
     * Get user info by ID
     */
    SysUserResp getById(Long userId);

    /**
     * Add new user.
     */
    void addUser(SysUserAddReq req);

    /**
     * Update existing user.
     */
    void updateUser(SysUserUpdateReq req);

    /**
     * Delete users by IDs.
     */
    void deleteByIds(List<Long> userIds);
}
