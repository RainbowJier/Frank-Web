package org.frank.adapter.controller;


import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.frank.app.service.SysUserService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.shared.sysUser.req.*;
import org.frank.shared.sysUser.resp.SysUserResp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list")
    @ApiOperation("Get user list through pagination.")
    public AjaxResult<PageResult> list(@RequestBody SysUserQueryReq req) {
        return AjaxResult.success(service.selectUserList(req));
    }

    @GetMapping(value = {"/", "/{userId}"})
    @ApiOperation("Get user detail by ID.")
    public AjaxResult<SysUserResp> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        return AjaxResult.success(service.getById(userId));
    }

    @PostMapping("add")
    @ApiOperation("Add new user.")
    public AjaxResult<Void> add(@Valid @RequestBody SysUserAddReq req) {
        service.addUser(req);
        return AjaxResult.success();
    }


    @PostMapping("update")
    @ApiOperation("Update existing user.")
    public AjaxResult<Void> edit(@Valid @RequestBody SysUserUpdateReq req) {
        service.updateUser(req);
        return AjaxResult.success();
    }


    @PostMapping("/delete")
    @ApiOperation("Delete user by ID.")
    public AjaxResult<Void> delete(@RequestBody List<Long> userIds) {
        if (userIds.contains(getUserId())) {
            throw new BusinessException("You can not delete the current login user.");
        }

        if (CollUtil.isEmpty(userIds)) {
            return AjaxResult.failed("The user id list is empty.");
        }

        service.deleteByIds(userIds);
        return AjaxResult.success();
    }

    @PostMapping("/reset-password")
    @ApiOperation("Reset user password.")
    public AjaxResult<Void> resetPwd(@Valid @RequestBody ResetPasswordReq req) {
        service.resetPassword(req);
        return AjaxResult.success();
    }

    @PostMapping("/change-status")
    @ApiOperation("Change user status.")
    public AjaxResult<Void> changeStatus(@Valid @RequestBody ChangeStatusReq req) {
        if (req.getUserId().equals(getUserId())) {
            return AjaxResult.failed("You can not change status for the current login user.");
        }

        service.changeStatus(req);
        return AjaxResult.success();
    }


}
