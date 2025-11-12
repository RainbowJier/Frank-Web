package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysDictTypeService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.domain.BaseController;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/remove/{dictId}")
    @ApiOperation("Delete dict type by dictId.")
    public AjaxResult<Void> remove(@PathVariable("dictId") Long dictId) {
        service.deleteDictTypeById(dictId);
        return AjaxResult.success();
    }




}
