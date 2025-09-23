package org.frank.adapter.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.frank.app.service.SysLoginService;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.LoginUser;
import org.frank.shared.sysLogin.req.LoginReq;
import org.frank.shared.sysLogin.resp.LoginResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys-login")
@Api(value = "sysLogin", tags = "用户登录")
public class SysLoginController extends BaseController {

    @Resource
    private SysLoginService service;

    @Resource
    private TokenService tokenService;

    @PostMapping("/login")
    @ApiModelProperty("Common Login")
    public AjaxResult<?> login(@RequestBody @Valid LoginReq loginReq) {
        String token = service.login(loginReq);
        return AjaxResult.success(new LoginResp(token));
    }

    @PostMapping("/logout")
    @ApiModelProperty("Common Logout")
    public AjaxResult<?> logout() {
        LoginUser loginUser = getLoginUser();

        // delete login user from cache
        tokenService.delLoginUser(loginUser.getToken());

        // record operation logs.

        return AjaxResult.success();
    }




}
