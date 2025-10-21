package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysMenuService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.domain.entity.SysMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("获取菜单列表")
    public AjaxResult<?> list(SysMenu menu) {
        List<SysMenu> menus = service.selectMenuList(menu, getUserId());
        return AjaxResult.success(menus);
    }


}