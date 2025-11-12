package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.frank.app.service.SysUserService;
import org.frank.common.core.page.PageResult;
import org.frank.common.exception.BusinessException;
import org.frank.common.util.sign.BCryptUtils;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.frank.shared.sysUser.req.*;
import org.frank.shared.sysUser.resp.SysUserResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private ISysUserGateway gateway;

    @Resource
    private ISysUserRelRoleGateway userRelRoleGateway;

    @Override
    public PageResult selectUserList(SysUserQueryReq params) {
        IPage<SysUser> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(params.getUserId()), SysUser::getUserId, params.getUserId())
                .eq(ObjectUtil.isNotEmpty(params.getStatus()), SysUser::getStatus, params.getStatus())
                .like(StringUtils.hasText(params.getUserName()), SysUser::getUserName, params.getUserName())
                .like(StringUtils.hasText(params.getPhoneNumber()), SysUser::getPhoneNumber, params.getPhoneNumber());

        if (params.getBeginTime() != null && params.getEndTime() != null) {
            wrapper.between(SysUser::getCreateTime, params.getBeginTime(), params.getEndTime());
        }

        IPage<SysUser> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysUserResp.class);
    }

    @Override
    public SysUserResp getById(Long userId) {
        SysUser sysUser = gateway.getById(userId);
        if (ObjectUtils.isEmpty(sysUser)) {
            return null;
        }
        return BeanUtil.copyProperties(sysUser, SysUserResp.class);
    }

    @Override
    @Transactional
    public void addUser(SysUserAddReq req) {
        if (!gateway.checkUserNameUnique(req.getUserName())) {
            throw new BusinessException("User name is already existed.");
        }

        if (StringUtils.hasText(req.getPhoneNumber()) && !gateway.checkPhoneUnique(req.getPhoneNumber())) {
            throw new BusinessException("Phone number is already existed.");
        }

        if (StringUtils.hasText(req.getEmail()) && !gateway.checkEmailUnique(req.getEmail())) {
            throw new BusinessException("Mail is already existed.");
        }

        SysUser user = BeanUtil.copyProperties(req, SysUser.class);
        user.setPassword(BCryptUtils.hashPassword(req.getPassword()));

        if (BooleanUtils.isFalse(gateway.save(user))) {
            throw new BusinessException("Fail to add.");
        }
    }

    @Override
    @Transactional
    public void updateUser(SysUserUpdateReq req) {
        SysUser existingUser = checkUserExists(req.getUserId());

        // 校验手机号格式（如果提供了手机号）
        if (StringUtils.hasText(req.getPhoneNumber())) {
            if (!req.getPhoneNumber().matches("^1[3-9]\\d{9}$")) {
                throw new BusinessException("format of phone number is incorrect.");
            }
            if (!req.getPhoneNumber().equals(existingUser.getPhoneNumber()) &&
                    !gateway.checkPhoneUniqueExcludeCurrent(req.getPhoneNumber(), req.getUserId())) {
                throw new BusinessException("Phone number is already existed.");
            }
        }

        // 校验邮箱格式（如果提供了邮箱）
        if (StringUtils.hasText(req.getEmail())) {
            if (!req.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new BusinessException("format of mail is incorrect");
            }
            if (!req.getEmail().equals(existingUser.getEmail()) &&
                    !gateway.checkEmailUniqueExcludeCurrent(req.getEmail(), req.getUserId())) {
                throw new BusinessException("Mail is already existed.");
            }
        }

        SysUser user = BeanUtil.copyProperties(req, SysUser.class);
        if (BooleanUtils.isFalse(gateway.updateById(user))) {
            throw new BusinessException("Fail to update user info.");
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> userIds) {
        if (BooleanUtils.isFalse(gateway.removeByIds(userIds))) {
            throw new BusinessException("Fail to delete users.");
        }
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordReq req) {
        SysUser user = checkUserExists(req.getUserId());
        user.setPassword(BCryptUtils.hashPassword(req.getPassword()));

        if (BooleanUtils.isFalse(gateway.updateById(user))) {
            throw new BusinessException("Fail to reset password.");
        }
    }

    @Override
    @Transactional
    public void changeStatus(ChangeStatusReq req) {
        SysUser user = checkUserExists(req.getUserId());

        user.setStatus(req.getStatus());
        if (BooleanUtils.isFalse(gateway.updateById(user))) {
            throw new BusinessException("Fail to change status.");
        }
    }

    @Override
    public PageResult selectAllocatedUserList(AllocatedUserQueryReq req) {
        List<Long> userIds = userRelRoleGateway.selectUserIdsByRoleId(req.getRoleId());
        if (userIds.isEmpty()) {
            return new PageResult(0L, Long.valueOf(req.getPageNum()), Long.valueOf(req.getPageSize()), List.of());
        }

        IPage<SysUser> page = new Page<>(req.getPageNum(), req.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIds)
                .like(StringUtils.hasText(req.getUserName()), SysUser::getUserName, req.getUserName())
                .like(StringUtils.hasText(req.getPhoneNumber()), SysUser::getPhoneNumber, req.getPhoneNumber());

        IPage<SysUser> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysUserResp.class);
    }

    @Override
    public PageResult selectUnallocatedUserList(AllocatedUserQueryReq req) {
        List<Long> allocatedUserIds = userRelRoleGateway.selectUserIdsByRoleId(req.getRoleId());
        allocatedUserIds.add(1L);   // exclude administrator.

        IPage<SysUser> page = new Page<>(req.getPageNum(), req.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(req.getUserName()), SysUser::getUserName, req.getUserName())
                .like(StringUtils.hasText(req.getPhoneNumber()), SysUser::getPhoneNumber, req.getPhoneNumber());

        if (CollUtil.isNotEmpty(allocatedUserIds)) {
            wrapper.notIn(SysUser::getUserId, allocatedUserIds);
        }

        IPage<SysUser> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysUserResp.class);
    }


    private SysUser checkUserExists(Long userId) {
        SysUser user = gateway.getById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("The user is not existed.");
        }
        return user;
    }
}
