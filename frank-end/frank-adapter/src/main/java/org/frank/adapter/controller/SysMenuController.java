package org.frank.adapter.controller;


import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysMenuService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.shared.sysMenu.req.AddMenuReq;
import org.frank.shared.sysMenu.req.MenuListReq;
import org.frank.shared.sysMenu.req.UpdateMenuReq;
import org.frank.shared.sysMenu.resp.SysMenuResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author Frank
 */
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService service;

    @GetMapping("/list")
    @ApiOperation("Get menu list.")
    public AjaxResult<List<SysMenuResp>> list(MenuListReq req) {
        return AjaxResult.success(service.list(req));
    }

    @GetMapping(value = "/{menuId}")
    @ApiOperation("Get menu detail info by id.")
    public AjaxResult<SysMenuResp> getInfoById(@PathVariable("menuId") Long menuId) {
        return AjaxResult.success(service.getInfoById(menuId));
    }

    @PostMapping("add")
    @ApiOperation("Add menu")
    public AjaxResult<Void> add(@Validated @RequestBody AddMenuReq req) {
        service.add(req);
        return AjaxResult.success();
    }


    @PostMapping("update")
    @ApiOperation("Update menu")
    public AjaxResult<Void> update(@Validated @RequestBody UpdateMenuReq req) {
        service.update(req);
        return AjaxResult.success();
    }

    @GetMapping("/remove/{menuId}")
    @ApiOperation("Remove menu")
    public AjaxResult<Void> remove(@PathVariable("menuId") Long menuId) {
        service.remove(menuId);
        return AjaxResult.success();
    }


    @GetMapping("/tree")
    @ApiOperation("Get menu tree select.")
    public AjaxResult<List<Tree<Long>>> tree() {
        return AjaxResult.success(service.tree());
    }

    @GetMapping("/roleTree/{roleId}")
    @ApiOperation("Get menu tree containing the selection nodes of the role.")
    public AjaxResult<List<Tree<Long>>> roleTree(@PathVariable Long roleId) {
        return AjaxResult.success(service.roleTree(getRoleIds(), isAdmin(), roleId));
    }


}