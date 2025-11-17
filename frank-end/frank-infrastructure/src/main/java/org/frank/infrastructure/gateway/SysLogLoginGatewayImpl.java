package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysLogLogin;
import org.frank.domain.gateway.ISysLogLoginGateway;
import org.frank.infrastructure.mapper.SysLogLoginMapper;
import org.springframework.stereotype.Service;


@Service
public class SysLogLoginGatewayImpl
        extends ServiceImpl<SysLogLoginMapper, SysLogLogin>
        implements ISysLogLoginGateway {

    @Resource
    private SysLogLoginMapper mapper;

    @Override
    public void cleanAll() {
        LambdaUpdateWrapper<SysLogLogin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysLogLogin::getDelFlag, 1)
                .set(SysLogLogin::getDelFlag, 0);
        mapper.update(wrapper);
    }
}




