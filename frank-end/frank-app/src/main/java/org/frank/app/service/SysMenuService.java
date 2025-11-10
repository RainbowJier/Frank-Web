package org.frank.app.service;

import cn.hutool.core.lang.tree.Tree;
import org.frank.shared.sysMenu.req.AddMenuReq;
import org.frank.shared.sysMenu.req.MenuListReq;
import org.frank.shared.sysMenu.req.UpdateMenuReq;
import org.frank.shared.sysMenu.resp.SysMenuResp;

import java.util.List;

public interface SysMenuService {

    /**
     * Get all menus M,C,F.
     */
    List<SysMenuResp> list(MenuListReq req);

    /**
     * Get menu detail info by id.
     */
    SysMenuResp getInfoById(Long menuId);

    /**
     * Update menu info.
     */
    void update(UpdateMenuReq req);

    /**
     * Add menu.
     */
    void add(AddMenuReq req);

    /**
     * Remove menu.
     */
    void remove(Long menuId);

    /**
     * Get menu tree select.
     */
    List<Tree<Long>> tree();

    /**
     * Get menu tree select.
     */
    List<Tree<Long>> roleTree();
}
