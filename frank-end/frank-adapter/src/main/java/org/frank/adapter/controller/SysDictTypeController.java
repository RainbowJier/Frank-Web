package org.frank.adapter.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.frank.app.service.SysDictTypeService;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.core.page.TableDataInfo;
import org.frank.shared.sysDictType.req.PageQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("分页查询字典类型")
    public AjaxResult<TableDataInfo> list(PageQuery params) {
        return AjaxResult.success(service.selectDictTypeList(params));
    }





}
