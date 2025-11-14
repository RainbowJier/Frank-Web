package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysLogLogin;
import org.frank.domain.gateway.ISysLogLoginGateway;
import org.frank.infrastructure.mapper.SysLogLoginMapper;
import org.springframework.stereotype.Service;


@Service
public class SysLogLoginServiceImpl
        extends ServiceImpl<SysLogLoginMapper, SysLogLogin>
        implements ISysLogLoginGateway {

    @Resource
    private SysLogLoginMapper mapper;


}




