package org.frank.shared.sysUser.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.page.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysUserQueryReq extends BasePage {
    @ApiModelProperty(value = "User ID")
    private String userId;

    @ApiModelProperty(value = "User account")
    private String userName;

    @ApiModelProperty(value = "User nickname")
    private String nickName;

    @ApiModelProperty(value = "User type (00 system user)")
    private String userType;

    @ApiModelProperty(value = "User email")
    private String email;

    @ApiModelProperty(value = "Phone number")
    private String phoneNumber;

    @ApiModelProperty(value = "User gender (1 male 0 female 2 unknown)")
    private Integer sex;

    @ApiModelProperty(value = "Avatar URL")
    private String avatar;

    @ApiModelProperty(value = "Account status (1 normal 0 disabled)")
    private Integer status;

    @ApiModelProperty(value = "Start time")
    private String beginTime;

    @ApiModelProperty(value = "End time")
    private String endTime;
}
