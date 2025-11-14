package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统访问记录
 * @TableName sys_log_login
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_log_login")
@Data
public class SysLogLogin extends BaseEntity {
    @TableId(value = "id")
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "ipaddr")
    private String ipaddr;

    @TableField(value = "login_location")
    private String loginLocation;

    @TableField(value = "browser")
    private String browser;

    @TableField(value = "os")
    private String os;

    @TableField(value = "status")
    private String status;

    @TableField(value = "msg")
    private String msg;

    @TableField(value = "login_time")
    private Date loginTime;
}