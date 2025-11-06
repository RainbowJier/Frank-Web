package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
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
import org.frank.shared.sysUser.req.SysUserAddReq;
import org.frank.shared.sysUser.req.SysUserReq;
import org.frank.shared.sysUser.resp.SysUserResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private ISysUserGateway gateway;

    @Override
    public PageResult selectUserList(SysUserReq params) {
        IPage<SysUser> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(params.getUserId()), SysUser::getUserId, params.getUserId())
                .like(StringUtils.hasText(params.getUserName()), SysUser::getUserName, params.getUserName())
                .like(StringUtils.hasText(params.getPhoneNumber()), SysUser::getPhoneNumber, params.getPhoneNumber());

        if(params.getBeginTime() != null && params.getEndTime() != null){
            wrapper.between(SysUser::getCreateTime, params.getBeginTime(), params.getEndTime());
        }

        IPage<SysUser> pageRes = gateway.page(page, wrapper);
        return PageResult.ok(pageRes, SysUserResp.class);
    }

    @Override
    public SysUserResp getById(Long userId) {
        SysUser sysUser = gateway.getById(userId);
        if(ObjectUtils.isEmpty(sysUser)){
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

        if(BooleanUtils.isFalse(gateway.save(user))){
            throw new BusinessException("新增用户失败");
        }
    }
}
