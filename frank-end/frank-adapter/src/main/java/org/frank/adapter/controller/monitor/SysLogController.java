package org.frank.adapter.controller.monitor;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysLog.req.LoginPageQueryReq;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sys-log")
@Api(tags = "System Login Logs")
@Slf4j
public class SysLogController {

    @Resource
    private SysLogService service;

    @PostMapping("/login/list")
    @ApiOperation("Query login logs list by pagination.")
    public AjaxResult<PageResult> selectLoginPage(@RequestBody @Valid LoginPageQueryReq req) {
        return AjaxResult.success(service.selectLoginPage(req));
    }

    @GetMapping("/login/clean")
    @ApiOperation("Clean log login list.")
    public AjaxResult<Void> cleanLoginList() {
        service.cleanLoginList();
        return AjaxResult.success();
    }

    @PostMapping("/login/delete")
    @ApiOperation("Delete login log by batch.")
    public AjaxResult<Void> deleteLoginLog(@RequestBody List<Long> infoIds) {
        if (CollUtil.isEmpty(infoIds)) return AjaxResult.failed("Please select at least one record to delete.");
        service.deleteLoginLogByIds(infoIds);
        return AjaxResult.success();
    }


}
