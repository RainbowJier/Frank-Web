package org.frank.shared.sysUser.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserUpdateReq {
    @ApiModelProperty(value = "user ID", required = true)
    @NotNull(message = "user ID can not be empty")
    private Long userId;

    @ApiModelProperty(value = "user nick name", required = true)
    @NotBlank(message = "")
    @Size(max = 30, message = "the length of nickname can not be more than 30 chars.")
    private String nickName;

    @ApiModelProperty(value = "phone number")
    private String phoneNumber;

    @ApiModelProperty(value = "mail")
    private String email;

    @ApiModelProperty(value = "sex (1-male,0-female, 3-unknown）")
    private Integer sex;

    @ApiModelProperty(value = "user status, 1-able, 0-disable")
    private Integer status;

    @ApiModelProperty(value = "remark")
    private String remark;
}