package org.frank.adapter.controller;


import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.frank.app.service.SysPermissionService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.LoginUser;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysUser;
import org.frank.shared.UserPermission.resp.InfoResp;
import org.frank.shared.UserPermission.resp.RouterResp;
import org.frank.shared.UserPermission.resp.SysMenuResp;
import org.frank.shared.sysLogin.resp.SysUserResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys-user-permission")
public class SysPermissionController extends BaseController {

    @Resource
    private SysPermissionService service;

    /**
     * 获取用户权限信息
     */
    @GetMapping("getInfo")
    public AjaxResult<InfoResp> getInfo() {
        LoginUser loginUser = getLoginUser();
        SysUser user = getSysUser();
        user.setAdmin(isAdmin());

        SysUserResp sysUser = BeanUtil.copyProperties(user, SysUserResp.class);

        Set<String> roles = service.getRolePermission(user);

        Set<String> permission = service.getMenuPermission(user);
        if (loginUser.getMenuPermissions() == null || !loginUser.getMenuPermissions().equals(permission)) {
            loginUser.setMenuPermissions(permission);
        }

        return AjaxResult.success(new InfoResp(sysUser, roles, permission));
    }


    /**
     * 获取路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult<List<RouterResp>> getRouters() {
        SysUser user = getSysUser();
        user.setAdmin(isAdmin());

        List<SysMenuResp> selectMenuList = service.selectMenuList(user);

        return AjaxResult.success(service.buildMenus(selectMenuList));
    }

}
