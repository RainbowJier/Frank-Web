package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysUserService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysUser.req.SysUserReq;
import org.frank.shared.sysUser.resp.SysUserResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息
 *
 * @author Frank
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService service;

    @GetMapping("/list")
    @ApiOperation("Get user list through pagination.")
    public AjaxResult<PageResult> list(SysUserReq req) {
        return AjaxResult.success(service.selectUserList(req));
    }

    @GetMapping(value = {"/", "/{userId}"})
    @ApiOperation("Get user detail by ID.")
    public AjaxResult<SysUserResp> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        return AjaxResult.success(service.getById(userId));
    }














}
