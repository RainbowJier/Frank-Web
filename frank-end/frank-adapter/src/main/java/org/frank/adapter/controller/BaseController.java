package org.frank.adapter.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.util.ServletUtil;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;


@Slf4j
public class BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private ISysUserGateway sysUserGateway;

    @Resource
    private ISysUserRelRoleGateway sysUserRelRoleGateway;

    @Resource
    private ISysRoleGateway sysRoleGateway;

    protected LoginUser getLoginUser() {
        HttpServletRequest request = ServletUtil.getRequest();
        return tokenService.getLoginUser(request);
    }

    /**
     * Check is admin or not.
     *
     * @return true if is admin, false otherwise.
     */
    protected boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return sysUserRelRoleGateway.isAdmin(loginUser.getUserId());
    }


    /**
     * Get the current user's ID.
     *
     * @return User id
     */
    protected Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * get user detail by id.
     *
     * @return SysUser
     */
    protected SysUser getSysUser() {
        return sysUserGateway.getById(getUserId());
    }
}
