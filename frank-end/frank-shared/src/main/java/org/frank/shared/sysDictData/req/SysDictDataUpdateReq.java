package org.frank.shared.sysDictData.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictDataUpdateReq {
    @ApiModelProperty(value = "Dict Code")
    @NotNull(message = "Dict code cannot be null.")
    private Long dictCode;

    @ApiModelProperty(value = "Dict Type")
    @NotBlank(message = "Dict type cannot be null.")
    private String dictType;

    @ApiModelProperty(value = "Dict Label")
    @NotBlank(message = "Dict label cat not be null.")
    private String dictLabel;

    @ApiModelProperty(value = "Dict Value")
    @NotBlank(message = "Dict value cat not be null.")
    private String dictValue;

    @ApiModelProperty(value = "Sort")
    @NotNull(message = "Dict sort cat not be null.")
    private Integer dictSort;

    @ApiModelProperty(value = "css")
    private String cssClass;

    @ApiModelProperty(value = "Label css")
    private String listClass;

    @ApiModelProperty(value = "Status")
    @NotNull(message = "Dict status cat not be null.")
    private Integer status;

    @ApiModelProperty(value = "Remark")
    private String remark;
}