package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysDictData;
import org.frank.domain.gateway.ISysDictDataGateway;
import org.frank.infrastructure.mapper.SysDictDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class SysDictDataGatewayImpl
        extends ServiceImpl<SysDictDataMapper, SysDictData>
        implements ISysDictDataGateway {

    @Resource
    private SysDictDataMapper mapper;

    @Override
    public List<SysDictData> selectByCondition(SysDictData dictData) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(!ObjectUtils.isEmpty(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .eq(!ObjectUtils.isEmpty(dictData.getStatus()), SysDictData::getStatus, 1)
                .eq(!ObjectUtils.isEmpty(dictData.getDictValue()), SysDictData::getDictValue, dictData.getDictValue())
                .orderByAsc(SysDictData::getDictSort);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public Boolean checkDictValueUnique(String dictType, String dictValue) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue);

        return mapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Boolean checkDictValueUniqueExcludeCur(String dictType, Long dictCode, String dictValue) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue)
                .ne(SysDictData::getDictCode, dictCode);

        return mapper.selectCount(queryWrapper) > 0;
    }
}




