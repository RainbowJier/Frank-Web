package org.frank.infrastructure.gateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysRole;
import org.frank.domain.gateway.ISysRoleGateway;
import org.frank.infrastructure.mapper.SysRoleMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class SysRoleGatewayImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleGateway {

    @Resource
    private SysRoleMapper mapper;

    @Override
    public List<SysRole> selectListByIds(List<Long> roleIds) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        return mapper.selectList(queryWrapper.in(SysRole::getRoleId, roleIds));
    }

    @Override
    public boolean checkRoleNameUnique(String roleName) {
        if (!StringUtils.hasText(roleName)) {
            return true;
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleName, roleName);
        return count(queryWrapper) == 0;
    }

    @Override
    public boolean checkRoleKeyUnique(String roleKey) {
        if (!StringUtils.hasText(roleKey)) {
            return true;
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleKey, roleKey);
        return count(queryWrapper) == 0;
    }
}