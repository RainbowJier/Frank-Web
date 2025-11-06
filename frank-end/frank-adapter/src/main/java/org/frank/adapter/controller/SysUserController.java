package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysUserService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysUser.req.SysUserReq;
import org.springframework.web.bind.annotation.GetMapping;
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
    @ApiOperation("获取用户列表")
    public AjaxResult<PageResult> list(SysUserReq req) {
        return AjaxResult.success(service.selectUserList(req));
    }



}
