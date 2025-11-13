package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.req.SysDictTypeUpdateReq;
import org.frank.shared.sysDictType.resp.SysDictTypeOptionListResp;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;

import java.util.List;

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
     * Query dict type by dictId.
     */
    SysDictTypeResp selectDictTypeById(Long dictId);

    /**
     * Update dict type.
     */
    void updateDictType(SysDictTypeUpdateReq req);

    /**
     * Delete dict type by batch dictId.
     */
    void removeByIds(List<Long> dictIds);

    /**
     * Get dict type option list to select.
     */
    SysDictTypeOptionListResp selectDictTypeAll();
}
