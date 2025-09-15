package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysMenuGateway;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.infrastructure.mapper.SysMenuMapper;
import org.frank.infrastructure.mapper.SysUserMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SysMenuGatewayImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuGateway {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> selectByIds(List<Long> menuIds) {
        return sysMenuMapper.selectByIds(menuIds);
    }
}




