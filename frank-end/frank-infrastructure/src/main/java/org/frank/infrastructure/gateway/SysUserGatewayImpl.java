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
    private SysUserMapper mapper;

    @Override
    public SysUser selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username);
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, userName);
        return count(queryWrapper) == 0;
    }

    @Override
    public boolean checkPhoneUnique(String phoneNumber) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhoneNumber, phoneNumber);
        return count(queryWrapper) == 0;
    }

    @Override
    public boolean checkEmailUnique(String email) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, email);
        return count(queryWrapper) == 0;
    }
}




