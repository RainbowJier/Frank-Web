package org.frank.app.service.monitor;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysLog.req.LoginPageQueryReq;

public interface SysLogService {

    /**
     * Query login logs list by pagination.
     */
    PageResult selectLoginPage(LoginPageQueryReq req);
}
