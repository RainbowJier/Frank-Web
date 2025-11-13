package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysDictType;

public interface ISysDictTypeGateway extends IService<SysDictType> {

    /**
     * Check if dict type is unique excluding current id.
     */
    Boolean checkDictTypeUniqueExcludeCur(Long dictId, String dictType);

    /**
     * Check if dict name is unique excluding current id.
     */
    Boolean checkDictNameUniqueExcludeCur(Long dictId, String dictName);

    /**
     * Check if dict type is unique.
     */
    Boolean checkDictTypeUnique(String dictType);

    /**
     * Check if dict name is unique.
     */
    Boolean checkDictNameUnique(String dictName);
}
