package org.frank.infrastructure.gateway;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.gateway.ISysMenuGateway;
import org.frank.infrastructure.mapper.SysMenuMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SysMenuGatewayImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuGateway {

    @Resource
    private SysMenuMapper mapper;

    @Override
    public List<SysMenu> selectByIds(List<Long> menuIds) {
        return mapper.selectByIds(menuIds);
    }

    @Override
    public List<SysMenu> selectListByIds(List<Long> menuIds, List<String> menuTypeList) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollUtil.isNotEmpty(menuTypeList), SysMenu::getMenuType, menuTypeList)
                .in(CollUtil.isNotEmpty(menuIds), SysMenu::getMenuId, menuIds)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<SysMenu> selectList(SysMenu query, List<String> menuTypeList) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotEmpty(query.getMenuName()), SysMenu::getMenuName, query.getMenuName())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), SysMenu::getStatus, query.getStatus())
                .in(CollUtil.isNotEmpty(menuTypeList), SysMenu::getMenuType, menuTypeList)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);

        return mapper.selectList(queryWrapper);
    }

    @Override
    public boolean checkMenuNameUnique(Long parentId, String menuName) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getMenuName, menuName)
                .eq(SysMenu::getParentId, parentId);
        return mapper.selectCount(queryWrapper) <= 0;
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, menuId);
        return mapper.selectCount(queryWrapper) > 0;
    }
}




