package org.frank.adapter.controller;


import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysDictTypeService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.req.SysDictTypeUpdateReq;
import org.frank.shared.sysDictType.resp.SysDictTypeOptionListResp;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author Frank
 */
@RestController
@RequestMapping("/sys-dict-type")
public class SysDictTypeController extends BaseController {
    @Resource
    private SysDictTypeService service;

    @GetMapping("/list")
    @ApiOperation("Query dict type list by pagination.")
    public AjaxResult<PageResult> list(PageQuery params) {
        return AjaxResult.success(service.selectDictTypeList(params));
    }

    @PostMapping("add")
    @ApiOperation("Add dict type.")
    public AjaxResult<Void> add(@Validated @RequestBody SysDictTypeAddReq req) {
        service.insertDictType(req);
        return AjaxResult.success();
    }

    @GetMapping("/{dictId}")
    @ApiOperation("Query dict type by dictId.")
    public AjaxResult<SysDictTypeResp> get(@PathVariable("dictId") Long dictId) {
        if (dictId == null) {
            throw new BusinessException("字典ID不能为空");
        }

        SysDictTypeResp result = service.selectDictTypeById(dictId);
        return AjaxResult.success(result);
    }

    @PostMapping("/update")
    @ApiOperation("Update dict type.")
    public AjaxResult<Void> update(@Validated @RequestBody SysDictTypeUpdateReq req) {
        service.updateDictType(req);
        return AjaxResult.success();
    }

    @PostMapping("/remove")
    @ApiOperation("Delete dict type by dictId.")
    public AjaxResult<Void> remove(@RequestBody List<Long> dictIds) {
        if (CollUtil.isEmpty(dictIds)) return AjaxResult.failed("Dict id list can not be empty.");

        service.removeByIds(dictIds);
        return AjaxResult.success();
    }

    @GetMapping("/option-list")
    @ApiOperation("Get dict type option list to select.")
    public AjaxResult<SysDictTypeOptionListResp> optionList() {
        return AjaxResult.success(service.selectDictTypeAll());
    }

    @GetMapping("/refreshCache")
    @ApiOperation("Refresh dict type cache.")
    public AjaxResult<Void> refreshCache() {
        service.resetDictCache();
        return AjaxResult.success();
    }

}
