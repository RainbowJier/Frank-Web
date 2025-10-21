package org.frank.app.service;

import org.frank.shared.sysDictData.resp.SysDictDataResp;

import java.util.List;

public interface SysDictDataService {
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataResp> selectDictDataByType(String dictType);


}
