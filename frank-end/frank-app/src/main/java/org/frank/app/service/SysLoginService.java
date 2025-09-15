package org.frank.app.service;

import org.frank.shared.sysLogin.req.LoginReq;

public interface SysLoginService {


    /**
     * login.
     * @param loginReq
     * @return token
     */
    String login(LoginReq loginReq);
}
