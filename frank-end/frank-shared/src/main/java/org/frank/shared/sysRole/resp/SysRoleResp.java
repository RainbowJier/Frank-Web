package org.frank.shared.sysRole.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysRoleResp {
    @ApiModelProperty(value = "Role ID")
    private Long roleId;

    @ApiModelProperty(value = "Role name")
    private String roleName;

    @ApiModelProperty(value = "Role permission string")
    private String roleKey;

    @ApiModelProperty(value = "Display order")
    private Integer roleSort;

    @ApiModelProperty(value = "Whether menu tree selection items are associated")
    private Integer menuCheckStrictly;
}
