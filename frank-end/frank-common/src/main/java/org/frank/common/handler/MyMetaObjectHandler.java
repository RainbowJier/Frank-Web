package org.frank.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.util.ServletUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Resource
    @Lazy
    private TokenService tokenService;

    /**
     * 获取当前登录用户ID
     * 避免依赖BaseController造成循环依赖
     */
    private Long getCurrentUserId() {
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            return loginUser != null ? loginUser.getUserId() : null;
        } catch (
                Exception e) {
            log.warn("获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Long userId = getCurrentUserId();
        log.info("当前操作用户ID: {}", userId);

        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Long userId = getCurrentUserId();
        log.info("当前操作用户ID: {}", userId);

        this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}