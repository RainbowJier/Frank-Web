package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysMenu;

import java.util.List;

public interface ISysMenuGateway extends IService<SysMenu> {

    List<SysMenu> selectByIds(List<Long> menuIds);

    /**
     * get menu tree by menuIds.
     */
    List<SysMenu> selectListByIds(List<Long> menuIds, List<String> menuTypeList);

    /**
     * get menu list by conditions.
     */
    List<SysMenu> selectList(SysMenu query, List<String> menuTypeList);

    /**
     * check menu name unique.
     */
    boolean checkMenuNameUnique(Long parentId, String menuName);

    /**
     * check menu name unique exclude current menu.
     */
    boolean checkMenuNameUniqueExcludeCurrent(Long menuId, Long parentId, String menuName);

    /**
     * check menu has child.
     */
    boolean hasChildByMenuId(Long menuId);
}