package org.frank.shared.sysDictType.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "新增字典类型请求")
public class SysDictTypeAddReq {

    @ApiModelProperty(value = "Dict name", required = true)
    @NotBlank(message = "Dict")
    private String dictName;

    @ApiModelProperty(value = "字典类型", required = true)
    @NotBlank(message = "字典类型不能为空")
    private String dictType;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}