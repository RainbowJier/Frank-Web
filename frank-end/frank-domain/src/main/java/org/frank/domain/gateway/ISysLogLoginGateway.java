package org.frank.domain.gateway;


import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysLogLogin;

public interface ISysLogLoginGateway extends IService<SysLogLogin> {

    /**
     * clean all log login list.
     */
    void cleanAll();
}
