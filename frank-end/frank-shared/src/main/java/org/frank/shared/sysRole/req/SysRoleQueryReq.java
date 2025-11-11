package org.frank.shared.sysRole.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.page.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleQueryReq extends BasePage {
    @ApiModelProperty(value = "Role name")
    private String roleName;

    @ApiModelProperty(value = "Role permission string")
    private String roleKey;

    @ApiModelProperty(value = "Role status (1 normal 0 disabled)")
    private Integer status;

    @ApiModelProperty(value = "Begin time")
    private String beginTime;

    @ApiModelProperty(value = "End time")
    private String endTime;
}