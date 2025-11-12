package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysDictDataService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.shared.sysDictData.resp.SysDictDataResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys-dict-data")
@ApiOperation("Dict Data")
public class SysDictDataController extends BaseController {
    @Resource
    private SysDictDataService service;

    @GetMapping(value = "/type/{dictType}")
    @ApiOperation("Get dict info by type.")
    public AjaxResult<List<SysDictDataResp>> dictType(@PathVariable("dictType") String dictType) {
        return AjaxResult.success(service.selectDictDataByType(dictType));
    }


}
