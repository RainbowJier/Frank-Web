package org.frank.client.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysDictDataService;
import org.frank.common.util.DictUtil;
import org.frank.domain.entity.SysDictData;
import org.frank.domain.gateway.ISysDictDataGateway;
import org.frank.shared.sysDictData.resp.SysDictDataResp;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private ISysDictDataGateway gateway;

    @Override
    public List<SysDictDataResp> selectDictDataByType(String dictType) {
        List<SysDictData> list = DictUtil.getDictCache(dictType);
        if (CollUtil.isNotEmpty(list)) {
            return BeanUtil.copyToList(list, SysDictDataResp.class);
        }

        list = gateway.selectByCondition(new SysDictData(dictType));
        if (CollUtil.isNotEmpty(list)) {
            DictUtil.setDictCache(dictType, list);
            return BeanUtil.copyToList(list, SysDictDataResp.class);
        }

        return Collections.emptyList();
    }

}
