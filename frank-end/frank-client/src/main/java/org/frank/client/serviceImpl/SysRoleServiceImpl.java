package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.frank.app.service.SysRoleService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.domain.entity.SysRole;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysRoleRelMenuGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.shared.sysRole.req.SysRoleAddReq;
import org.frank.shared.sysRole.req.SysRoleChangeStatusReq;
import org.frank.shared.sysRole.req.SysRoleQueryReq;
import org.frank.shared.sysRole.req.SysRoleUpdateReq;
import org.frank.shared.sysRole.resp.SysRoleResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private ISysRoleGateway gateway;

    @Resource
    private ISysUserRelRoleGateway userRoleGateway;

    @Resource
    private ISysRoleRelMenuGateway roleMenuGateway;

    @Override
    public List<SysRoleResp> getRoleList(Long userId) {
        List<Long> roleIds = userRoleGateway.selectRoleIdsByUserId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<SysRole> sysRoles = gateway.selectListByIds(roleIds);
        if (CollUtil.isEmpty(sysRoles)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(sysRoles, SysRoleResp.class);

    }

    @Override
    public PageResult selectRoleList(SysRoleQueryReq params) {
        IPage<SysRole> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getRoleName()), SysRole::getRoleName, params.getRoleName())
                .like(StringUtils.isNotBlank(params.getRoleKey()), SysRole::getRoleKey, params.getRoleKey())
                .eq(ObjectUtil.isNotEmpty(params.getStatus()), SysRole::getStatus, params.getStatus());
        if (StringUtils.isNotBlank(params.getBeginTime()) && StringUtils.isNotBlank(params.getEndTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime beginTime = LocalDate.parse(params.getBeginTime(), formatter).atStartOfDay();
            LocalDateTime endTime = LocalDate.parse(params.getEndTime(), formatter).atTime(LocalTime.MAX);
            wrapper.between(SysRole::getCreateTime, beginTime, endTime);
        }

        IPage<SysRole> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysRoleResp.class);
    }

    @Override
    public SysRoleResp getById(Long roleId) {
        SysRole sysRole = gateway.getById(roleId);
        if (sysRole == null) {
            return null;
        }
        return BeanUtil.copyProperties(sysRole, SysRoleResp.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(SysRoleAddReq req) {
        if (!gateway.checkRoleNameUnique(req.getRoleName())) {
            throw new BusinessException("Role name is already existed.");
        }

        if (!gateway.checkRoleKeyUnique(req.getRoleKey())) {
            throw new BusinessException("Role key is already existed.");
        }

        SysRole role = BeanUtil.copyProperties(req, SysRole.class);
        if (BooleanUtils.isFalse(gateway.save(role))) {
            throw new BusinessException("Fail to add role.");
        }

        // role relate mune.
        relatedMenu(req.getMenuIds(), role.getRoleId());
    }

    private void relatedMenu(List<Long> menuIds, Long roleId) {
        // 先删除原有的角色菜单关联
        roleMenuGateway.removeByRoleId(roleId);

        if (CollUtil.isEmpty(menuIds)) {
            return;
        }

        // 重新建立角色菜单关联
        if (BooleanUtil.isFalse(roleMenuGateway.saveBatchRoleMenu(roleId, menuIds))) {
            throw new BusinessException("Fail to related menu.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            if (SysRole.isAdmin(roleId)) {
                throw new BusinessException("Administrator cannot be deleted.");
            }
        }

        // Check if there are any users currently using these roles.
        for (Long roleId : roleIds) {
            List<Long> userIds = userRoleGateway.selectUserIdsByRoleId(roleId);
            if (CollUtil.isNotEmpty(userIds)) {
                throw new BusinessException("There are users currently using these roles.");
            }
        }

        // remove role-menu relation
        roleMenuGateway.removeBatchByRoleIds(roleIds);

        // remove role
        gateway.removeByIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateReq req) {
        // Check if admin role (role cannot be modified)
        if (SysRole.isAdmin(req.getRoleId())) {
            throw new BusinessException("Administrator role cannot be modified");
        }

        // Check role name uniqueness (exclude current role)
        if (!gateway.checkRoleNameUniqueExcludeCur(req.getRoleId(), req.getRoleName())) {
            throw new BusinessException("Role name already exists");
        }

        // Check role key uniqueness (exclude current role)
        if (!gateway.checkRoleKeyUniqueExcludeCur(req.getRoleId(), req.getRoleKey())) {
            throw new BusinessException("Role key string already exists");
        }

        // update related menu.
        relatedMenu(req.getMenuIds(), req.getRoleId());

        // update role info
        SysRole updateRole = BeanUtil.copyProperties(req, SysRole.class);
        if (BooleanUtils.isFalse(gateway.updateById(updateRole))) {
            throw new BusinessException("Failed to update role");
        }

        log.info("Successfully updated role, roleId: {}, roleName: {}", req.getRoleId(), req.getRoleName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(SysRoleChangeStatusReq req) {
        // Check if admin role (role cannot be modified)
        if (SysRole.isAdmin(req.getRoleId())) {
            throw new BusinessException("Administrator role status cannot be modified");
        }

        // Check if role exists
        SysRole existRole = gateway.getById(req.getRoleId());
        if (existRole == null) {
            throw new BusinessException("Role does not exist");
        }

        // Update role status
        SysRole updateRole = new SysRole();
        updateRole.setRoleId(req.getRoleId());
        updateRole.setStatus(req.getStatus());

        if (BooleanUtils.isFalse(gateway.updateById(updateRole))) {
            throw new BusinessException("Failed to update role status");
        }

        log.info("Successfully changed role status, roleId: {}, status: {}", req.getRoleId(), req.getStatus());
    }
}
