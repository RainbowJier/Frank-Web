package org.frank.infrastructure.gateway;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.infrastructure.mapper.SysUserMapper;
import org.springframework.stereotype.Component;


@Component
public class SysUserGatewayImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserGateway {

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username)
                .eq(SysUser::getDelFlag, 1);
        return sysUserMapper.selectOne(queryWrapper);
    }
}




