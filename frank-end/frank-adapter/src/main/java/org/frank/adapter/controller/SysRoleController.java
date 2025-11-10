package org.frank.adapter.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysRoleService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysRole.req.SysRoleAddReq;
import org.frank.shared.sysRole.req.SysRoleQueryReq;
import org.frank.shared.sysRole.resp.SysRoleResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Resource
    private SysRoleService service;

    @GetMapping("/roleList/{userId}")
    @ApiOperation("Get role list by user id.")
    public AjaxResult<List<SysRoleResp>> roleList(@PathVariable("userId") Long userId) {
        if (userId == null) {
            return AjaxResult.failed("User id can not be empty.");
        }

        return AjaxResult.success(service.getRoleList(userId));
    }

    @GetMapping("/list")
    @ApiOperation("Get role list through pagination.")
    public AjaxResult<PageResult> list(SysRoleQueryReq req) {
        return AjaxResult.success(service.selectRoleList(req));
    }

    @GetMapping(value = "/{roleId}")
    @ApiOperation("Get role detail by ID.")
    public AjaxResult<SysRoleResp> getInfo(@PathVariable Long roleId) {
        if (roleId == null) {
            return AjaxResult.failed("Role id can not be empty.");
        }

        return AjaxResult.success(service.getById(roleId));
    }

    @PostMapping("add")
    @ApiOperation("Add new role.")
    public AjaxResult<Void> add(@Validated @RequestBody SysRoleAddReq req) {
        service.addRole(req);
        return AjaxResult.success();
    }



}
