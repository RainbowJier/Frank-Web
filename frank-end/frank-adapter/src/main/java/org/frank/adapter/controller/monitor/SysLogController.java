package org.frank.adapter.controller.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysLog.req.LoginPageQueryReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sys-log")
@Api(tags = "System Login Logs")
@Slf4j
public class SysLogController {

    @Resource
    private SysLogService service;

    @PostMapping("/login/list")
    @ApiOperation("Query login logs list by pagination.")
    public AjaxResult<PageResult> list(@RequestBody @Valid LoginPageQueryReq req) {
        return AjaxResult.success(service.selectLoginPage(req));
    }
}
