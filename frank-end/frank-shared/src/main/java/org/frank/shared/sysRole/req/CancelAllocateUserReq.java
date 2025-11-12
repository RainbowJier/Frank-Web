package org.frank.shared.sysRole.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CancelAllocateUserReq {
    @ApiModelProperty(value = "Role ID")
    @NotNull(message = "Role ID can not be empty.")
    private Long roleId;

    @ApiModelProperty(value = "User ID list")
    @NotNull(message = "User ID list can not be empty.")
    private List<Long> userIds;
}
