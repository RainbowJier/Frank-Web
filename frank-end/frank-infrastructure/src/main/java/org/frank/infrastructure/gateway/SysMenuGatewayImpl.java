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

    @Override
    public List<SysMenu> selectMenuTreeAll() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenu::getMenuType, "M", "C")
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);

        return sysMenuMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysMenu> selectMenuTreeByIds(List<Long> menuIds) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenu::getMenuType, "M", "C")
                .in(SysMenu::getMenuId, menuIds)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);

        return sysMenuMapper.selectList(queryWrapper);
    }
}




