package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysLogOper;

public interface ISysLogOperGateway extends IService<SysLogOper> {

    /**
     * clean all log operation list.
     */
    void cleanAll();
}