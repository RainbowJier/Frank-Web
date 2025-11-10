package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.frank.app.service.SysRoleService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.domain.entity.SysRole;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.shared.sysRole.req.SysRoleAddReq;
import org.frank.shared.sysRole.req.SysRoleQueryReq;
import org.frank.shared.sysRole.resp.SysRoleResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private ISysRoleGateway gateway;

    @Resource
    private ISysUserRelRoleGateway userRoleGateway;

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
        wrapper.like(StringUtils.hasText(params.getRoleName()), SysRole::getRoleName, params.getRoleName())
                .like(StringUtils.hasText(params.getRoleKey()), SysRole::getRoleKey, params.getRoleKey())
                .eq(ObjectUtil.isNotEmpty(params.getStatus()), SysRole::getStatus, params.getStatus());
        if (params.getBeginTime() != null && params.getEndTime() != null) {
            wrapper.between(SysRole::getCreateTime, params.getBeginTime(), params.getEndTime());
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
    }
}
