package org.frank.app.service.monitor;

import org.frank.shared.sysLogOper.resp.SysLogOperResp;

import java.util.List;

public interface SysLogOperService {

    /**
     * delete by batch.
     */
    void deleteByIds(List<Long> operIds);

    /**
     * clean all.
     */
    void cleanAll();

    /**
     * get info by id.
     */
    SysLogOperResp getInfoById(Long operId);
}
