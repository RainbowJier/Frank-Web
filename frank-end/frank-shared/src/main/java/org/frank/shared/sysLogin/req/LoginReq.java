package org.frank.shared.sysLogin.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录对象
 *
 * @author Frank
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq implements Serializable {

    @ApiModelProperty(value = "user name")
    @NotBlank(message = "user name can not be none.")
    @Size(min = 2, max = 20, message = "username length must be between 2 and 20")
    private String username;

    @ApiModelProperty(value = "password")
    @NotBlank(message = "password can not be none.")
    @Size(min = 5, max = 20, message = "password length must be between 2 and 20")
    private String password;
}
