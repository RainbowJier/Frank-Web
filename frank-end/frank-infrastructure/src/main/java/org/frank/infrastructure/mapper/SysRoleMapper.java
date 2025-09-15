package org.frank.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.frank.domain.entity.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    SysRole selectByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    SysRole selectByRoleKey(@Param("roleKey") String roleKey);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 查询所有正常状态的角色
     *
     * @return 角色列表
     */
    List<SysRole> selectAllNormalRoles();

    /**
     * 根据条件查询角色列表
     *
     * @param roleName 角色名称
     * @param status   角色状态
     * @param dataScope 数据范围
     * @return 角色列表
     */
    List<SysRole> selectRolesByCondition(@Param("roleName") String roleName, 
                                       @Param("status") Integer status, 
                                       @Param("dataScope") String dataScope);

    /**
     * 检查角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 是否唯一
     */
    boolean checkRoleNameUnique(@Param("roleName") String roleName);

    /**
     * 检查角色权限字符串是否唯一
     *
     * @param roleKey 角色权限字符串
     * @return 是否唯一
     */
    boolean checkRoleKeyUnique(@Param("roleKey") String roleKey);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(@Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色ID数组
     * @return 删除数量
     */
    int deleteRoleByIds(@Param("roleIds") Long[] roleIds);
}




