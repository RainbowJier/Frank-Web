package org.frank.app.service;

import org.frank.common.core.page.TableDataInfo;
import org.frank.shared.sysDictType.req.PageQuery;

public interface SysDictTypeService {

    /**
     * 分页查询字典数据列表
     */
    TableDataInfo selectDictTypeList(PageQuery params);
}
