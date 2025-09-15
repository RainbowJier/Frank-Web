package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysUserRelRole;

import java.util.List;

public interface ISysUserRelRoleGateway extends IService<SysUserRelRole> {

    /**
     * 批量保存用户角色关联
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 保存结果
     */
    boolean saveBatchUserRole(Long userId, List<Long> roleIds);

    /**
     * 根据用户ID删除用户角色关联
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteByUserId(Long userId);

    /**
     * 根据角色ID删除用户角色关联
     * @param roleId 角色ID
     * @return 删除结果
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(Long userId);

    /**
     * 根据角色ID查询用户ID列表
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(Long roleId);

    /**
     * check is admin or not.
     * @param userId
     * @return
     */
    boolean isAdmin(Long userId);
}