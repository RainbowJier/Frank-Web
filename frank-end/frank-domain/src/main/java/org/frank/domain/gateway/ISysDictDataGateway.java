package org.frank.domain.gateway;


import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysDictData;

import java.util.List;


public interface ISysDictDataGateway extends IService<SysDictData> {

    /**
     * 条件查询
     */
    List<SysDictData> selectByCondition(SysDictData dictData);
}
