package org.frank.adapter.controller.monitor;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogLoginService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysLogLogin.req.LoginPageQueryReq;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sys-log-login")
@Api(tags = "System Login Logs")
@Slf4j
public class SysLogLoginController {

    @Resource
    private SysLogLoginService service;

    @PostMapping("list")
    @ApiOperation("Query login logs list by pagination.")
    public AjaxResult<PageResult> selectLoginPage(@RequestBody @Valid LoginPageQueryReq req) {
        return AjaxResult.success(service.selectLoginPage(req));
    }

    @GetMapping("clean")
    @ApiOperation("Clean log login list.")
    public AjaxResult<Void> cleanLoginList() {
        service.cleanLoginList();
        return AjaxResult.success();
    }

    @PostMapping("delete")
    @ApiOperation("Delete login log by batch.")
    public AjaxResult<Void> deleteLoginLog(@RequestBody List<Long> infoIds) {
        if (CollUtil.isEmpty(infoIds)) return AjaxResult.failed("Please select at least one record to delete.");
        service.deleteLoginLogByIds(infoIds);
        return AjaxResult.success();
    }


}
