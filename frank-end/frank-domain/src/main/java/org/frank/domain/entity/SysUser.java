package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息表
 *
 * @TableName sys_user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user")
@Data
public class SysUser extends BaseEntity {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "nick_name")
    private String nickName;

    /**
     * user type(00 is system user.)
     */
    @TableField(value = "user_type")
    private String userType;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 1-male, 0-female, 2-unknown.
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * address of avatar.
     */
    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "password")
    private String password;

    /**
     * 1-able, 0-disable
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private boolean isAdmin;

    public boolean isAdmin() {
        return userId != null && 1L == userId;
    }
}