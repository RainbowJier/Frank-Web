package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysDictTypeService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.domain.entity.SysDictType;
import org.frank.domain.gateway.ISysDictTypeGateway;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.req.SysDictTypeAddReq;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private ISysDictTypeGateway gateway;

    @Override
    public PageResult selectDictTypeList(PageQuery params) {
        IPage<SysDictType> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(params.getDictName()), SysDictType::getDictName, params.getDictName())
                .eq(params.getStatus() != null, SysDictType::getStatus, params.getStatus())
                .like(StringUtils.hasText(params.getDictType()), SysDictType::getDictType, params.getDictType())
                .orderByDesc(SysDictType::getCreateTime);
        // 创建时间范围查询
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
        SysDictType existDictType = selectByDictType(req.getDictType());
        if (existDictType != null) {
            throw new BusinessException("Dict type [" + req.getDictType() + "] is already existed.");
        }

        // Check if dict name is unique.
        SysDictType sysDictType = selectByDictName(req.getDictName());
        if (sysDictType != null) {
            throw new BusinessException("Dict name [" + req.getDictName() + "] is already existed.");
        }

        SysDictType dictType = new SysDictType();
        dictType.setDictName(req.getDictName())
                .setDictType(req.getDictType())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());

        if (BooleanUtil.isFalse(gateway.save(dictType))) {
            throw new BusinessException("新增字典类型失败");
        }
    }

    @Override
    public SysDictType selectByDictType(String type) {
        return gateway.getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, type));
    }

    @Override
    public SysDictType selectByDictName(String name) {
        return gateway.getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictName, name));
    }

    @Override
    public SysDictTypeResp selectDictTypeById(Long dictId) {
        SysDictType sysDictType = gateway.getById(dictId);
        return BeanUtil.copyProperties(sysDictType, SysDictTypeResp.class);
    }

    @Override
    public void deleteDictTypeById(Long dictId) {
        if (dictId == null) {
            throw new BusinessException("字典ID不能为空");
        }

        SysDictType sysDictType = gateway.getById(dictId);
        if (sysDictType == null) {
            throw new BusinessException("字典类型不存在");
        }

        if (BooleanUtil.isFalse(gateway.removeById(dictId))) {
            throw new BusinessException("删除字典类型失败");
        }

        log.info("删除字典类型成功，dictId: {}, dictType: {}", dictId, sysDictType.getDictType());
    }
}