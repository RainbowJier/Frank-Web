package org.frank.shared.gdb.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GDB图层查询请求
 *
 * @author Frank
 * @since 2025-11-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GdbLayerExportReq {

    @ApiModelProperty(value = "GDB 文件路径")
    private String gdbPath;

    /**
     * 图层模糊匹配列表
     */
    private List<String> layerNameList;

    /**
     * 查询的字段
     */
    private String field;

    /**
     * 字段模糊匹配列表
     */
    private String value;

    /**
     * 导出路径
     */
    private String targetGdbPath;
}