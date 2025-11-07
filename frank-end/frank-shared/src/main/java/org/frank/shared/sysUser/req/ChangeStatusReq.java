package org.frank.shared.sysUser.req;


import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusReq {

    @ApiModelProperty("User Id")
    @NotNull(message = "User Id is empty.")
    private Long userId;

    @ApiModelProperty("Status")
    @NotNull(message = "Status is empty.")
    private Integer status;
}
