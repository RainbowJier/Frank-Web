package org.frank.shared.sysLogin.resp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "LoginResp", description = "login response")
public class LoginResp {
    @ApiModelProperty(value = "token")
    private String token;
}
