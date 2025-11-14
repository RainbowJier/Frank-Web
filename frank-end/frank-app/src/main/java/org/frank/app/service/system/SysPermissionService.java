package org.frank.app.service.system;

import org.frank.domain.entity.SysUser;
import org.frank.shared.userPermission.resp.RouterResp;
import org.frank.shared.userPermission.resp.SysMenuResp;

import java.util.List;
import java.util.Set;

public interface SysPermissionService {

    /**
     * get role permission
     * @param user
     * @return
     */
    Set<String> getRolePermission(SysUser user);


    /**
     * get menu permission
     * @param user
     * @return
     */
    Set<String> getMenuPermission(SysUser user);

    /**
     * get menu list
     * @param sysUser
     * @return
     */
    List<SysMenuResp> selectMenuList(SysUser sysUser);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterResp> buildMenus(List<SysMenuResp> menus);

}
