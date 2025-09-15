package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysRoleRelMenu;

import java.util.List;

public interface ISysRoleRelMenuGateway extends IService<SysRoleRelMenu> {

    /**
     * 批量保存角色菜单关联
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 保存结果
     */
    boolean saveBatchRoleMenu(Long roleId, List<Long> menuIds);

    /**
     * 根据角色ID删除角色菜单关联
     * @param roleId 角色ID
     * @return 删除结果
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 根据菜单ID删除角色菜单关联
     * @param menuId 菜单ID
     * @return 删除结果
     */
    boolean deleteByMenuId(Long menuId);

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 根据菜单ID查询角色ID列表
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByMenuId(Long menuId);


    /**
     * 根据角色ID列表查询菜单ID列表
     * @param roleIds
     * @return
     */
    List<Long> selectMenuIdsByRoleIds(List<Long> roleIds);
}