package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单关联表
 * @TableName sys_role_rel_menu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_role_rel_menu")
public class SysRoleRelMenu extends BaseEntity{
    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "menu_id")
    private Long menuId;
}