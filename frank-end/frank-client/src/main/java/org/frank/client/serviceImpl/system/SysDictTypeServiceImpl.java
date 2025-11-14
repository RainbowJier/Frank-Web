package org.frank.client.serviceImpl.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.system.SysDictTypeService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.common.util.DictUtil;
import org.frank.domain.entity.SysDictData;
import org.frank.domain.entity.SysDictType;
import org.frank.domain.gateway.ISysDictDataGateway;
import org.frank.domain.gateway.ISysDictTypeGateway;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.req.SysDictTypeUpdateReq;
import org.frank.shared.sysDictType.resp.SysDictTypeOptionListResp;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private ISysDictTypeGateway gateway;

    @Resource
    private ISysDictDataGateway sysDictDataGateway;

    @Override
    public PageResult selectDictTypeList(PageQuery params) {
        IPage<SysDictType> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(params.getDictName()), SysDictType::getDictName, params.getDictName())
                .eq(params.getStatus() != null, SysDictType::getStatus, params.getStatus())
                .like(StringUtils.hasText(params.getDictType()), SysDictType::getDictType, params.getDictType())
                .orderByDesc(SysDictType::getCreateTime);

        if (StringUtils.hasText(params.getBeginTime()) && StringUtils.hasText(params.getEndTime())) {
            wrapper.ge(StringUtils.hasText(params.getBeginTime()), SysDictType::getCreateTime, params.getBeginTime())
                    .le(StringUtils.hasText(params.getEndTime()), SysDictType::getCreateTime, params.getEndTime());
        }

        List<SysDictType> list = gateway.list(page, wrapper);
        List<SysDictTypeResp> respList = BeanUtil.copyToList(list, SysDictTypeResp.class);

        return new PageResult(respList, list.size());
    }

    @Override
    public void insertDictType(SysDictTypeAddReq req) {
        // Check if dict type is unique.
        if (BooleanUtil.isTrue(gateway.checkDictTypeUnique(req.getDictType()))) {
            throw new BusinessException("Dict type [" + req.getDictType() + "] is already existed.");
        }

        // Check if dict name is unique.
        if (BooleanUtil.isTrue(gateway.checkDictNameUnique(req.getDictName()))) {
            throw new BusinessException("Dict name [" + req.getDictName() + "] is already existed.");
        }

        SysDictType dictType = new SysDictType();
        dictType.setDictName(req.getDictName())
                .setDictType(req.getDictType())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());

        if (BooleanUtil.isFalse(gateway.save(dictType))) {
            throw new BusinessException("Fail to add new dict type.");
        }
    }

    @Override
    public SysDictTypeResp selectDictTypeById(Long dictId) {
        SysDictType sysDictType = gateway.getById(dictId);
        return BeanUtil.copyProperties(sysDictType, SysDictTypeResp.class);
    }

    @Override
    public void updateDictType(SysDictTypeUpdateReq req) {
        // Check if dict type exists
        if (ObjectUtil.isEmpty(gateway.getById(req.getDictId()))) {
            throw new BusinessException("Dict type not found with ID: " + req.getDictId());
        }

        // Check if dict type is unique.
        if (BooleanUtil.isTrue(gateway.checkDictTypeUniqueExcludeCur(req.getDictId(), req.getDictType()))) {
            throw new BusinessException("Dict type [" + req.getDictType() + "] is already existed.");
        }

        // Check if dict name is unique.
        if (BooleanUtil.isTrue(gateway.checkDictNameUniqueExcludeCur(req.getDictId(), req.getDictName()))) {
            throw new BusinessException("Dict name [" + req.getDictName() + "] is already existed.");
        }

        // Update the dict type
        SysDictType updateDictType = new SysDictType();
        updateDictType.setDictId(req.getDictId());
        updateDictType.setDictName(req.getDictName());
        updateDictType.setDictType(req.getDictType());
        updateDictType.setStatus(req.getStatus());
        updateDictType.setRemark(req.getRemark());

        if (BooleanUtil.isFalse(gateway.updateById(updateDictType))) {
            throw new BusinessException("Update dict type failed");
        }

        log.info("Successfully updated dict type: {}", req.getDictId());
    }

    @Override
    public void removeByIds(List<Long> dictIds) {
        if (BooleanUtil.isFalse(gateway.removeByIds(dictIds))) {
            throw new BusinessException("Fail to remove dict data by batch.");
        }
    }

    @Override
    public SysDictTypeOptionListResp selectDictTypeAll() {
        return BeanUtil.copyProperties(gateway.list(), SysDictTypeOptionListResp.class);
    }

    @Override
    public void resetDictCache() {
        DictUtil.clearDictCache();
        loadingDictCache();
    }

    @Override
    public void loadingDictCache() {
        List<SysDictData> list = sysDictDataGateway.list();
        Map<String, List<SysDictData>> dictDataMap = list.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtil.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }
}