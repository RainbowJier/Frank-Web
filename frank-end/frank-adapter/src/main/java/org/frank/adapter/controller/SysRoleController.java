package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysRoleService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.shared.sysRole.resp.SysRoleResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Resource
    private SysRoleService service;

    @GetMapping("/roleList/{userId}")
    @ApiOperation("Get role list by user id.")
    public AjaxResult<List<SysRoleResp>> roleList(@PathVariable("userId") Long userId) {
        if(userId == null){
            return AjaxResult.failed("User id can not be empty.");
        }

        return AjaxResult.success(service.getRoleList(userId));
    }


}
