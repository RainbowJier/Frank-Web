package org.frank.shared.sysUser.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frank.common.core.page.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
public class AllocatedUserQueryReq extends BasePage {

    @ApiModelProperty(value = "Role ID")
    @NotNull(message = "Role ID can not be empty.")
    private Long roleId;

    @ApiModelProperty(value = "User name")
    private String userName;

    @ApiModelProperty(value = "Phone number")
    private String phoneNumber;
}