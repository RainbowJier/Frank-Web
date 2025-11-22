package org.frank.adapter.controller.gdb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.core.domain.AjaxResult;
import org.frank.common.util.gdal.GdalQueryParam;
import org.frank.common.util.gdal.GdalUtil;
import org.frank.common.util.gdal.PageResult;
import org.frank.shared.gdb.req.GdbLayerExportReq;
import org.frank.shared.gdb.req.GdbLayerQueryReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GDB文件读取控制器
 * 提供GDB文件数据集、图层和图层数据读取的REST API
 *
 * @author Frank
 * @since 2025-11-17
 */
@Slf4j
@RestController
@RequestMapping("/gdb")
@RequiredArgsConstructor
@Api(tags = "GDB文件读取")
public class GdbController {
    @Resource
    private GdalUtil gdalUtil;

    @PostMapping("getGdbList")
    @ApiOperation(value = "Read GDB file data by pagination.")
    public AjaxResult<PageResult<Map<String, Object>>> getGdbList(@RequestBody GdbLayerQueryReq req) {
        String gdbPath = req.getGdbPath();
        List<String> layerNameList = req.getLayerNameList();
        String field = req.getField();
        String value = req.getValue();

        GdalQueryParam queryParam = GdalQueryParam.builder()
                .gdbPath(gdbPath)
                .layerNames(layerNameList)
                .fuzzyMatchLayer(true)
                .distinct(true)
                .returnFields(Arrays.asList(field))
                .whereClause(field + " like '%" + value + "%'")
                .pageNum(req.getPageNum())
                .pageSize(req.getPageSize())
                .build();

        return AjaxResult.success(gdalUtil.queryData(queryParam));
    }

    @PostMapping("exportGdb")
    @ApiOperation(value = "Export a filtered GDB file ")
    public AjaxResult<Void> exportGdb(@RequestBody GdbLayerExportReq req) {
        String gdbPath = req.getGdbPath();
        List<String> layerNameList = req.getLayerNameList();
        String field = req.getField();
        String value = req.getValue();
        String targetGdbPath = req.getTargetGdbPath();

        GdalQueryParam queryParam = GdalQueryParam.builder()
                .gdbPath(gdbPath)
                .layerNames(layerNameList)
                .fuzzyMatchLayer(true)
                .returnFields(null)   // 默认查询全部图层的字段
                .whereClause(field + " like '%" + value + "%'")
                .targetGdbPath(targetGdbPath)
                .build();

        gdalUtil.exportToGdb(queryParam);
        return AjaxResult.success();
    }


}