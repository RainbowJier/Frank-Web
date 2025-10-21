package org.frank.app.service;

import org.frank.domain.entity.SysMenu;

import java.util.List;

public interface SysMenuService {

    /**
     * 获取菜单列表
     *
     * @param menu
     * @param userId
     * @return
     */
    List<SysMenu> selectMenuList(SysMenu menu, Long userId);
}
