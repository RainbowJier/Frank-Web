package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色和菜单关联表
 * @TableName sys_role_rel_menu
 */
@TableName(value ="sys_role_rel_menu")
@Data
public class SysRoleRelMenu {
    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @TableField(value = "menu_id")
    private Long menuId;
}