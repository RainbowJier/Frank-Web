package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.domain.entity.SysDictType;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;

public interface SysDictTypeService {

    /**
     * Query dict type list by pagination.
     */
    PageResult selectDictTypeList(PageQuery params);

    /**
     * Add new dict type.
     */
    void insertDictType(SysDictTypeAddReq req);


    /**
     * Get dict type by dict type.
     */
    SysDictType selectByDictType(String type);

    /**
     * Get dict type by dict name..
     */
    SysDictType selectByDictName(String name);

    /**
     * Query dict type by dictId.
     */
    SysDictTypeResp selectDictTypeById(Long dictId);

    /**
     * Delete dict type by dictId.
     */
    void deleteDictTypeById(Long dictId);
}
