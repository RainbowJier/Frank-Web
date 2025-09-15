package org.frank.infrastructure.gateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.domain.entity.SysUserRelRole;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.infrastructure.mapper.SysUserRelRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserRelRoleGatewayImpl extends ServiceImpl<SysUserRelRoleMapper, SysUserRelRole> implements ISysUserRelRoleGateway {

    @Resource
    private SysUserRelRoleMapper sysUserRelRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchUserRole(Long userId, List<Long> roleIds) {
        try {
            // 先删除原有的用户角色关联
            deleteByUserId(userId);

            if (roleIds == null || roleIds.isEmpty())    return true;

            // 批量插入新的用户角色关联
            List<SysUserRelRole> userRoleList = roleIds.stream()
                    .map(roleId -> {
                        SysUserRelRole userRole = new SysUserRelRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());

            return saveBatch(userRoleList);
        } catch (Exception e) {
            log.error("批量保存用户角色关联失败，userId: {}, roleIds: {}", userId, roleIds, e);
            throw new RuntimeException("批量保存用户角色关联失败", e);
        }
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        if (userId == null) return false;

        LambdaQueryWrapper<SysUserRelRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRelRole::getUserId, userId);

        return remove(queryWrapper);
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }

        LambdaQueryWrapper<SysUserRelRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRelRole::getRoleId, roleId);

        return remove(queryWrapper);
    }

    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        if (userId == null) return Collections.emptyList();

        LambdaQueryWrapper<SysUserRelRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRelRole::getUserId, userId);
        List<SysUserRelRole> userRoleList = sysUserRelRoleMapper.selectList(queryWrapper);

        return userRoleList.stream()
                .map(SysUserRelRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> selectUserIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUserRelRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRelRole::getRoleId, roleId);
        queryWrapper.select(SysUserRelRole::getUserId);

        List<SysUserRelRole> userRoleList = list(queryWrapper);
        return userRoleList.stream()
                .map(SysUserRelRole::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdmin(Long userId) {
        if (userId == null) return false;
        List<Long> roleIds = selectRoleIdsByUserId(userId);
        return roleIds.contains(1L);
    }
}