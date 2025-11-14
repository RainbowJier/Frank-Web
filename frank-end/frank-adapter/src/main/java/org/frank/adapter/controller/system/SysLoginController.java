package org.frank.adapter.controller.system;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.frank.app.service.system.SysLoginService;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
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
    @ApiOperation("Common Login")
    public AjaxResult<?> login(@RequestBody @Valid LoginReq loginReq) {
        String token = service.login(loginReq);
        return AjaxResult.success(new LoginResp(token));
    }

    @PostMapping("/logout")
    @ApiOperation("Common Logout")
    public AjaxResult<?> logout() {
        LoginUser loginUser = getLoginUser();
        if(ObjectUtils.isEmpty(loginUser)){
            return AjaxResult.success("用户未登录");
        }

        // delete login user from cache
        tokenService.delLoginUser(loginUser.getToken());

        // record operation logs.

        return AjaxResult.success();
    }




}
