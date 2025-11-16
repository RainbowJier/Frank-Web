package org.frank.app.service.monitor;

import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysLogLogin;
import org.frank.shared.sysLogLogin.req.LoginPageQueryReq;

import java.util.List;

public interface SysLogLoginService {

    /**
     * Query login logs list by pagination.
     */
    PageResult selectLoginPage(LoginPageQueryReq req);

    /**
     * save login log asynchronously.
     */
    void saveLoginLogAsync(SysLogLogin sysLogLogin);

    /**
     * clean log login list.
     */
    void cleanLoginList();

    /**
     * delete log login list.
     */
    void deleteLoginLogByIds(List<Long> infoIds);
}
