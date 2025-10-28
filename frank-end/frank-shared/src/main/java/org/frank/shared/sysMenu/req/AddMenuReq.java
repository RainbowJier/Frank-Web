package org.frank.shared.sysMenu.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.domain.BaseReq;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMenuReq extends BaseReq {
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序")
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    @ApiModelProperty(value = "路由地址")
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    @ApiModelProperty(value = "组件路径")
    @Size(max = 200, message = "组件路径不能超过255个字符")
    private String component;

    @ApiModelProperty(value = "路由参数")
    private String query;

    @ApiModelProperty(value = "路由名称")
    private String routeName;

    @ApiModelProperty(value = "是否为外链（1是 0否）")
    private Integer isFrame;

    @ApiModelProperty(value = "是否缓存（1缓存 0不缓存）")
    private Integer isCache;

    @ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）")
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    @ApiModelProperty(value = "菜单状态（1显示 0隐藏）")
    private Integer visible;

    @ApiModelProperty(value = "菜单状态（1显示 0隐藏）")
    private Integer status;

    @ApiModelProperty(value = "权限标识")
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;
}
