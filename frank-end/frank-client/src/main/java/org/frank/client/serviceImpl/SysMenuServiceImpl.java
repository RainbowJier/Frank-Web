package org.frank.client.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysMenuService;
import org.frank.common.constant.UserConstants;
import org.frank.common.exception.BusinessException;
import org.frank.common.util.StringUtil;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.gateway.ISysMenuGateway;
import org.frank.domain.gateway.ISysRoleRelMenuGateway;
import org.frank.shared.sysMenu.req.AddMenuReq;
import org.frank.shared.sysMenu.req.MenuListReq;
import org.frank.shared.sysMenu.req.UpdateMenuReq;
import org.frank.shared.sysMenu.resp.SysMenuResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private ISysMenuGateway gateway;

    @Resource
    private ISysRoleRelMenuGateway sysRoleRelMenuGateway;

    @Override
    public List<SysMenuResp> list(MenuListReq req) {
        List<SysMenu> menuList;

        if (req.getIsAdmin()) {
            SysMenu query = BeanUtil.copyProperties(req, SysMenu.class);
            menuList = gateway.selectList(query, null);
        } else {
            menuList = getByRoleIds(req.getRoleIds());
        }

        return BeanUtil.copyToList(menuList, SysMenuResp.class);
    }

    /**
     * get menu list by role ids.
     */
    private List<SysMenu> getByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleRelMenuGateway.selectMenuIdsByRoleIds(roleIds);
        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        return gateway.selectListByIds(menuIds, null);
    }

    @Override
    public SysMenuResp getInfoById(Long menuId) {
        SysMenu menu = gateway.getById(menuId);
        return ObjectUtil.isEmpty(menu) ? null : BeanUtil.copyProperties(menu, SysMenuResp.class);
    }

    @Override
    @Transactional
    public void update(UpdateMenuReq req) {
        SysMenu menu = new SysMenu();
        BeanUtil.copyProperties(req, menu);

        if (gateway.checkMenuNameUniqueExcludeCurrent(menu.getMenuId(), menu.getParentId(), menu.getMenuName())) {
            throw new BusinessException("Failed to update menu: " + menu.getMenuName() + ", Menu name exist");
        } else if (UserConstants.YES_FRAME == menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            throw new BusinessException("Failed to update menu: " + menu.getMenuName() + ", The address must begin with http(s)://");
        } else if (Objects.equals(menu.getMenuId(), menu.getParentId())) {
            throw new BusinessException("Failed to update menu: " + menu.getMenuName() + ", Can not choose itself");
        }

        if (!gateway.updateById(menu)) {
            throw new BusinessException("Failed to update menu");
        }
    }

    @Override
    @Transactional
    public void add(AddMenuReq req) {
        SysMenu menu = new SysMenu();
        BeanUtil.copyProperties(req, menu);

        if (gateway.checkMenuNameUnique(menu.getParentId(), menu.getMenuName())) {
            throw new BusinessException("failed to update menu: " + menu.getMenuName() + ", Menu name exist");
        } else if (UserConstants.YES_FRAME == menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            throw new BusinessException("failed to update menu: " + menu.getMenuName() + ", The address must begin with http(s)://");
        }

        if (!gateway.save(menu)) {
            throw new BusinessException("Failed to add menu.");
        }
    }

    @Override
    @Transactional
    public void remove(Long menuId) {
        if (gateway.hasChildByMenuId(menuId)) {
            throw new BusinessException("Exist sub-menu and can not be deleted.");
        }
        List<Long> list = sysRoleRelMenuGateway.selectRoleIdsByMenuId(menuId);
        if (CollUtil.isEmpty(list)) {
            throw new BusinessException("The menu has been assigned and can not be deleted.");
        }

        if (!gateway.removeById(menuId)) {
            throw new BusinessException("Failed to delete menu.");
        }
    }
}
