package org.frank.shared.userPermission.resp;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.frank.shared.sysLogin.resp.SysUserResp;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "user info", description = "login response")
public class InfoResp {

    private SysUserResp user;

    private Set<String> roles;

    private Set<String> permissions;
}
