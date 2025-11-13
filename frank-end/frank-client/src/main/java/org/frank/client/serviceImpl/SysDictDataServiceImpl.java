package org.frank.client.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysDictDataService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.common.util.DictUtil;
import org.frank.domain.entity.SysDictData;
import org.frank.domain.gateway.ISysDictDataGateway;
import org.frank.shared.sysDictData.req.SysDictDataAddReq;
import org.frank.shared.sysDictData.req.SysDictDataListReq;
import org.frank.shared.sysDictData.req.SysDictDataUpdateReq;
import org.frank.shared.sysDictData.resp.SysDictDataResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private ISysDictDataGateway gateway;

    @Override
    public List<SysDictDataResp> getDictDataByType(String dictType) {
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

    @Override
    public PageResult selectDictDataList(SysDictDataListReq params) {
        IPage<SysDictData> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(params.getDictType()), SysDictData::getDictType, params.getDictType())
                .like(StringUtils.hasText(params.getDictLabel()), SysDictData::getDictLabel, params.getDictLabel())
                .eq(params.getStatus() != null, SysDictData::getStatus, params.getStatus())
                .orderByAsc(SysDictData::getDictSort);

        IPage<SysDictData> list = gateway.page(page, wrapper);

        return PageResult.ok(list, SysDictDataResp.class);
    }

    @Override
    public SysDictDataResp getDictDataByDictCode(Long dictCode) {
        SysDictData sysDictData = gateway.getById(dictCode);
        return BeanUtil.copyProperties(sysDictData, SysDictDataResp.class);
    }

    @Override
    public void addDictData(SysDictDataAddReq req) {
        if (BooleanUtil.isTrue(gateway.checkDictValueUnique(req.getDictType(), req.getDictValue()))) {
            throw new BusinessException("Dict value [" + req.getDictValue() + "] already exists in dict type [" + req.getDictType() + "]");
        }

        SysDictData sysDictData = BeanUtil.copyProperties(req, SysDictData.class);
        if (BooleanUtil.isFalse(gateway.save(sysDictData))) {
            throw new BusinessException("Fail to add a new dict data.");
        }

        // clear dict cache.
        DictUtil.clearDictCache();

        log.info("Dictionary data added successfully, dict type: {}, dict label: {}", req.getDictType(), req.getDictLabel());
    }

    @Override
    public void updateDictData(SysDictDataUpdateReq req) {
        // Check dict value uniqueness (exclude current record)
        if (BooleanUtil.isTrue(gateway.checkDictValueUniqueExcludeCur(req.getDictType(), req.getDictCode(), req.getDictValue()))) {
            throw new BusinessException("Dict value [" + req.getDictValue() + "] already exists in dict type [" + req.getDictType() + "]");
        }

        SysDictData sysDictData = BeanUtil.copyProperties(req, SysDictData.class);
        if (BooleanUtil.isFalse(gateway.updateById(sysDictData))) {
            throw new BusinessException("Failed to update dict data.");
        }

        // Clear dict type.
        DictUtil.removeDictCache(req.getDictType());
    }

    @Override
    public void deleteDictDataBatch(List<Long> dictCodes) {
        if (BooleanUtil.isFalse(gateway.removeByIds(dictCodes))) {
            throw new BusinessException("Failed to delete dict data.");
        }
    }
}
