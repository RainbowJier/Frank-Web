package org.frank.app.service.monitor;

import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysLogLogin;
import org.frank.shared.sysLog.req.LoginPageQueryReq;

public interface SysLogService {

    /**
     * Query login logs list by pagination.
     */
    PageResult selectLoginPage(LoginPageQueryReq req);

    /**
     * save login log asynchronously.
     */
    void saveLoginLogAsync(SysLogLogin sysLogLogin);
}
