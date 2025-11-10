package org.frank.shared.sysRole.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.domain.BaseReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 新增角色请求对象
 *
 * @author Frank
 * @since 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleAddReq extends BaseReq {
    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串", required = true)
    @NotBlank(message = "角色权限字符串不能为空")
    @Size(max = 100, message = "角色权限字符串长度不能超过100个字符")
    private String roleKey;

    @ApiModelProperty(value = "显示顺序")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    @ApiModelProperty(value = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    @ApiModelProperty(value = "角色状态（1正常 0停用）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}