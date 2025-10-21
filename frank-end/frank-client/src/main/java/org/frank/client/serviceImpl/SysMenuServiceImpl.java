package org.frank.client.serviceImpl;


import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysLoginService;
import org.frank.app.service.SysMenuService;
import org.frank.common.components.TokenService;
import org.frank.common.constant.CacheConstants;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.core.redis.RedisCache;
import org.frank.common.exception.AuthenticationException;
import org.frank.common.util.sign.BCryptUtils;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.frank.shared.sysLogin.req.LoginReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        return List.of();
    }
}
