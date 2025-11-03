package org.frank.app.service;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysDictType.req.PageQuery;

public interface SysDictTypeService {

    /**
     * 分页查询字典数据列表
     */
    PageResult selectDictTypeList(PageQuery params);
}
