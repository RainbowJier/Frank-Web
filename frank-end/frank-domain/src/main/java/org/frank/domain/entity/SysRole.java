package org.frank.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息表
 *
 * @TableName sys_role
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role")
@Data
public class SysRole extends BaseEntity {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @TableField(value = "role_name")
    private String roleName;

    /**
     * permission key(sys:role:list, sys:user:list)
     */
    @TableField(value = "role_key")
    private String roleKey;

    /**
     * 显示顺序
     */
    @TableField(value = "role_sort")
    private Integer roleSort;

    /**
     * 菜单树选择项是否关联显示
     */
    @TableField(value = "menu_check_strictly")
    private Integer menuCheckStrictly;

    /**
     * role status, 1-normal, 0-disable.
     */
    @TableField(value = "status")
    private Integer status;

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }
}