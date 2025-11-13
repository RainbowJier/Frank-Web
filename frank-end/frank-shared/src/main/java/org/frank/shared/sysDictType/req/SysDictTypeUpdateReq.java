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
@ApiModel(description = "Update dictionary type request")
public class SysDictTypeUpdateReq {

    @ApiModelProperty(value = "Dictionary ID", required = true)
    @NotNull(message = "Dictionary ID cannot be empty")
    private Long dictId;

    @ApiModelProperty(value = "Dictionary name", required = true)
    @NotBlank(message = "Dictionary name cannot be empty")
    private String dictName;

    @ApiModelProperty(value = "Dictionary type", required = true)
    @NotBlank(message = "Dictionary type cannot be empty")
    private String dictType;

    @ApiModelProperty(value = "Status", required = true)
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @ApiModelProperty(value = "Remark")
    private String remark;
}