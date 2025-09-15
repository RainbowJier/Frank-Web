package org.frank.infrastructure.gateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.domain.entity.SysRoleRelMenu;
import org.frank.domain.gateway.ISysRoleRelMenuGateway;
import org.frank.infrastructure.mapper.SysRoleRelMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleRelMenuGatewayImpl extends ServiceImpl<SysRoleRelMenuMapper, SysRoleRelMenu> implements ISysRoleRelMenuGateway {

    @Resource
    private SysRoleRelMenuMapper sysRoleRelMenuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchRoleMenu(Long roleId, List<Long> menuIds) {
        try {
            // 先删除原有的角色菜单关联
            deleteByRoleId(roleId);

            if (menuIds == null || menuIds.isEmpty()) {
                return true;
            }

            // 批量插入新的角色菜单关联
            List<SysRoleRelMenu> roleMenuList = menuIds.stream()
                    .map(menuId -> {
                        SysRoleRelMenu roleMenu = new SysRoleRelMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());

            return saveBatch(roleMenuList);
        } catch (Exception e) {
            log.error("批量保存角色菜单关联失败，roleId: {}, menuIds: {}", roleId, menuIds, e);
            throw new RuntimeException("批量保存角色菜单关联失败", e);
        }
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }

        LambdaQueryWrapper<SysRoleRelMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleRelMenu::getRoleId, roleId);

        return remove(queryWrapper);
    }

    @Override
    public boolean deleteByMenuId(Long menuId) {
        if (menuId == null) {
            return false;
        }

        LambdaQueryWrapper<SysRoleRelMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleRelMenu::getMenuId, menuId);

        return remove(queryWrapper);
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRoleRelMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleRelMenu::getRoleId, roleId);
        queryWrapper.select(SysRoleRelMenu::getMenuId);

        List<SysRoleRelMenu> roleMenuList = list(queryWrapper);
        return roleMenuList.stream()
                .map(SysRoleRelMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> selectRoleIdsByMenuId(Long menuId) {
        if (menuId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRoleRelMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleRelMenu::getMenuId, menuId);
        queryWrapper.select(SysRoleRelMenu::getRoleId);

        List<SysRoleRelMenu> roleMenuList = list(queryWrapper);
        return roleMenuList.stream()
                .map(SysRoleRelMenu::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> selectMenuIdsByRoleIds(List<Long> roleIds) {
        LambdaQueryWrapper<SysRoleRelMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleRelMenu::getRoleId, roleIds);

        List<SysRoleRelMenu> roleMenuList = sysRoleRelMenuMapper.selectList(queryWrapper);
        return roleMenuList.stream()
                .map(SysRoleRelMenu::getMenuId)
                .collect(Collectors.toList());
    }
}