package org.frank.shared.sysUser.req;


import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordReq {

    @ApiModelProperty("User Id")
    @NotNull(message = "User Id is empty.")
    private Long userId;

    @ApiModelProperty("New Password")
    @NotBlank(message = "New password is empty.")
    private String password;
}