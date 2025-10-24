package org.frank.client.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.frank.app.service.SysPermissionService;
import org.frank.common.constant.Constants;
import org.frank.common.constant.UserConstants;
import org.frank.common.enums.MenuEnum;
import org.frank.common.util.StringUtil;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysRole;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysMenuGateway;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysRoleRelMenuGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.shared.userPermission.resp.MetaResp;
import org.frank.shared.userPermission.resp.RouterResp;
import org.frank.shared.userPermission.resp.SysMenuResp;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private ISysRoleGateway sysRoleGateway;

    @Resource
    private ISysUserRelRoleGateway sysUserRelRoleGateway;

    @Resource
    private ISysRoleRelMenuGateway sysRoleRelMenuGateway;

    @Resource
    private ISysMenuGateway sysMenuGateway;


    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();

        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            List<SysRole> sysRoleList = getSysRoleList(user.getUserId());
            sysRoleList.forEach(sysRole -> roles.add(sysRole.getRoleKey()));
        }
        return roles;
    }

    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(getMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }


    @Override
    public List<SysMenuResp> selectMenuList(SysUser sysUser) {
        List<SysMenu> menus;

        List<String> menuTypeList = Arrays.asList(MenuEnum.MENU.getCode(), MenuEnum.CAIDAN.getCode());
        if (sysUser.isAdmin()) {
            menus = sysMenuGateway.selectList(new SysMenu(), menuTypeList);
        } else {
            menus = getMenuTreeByUserId(sysUser.getUserId(), menuTypeList);
        }

        List<SysMenuResp> list = BeanUtil.copyToList(menus, SysMenuResp.class);

        return getChildPerms(list, 0L);
    }


    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenuResp> getChildPerms(List<SysMenuResp> list, Long parentId) {
        List<SysMenuResp> returnList = new ArrayList<>();
        for (SysMenuResp t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (Objects.equals(t.getParentId(), parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenuResp> list, SysMenuResp t) {
        // 得到子节点列表
        List<SysMenuResp> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuResp tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuResp> list, SysMenuResp t) {
        return !getChildList(list, t).isEmpty();
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuResp> getChildList(List<SysMenuResp> list, SysMenuResp t) {
        List<SysMenuResp> tlist = new ArrayList<>();
        for (SysMenuResp n : list) {
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }


    private List<SysRole> getSysRoleList(Long userId) {
        List<Long> roleIds = sysUserRelRoleGateway.selectRoleIdsByUserId(userId);
        return sysRoleGateway.getBaseMapper().selectByIds(roleIds);
    }

    /**
     * get menu permission by user id.
     */
    private List<String> getMenuPermsByUserId(Long userId) {
        return getPermsByRoleIds(sysUserRelRoleGateway.selectRoleIdsByUserId(userId));
    }

    /**
     * get menu permission by role ids.
     */
    private List<String> getPermsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleRelMenuGateway.selectMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        return sysMenuGateway.selectByIds(menuIds).stream()
                .map(SysMenu::getPerms)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * get menu tree by user id.
     */
    private List<SysMenu> getMenuTreeByUserId(Long userId, List<String> menuTypeList) {
        return getMenuTreeByRoleIds(sysUserRelRoleGateway.selectRoleIdsByUserId(userId), menuTypeList);
    }


    /**
     * get menu tree by role ids.
     */
    private List<SysMenu> getMenuTreeByRoleIds(List<Long> roleIds, List<String> menuTypeList) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleRelMenuGateway.selectMenuIdsByRoleIds(roleIds);
        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        return sysMenuGateway.selectListByIds(menuIds, menuTypeList);
    }


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterResp> buildMenus(List<SysMenuResp> menus) {
        List<RouterResp> routers = new LinkedList<>();
        for (SysMenuResp menu : menus) {
            RouterResp router = new RouterResp();
            router.setHidden(menu.getVisible() != UserConstants.MENU_VISIBLE_SHOW);
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaResp(menu.getMenuName(), menu.getIcon(), menu.getIsCache() == UserConstants.MENU_CACHE_DISABLED, menu.getPath()));
            List<SysMenuResp> cMenus = menu.getChildren();
            if (StringUtil.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterResp> childrenList = new ArrayList<>();
                RouterResp children = new RouterResp();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaResp(menu.getMenuName(), menu.getIcon(), menu.getIsCache() == UserConstants.MENU_CACHE_DISABLED, menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaResp(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterResp> childrenList = new ArrayList<>();
                RouterResp children = new RouterResp();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaResp(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuResp menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuResp menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuResp menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME)) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuResp menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtil.ishttp(menu.getPath());
    }


    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuResp menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }


    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuResp menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }


    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }


}
