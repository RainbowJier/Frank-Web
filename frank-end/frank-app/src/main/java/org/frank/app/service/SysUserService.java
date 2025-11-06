package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysUser.req.SysUserReq;

public interface SysUserService {
    /**
     * 分页查询用户列表
     */
    PageResult selectUserList(SysUserReq req);







}
