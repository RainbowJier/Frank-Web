package org.frank.common.core.domain;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.components.TokenService;
import org.frank.common.util.ServletUtil;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private ISysUserGateway sysUserGateway;

    @Resource
    private ISysUserRelRoleGateway sysUserRelRoleGateway;

    public LoginUser getLoginUser() {
        HttpServletRequest request = ServletUtil.getRequest();
        return tokenService.getLoginUser(request);
    }

    /**
     * Check is admin or not.
     *
     * @return true if is admin, false otherwise.
     */
    public boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return sysUserRelRoleGateway.isAdmin(loginUser.getUserId());
    }


    /**
     * Get the current user's ID.
     *
     * @return User id
     */
    public Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * get user detail by id.
     *
     * @return SysUser
     */
    public SysUser getSysUser() {
        return sysUserGateway.getById(getUserId());
    }

    /**
     * get role id list by user id.
     *
     * @return role id list.
     */
    public List<Long> getRoleIds() {
        return sysUserRelRoleGateway.selectRoleIdsByUserId(getUserId());
    }
}
