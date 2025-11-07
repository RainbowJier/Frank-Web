package org.frank.shared.sysUser.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserResp {
    @ApiModelProperty(value = "user ID")
    private String userId;

    @ApiModelProperty(value = "user name")
    private String userName;

    @ApiModelProperty(value = "nick name")
    private String nickName;

    @ApiModelProperty(value = "user type")
    private String userType;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "phone number")
    private String phoneNumber;

    @ApiModelProperty(value = "1-able, 0-disable.")
    private Integer status;

    @ApiModelProperty(value = "sex")
    private Integer sex;

    @ApiModelProperty(value = "address of avatar")
    private String avatar;

    @ApiModelProperty(value = "create time")
    private Date createTime;
}
