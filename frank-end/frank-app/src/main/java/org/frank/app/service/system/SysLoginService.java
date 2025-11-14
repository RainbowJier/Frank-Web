package org.frank.app.service.system;

import org.frank.shared.sysLogin.req.LoginReq;

public interface SysLoginService {
    /**
     * login.
     */
    String login(LoginReq loginReq);
}
