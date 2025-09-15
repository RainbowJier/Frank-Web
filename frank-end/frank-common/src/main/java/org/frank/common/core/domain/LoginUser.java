package org.frank.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class LoginUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    private String token;

    private Long loginTime;

    private Long expireTime;

    private String ipaddr;

    private String loginLocation;

    private String browser;

    private String os;

    /**
     * 按钮权限列表
     */
    private Set<String> menuPermissions;
}