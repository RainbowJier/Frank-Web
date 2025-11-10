package org.frank.client.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
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
import org.frank.shared.sysMenu.resp.SysMenuTreeResp;
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
        SysMenu query = BeanUtil.copyProperties(req, SysMenu.class);
        List<SysMenu> menuList = getMenuList(req.getIsAdmin(), req.getRoleIds(), query);

        return BeanUtil.copyToList(menuList, SysMenuResp.class);
    }

    private List<SysMenu> getMenuList(Boolean isAdmin, List<Long> roleIds, SysMenu query) {
        List<SysMenu> menuList;
        if (BooleanUtil.isTrue(isAdmin)) {
            menuList = gateway.selectList(query);
        } else {
            menuList = getByRoleIds(roleIds, query);
        }
        return menuList;
    }

    private List<SysMenu> getByRoleIds(List<Long> roleIds, SysMenu query) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleRelMenuGateway.selectMenuIdsByRoleIds(roleIds);
        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        return gateway.selectListByIdsAndCondition(menuIds, query);
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
        if (CollUtil.isNotEmpty(list)) {
            throw new BusinessException("The menu has been assigned and can not be deleted.");
        }

        if (!gateway.removeById(menuId)) {
            throw new BusinessException("Failed to delete menu.");
        }
    }

    @Override
    public List<Tree<Long>> tree() {
        List<SysMenu> menuList = gateway.list();
        if (CollUtil.isEmpty(menuList)) {
            return null;
        }
        List<SysMenuTreeResp> menuTreeList = BeanUtil.copyToList(menuList, SysMenuTreeResp.class);

        TreeNodeConfig config = TreeNodeConfig.DEFAULT_CONFIG
                .setIdKey("id")
                .setParentIdKey("parentId")
                .setChildrenKey("children")
                .setWeightKey("orderNum");

        return TreeUtil.build(menuTreeList, 0L, config, (menu, tree) -> {
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.setName(menu.getMenuName());
            tree.setWeight(menu.getOrderNum());
            tree.putExtra("children", menu.getChildren());
        });
    }

    @Override
    public List<Tree<Long>> roleTree(List<Long> roleIds, Boolean isAdmin, Long roleId) {
        List<SysMenu> menuList = getMenuList(isAdmin, roleIds, null);
        List<SysMenuTreeResp> menuTreeList = BeanUtil.copyToList(menuList, SysMenuTreeResp.class);

        TreeNodeConfig config = TreeNodeConfig.DEFAULT_CONFIG
                .setIdKey("id")
                .setParentIdKey("parentId")
                .setChildrenKey("children")
                .setWeightKey("orderNum");

        return TreeUtil.build(menuTreeList, 0L, config, (menu, tree) -> {
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.setName(menu.getMenuName());
            tree.setWeight(menu.getOrderNum());
            tree.putExtra("children", menu.getChildren());
        });
    }
}
