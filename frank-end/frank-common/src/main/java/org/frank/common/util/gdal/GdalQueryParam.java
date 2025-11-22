package org.frank.common.util.gdal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GdalQueryParam {
    /**
     * GDB路径
     */
    private String gdbPath;

    /**
     * 图层数据列表
     */
    private List<String> layerNames;

    /**
     * 图层名称匹配方法
     * true: 开启 (如 layerNames=["Road"], 会匹配 "Road_L1", "Road_Net" 等)
     * false/null: 关闭 (默认，必须完全匹配)
     */
    private Boolean fuzzyMatchLayer;

    /**
     * 自定义查询字段
     */
    private List<String> returnFields;

    /**
     * 是否去重，如果去重的话
     */
    private Boolean distinct;

    /**
     * 自定义的 SQL 查询条件
     */
    private String whereClause;

    /**
     * 分页参数
     */
    private Integer pageNum;

    /**
     * 分页参数
     */
    private Integer pageSize;

    /**
     * 导出路径
     */
    private String targetGdbPath;
}