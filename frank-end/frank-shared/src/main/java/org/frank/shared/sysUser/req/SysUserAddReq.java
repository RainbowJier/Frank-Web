package org.frank.shared.sysUser.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.domain.BaseReq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 新增用户请求对象
 *
 * @author Frank
 * @since 2025-11-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysUserAddReq extends BaseReq {
    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phoneNumber;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 2, max = 20, message = "用户账号长度必须在2-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String userName;

    @ApiModelProperty(value = "password", required = true)
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 5, max = 20, message = "用户密码长度必须在5-20个字符之间")
    private String password;

    @ApiModelProperty(value = "sex (1-male,0-female, 3-unknown）")
    private Integer sex;

    @ApiModelProperty(value = "user status, 1-able, 0-disable")
    private Integer status;

    @ApiModelProperty(value = "remark")
    private String remark;
}