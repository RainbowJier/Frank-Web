package org.frank.infrastructure.gateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysDictType;
import org.frank.domain.gateway.ISysDictTypeGateway;
import org.frank.infrastructure.mapper.SysDictTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class SysDictTypeGatewayImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
        implements ISysDictTypeGateway {

    @Resource
    private SysDictTypeMapper mapper;

    @Override
    public Boolean checkDictTypeUniqueExcludeCur(Long dictId, String dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SysDictType::getDictId, dictId);
        wrapper.eq(SysDictType::getDictType, dictType);
        return mapper.selectOne(wrapper) != null;
    }

    @Override
    public Boolean checkDictNameUniqueExcludeCur(Long dictId, String dictName) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SysDictType::getDictId, dictId);
        wrapper.eq(SysDictType::getDictName, dictName);
        return mapper.selectOne(wrapper) != null;
    }

    @Override
    public Boolean checkDictTypeUnique(String dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictType::getDictType, dictType);
        return mapper.selectOne(wrapper) != null;
    }

    @Override
    public Boolean checkDictNameUnique(String dictName) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictType::getDictName, dictName);
        return mapper.selectOne(wrapper) != null;
    }
}




