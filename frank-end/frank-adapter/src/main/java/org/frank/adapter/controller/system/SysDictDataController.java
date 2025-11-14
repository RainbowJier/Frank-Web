package org.frank.adapter.controller.system;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.system.SysDictDataService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.shared.sysDictData.req.SysDictDataAddReq;
import org.frank.shared.sysDictData.req.SysDictDataListReq;
import org.frank.shared.sysDictData.req.SysDictDataUpdateReq;
import org.frank.shared.sysDictData.resp.SysDictDataResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys-dict-data")
@ApiOperation("Dict Data")
public class SysDictDataController extends BaseController {
    @Resource
    private SysDictDataService service;

    @PostMapping("/list")
    @ApiOperation("Query list.")
    public AjaxResult<PageResult> list(@RequestBody SysDictDataListReq params) {
        return AjaxResult.success(service.selectDictDataList(params));
    }

    @GetMapping("/{dictCode}")
    @ApiOperation("Get by dict code.")
    public AjaxResult<SysDictDataResp> getDictDataByDictCode(@PathVariable("dictCode") Long dictCode) {
        return AjaxResult.success(service.getDictDataByDictCode(dictCode));
    }

    @GetMapping(value = "/type/{dictType}")
    @ApiOperation("Get dict info by type.")
    public AjaxResult<List<SysDictDataResp>> selectDictDataByType(@PathVariable("dictType") String dictType) {
        return AjaxResult.success(service.getDictDataByType(dictType));
    }

    @PostMapping("/add")
    @ApiOperation("Add.")
    public AjaxResult<Void> add(@Validated @RequestBody SysDictDataAddReq req) {
        service.addDictData(req);
        return AjaxResult.success();
    }

    @PostMapping("update")
    @ApiOperation("Update.")
    public AjaxResult<Void> update(@Validated @RequestBody SysDictDataUpdateReq req) {
        service.updateDictData(req);
        return AjaxResult.success();
    }

    @PostMapping("delete")
    @ApiOperation("Delete.")
    public AjaxResult<Void> delete(@RequestBody List<Long> dictCodes) {
        service.deleteDictDataBatch(dictCodes);
        return AjaxResult.success();
    }

}
