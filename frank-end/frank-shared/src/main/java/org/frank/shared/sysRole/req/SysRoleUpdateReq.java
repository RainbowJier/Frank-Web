package org.frank.shared.sysRole.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleUpdateReq {
    @ApiModelProperty(value = "Role ID", required = true)
    @NotNull(message = "Role ID cannot be empty")
    private Long roleId;

    @ApiModelProperty(value = "Role name", required = true)
    @NotBlank(message = "Role name cannot be empty")
    @Size(max = 30, message = "Role name length cannot exceed 30 characters")
    private String roleName;

    @ApiModelProperty(value = "Role permission string", required = true)
    @NotBlank(message = "Role permission string cannot be empty")
    @Size(max = 100, message = "Role permission string length cannot exceed 100 characters")
    private String roleKey;

    @ApiModelProperty(value = "Display order")
    @NotNull(message = "Display order cannot be empty")
    private Integer roleSort;

    @ApiModelProperty(value = "Whether menu tree selections are associated")
    private Integer menuCheckStrictly;

    @ApiModelProperty(value = "Role status (1 normal 0 disabled)")
    private Integer status;

    @ApiModelProperty(value = "Remark")
    private String remark;

    @ApiModelProperty(value = "Selected menu id list.")
    private List<Long> menuIds;
}