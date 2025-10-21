package org.frank.adapter.controller;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysPermissionService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.LoginUser;
import org.frank.domain.entity.SysUser;
import org.frank.shared.sysLogin.resp.SysUserResp;
import org.frank.shared.userPermission.resp.InfoResp;
import org.frank.shared.userPermission.resp.RouterResp;
import org.frank.shared.userPermission.resp.SysMenuResp;
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

    @GetMapping("getInfo")
    @ApiOperation("获取用户权限信息")
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

    @GetMapping("getRouters")
    @ApiOperation("获取路由信息")
    public AjaxResult<List<RouterResp>> getRouters() {
        SysUser user = getSysUser();
        user.setAdmin(isAdmin());

        List<SysMenuResp> selectMenuList = service.selectMenuList(user);
        return AjaxResult.success(service.buildMenus(selectMenuList));
    }

}
