package org.frank.shared.gdb.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.page.BasePage;

import java.util.List;

/**
 * GDB图层查询请求
 *
 * @author Frank
 * @since 2025-11-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GdbLayerQueryReq extends BasePage {

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
}