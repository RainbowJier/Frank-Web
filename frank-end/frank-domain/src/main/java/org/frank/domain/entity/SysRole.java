package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frank.common.core.domain.BaseEntity;

/**
 * 角色信息表
 *
 * @TableName sys_role
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role")
@Data
public class SysRole extends BaseEntity {
    /**
     * 角色ID
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @TableField(value = "role_key")
    private String roleKey;

    /**
     * 显示顺序
     */
    @TableField(value = "role_sort")
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @TableField(value = "data_scope")
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    @TableField(value = "menu_check_strictly")
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    @TableField(value = "dept_check_strictly")
    private Integer deptCheckStrictly;

    /**
     * 角色状态（1正常 -1停用）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     *
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
}