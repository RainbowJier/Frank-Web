package org.frank.infrastructure.gateway;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.domain.entity.SysLogOper;
import org.frank.domain.gateway.ISysLogOperGateway;
import org.frank.infrastructure.mapper.SysLogOperMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SysLogOperGatewayImpl
        extends ServiceImpl<SysLogOperMapper, SysLogOper>
        implements ISysLogOperGateway {

    @Resource
    private SysLogOperMapper mapper;

    @Override
    public void cleanAll() {
        LambdaUpdateWrapper<SysLogOper> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysLogOper::getDelFlag, 1)
                .set(SysLogOper::getDelFlag, 0);
        mapper.update(wrapper);
    }
}