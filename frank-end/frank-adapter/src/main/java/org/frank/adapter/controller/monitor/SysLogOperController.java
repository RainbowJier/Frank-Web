package org.frank.adapter.controller.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogOperService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysLogOper.resp.SysLogOperResp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys-log-oper")
@Api(tags = "操作日志管理")
public class SysLogOperController {

    @Resource
    private SysLogOperService service;

    @PostMapping("/page")
    @ApiOperation("Query by pagination.")
    public AjaxResult<PageResult> pageQuery(@RequestBody @Valid SysLogOperPageQueryReq req) {
        return null;
    }

    @GetMapping("/{operId}")
    @ApiOperation("Get operation detail")
    public AjaxResult<SysLogOperResp> getInfoById(@PathVariable Long operId) {
        SysLogOperResp logOper = service.getInfoById(operId);
        return AjaxResult.success(logOper);
    }

    @PostMapping("/{operIds}")
    @ApiOperation("Delete by batch")
    public AjaxResult<Void> deleteByIds(@RequestBody List<Long> operIds) {
        service.deleteByIds(operIds);
        return AjaxResult.success();
    }

    @GetMapping("/clean")
    @ApiOperation("Clean all of operation logs")
    public AjaxResult<Void> cleanAll() {
        service.cleanAll();
        return AjaxResult.success();
    }
}