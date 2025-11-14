package org.frank.app.service.system;

import org.frank.common.core.page.PageResult;
import org.frank.shared.sysDictData.req.SysDictDataAddReq;
import org.frank.shared.sysDictData.req.SysDictDataListReq;
import org.frank.shared.sysDictData.req.SysDictDataUpdateReq;
import org.frank.shared.sysDictData.resp.SysDictDataResp;

import java.util.List;

public interface SysDictDataService {
    /**
     * Get dict data info by dict type.
     */
    List<SysDictDataResp> getDictDataByType(String dictType);

    /**
     * Query by pagination.
     */
    PageResult selectDictDataList(SysDictDataListReq params);

    /**
     * Get by dict code.
     */
    SysDictDataResp getDictDataByDictCode(Long dictCode);

    /**
     * Add dict data.
     */
    void addDictData(SysDictDataAddReq req);

    /**
     * Update dict data.
     */
    void updateDictData(SysDictDataUpdateReq req);

    void deleteDictDataBatch(List<Long> dictCodes);
}
