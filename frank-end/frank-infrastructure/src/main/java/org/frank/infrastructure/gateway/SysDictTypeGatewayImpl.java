package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.frank.domain.entity.SysDictType;
import org.frank.domain.gateway.ISysDictTypeGateway;
import org.frank.infrastructure.mapper.SysDictTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class SysDictTypeGatewayImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
        implements ISysDictTypeGateway {

}




