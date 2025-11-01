package org.frank.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.frank.common.components.TokenService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Resource
    private TokenService tokenService;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 获取当前登录用户
        Long userId = getCurrentUserId();

        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 获取当前登录用户
        Long userId = getCurrentUserId();

        this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * 获取当前登录用户
     */
    private Long getCurrentUserId() {
        try {
            // 方法2: 从自定义的上下文工具类获取
            // return SecurityUtils.getUsername();

            // 方法3: 从 ThreadLocal 获取
            // return UserContextHolder.getCurrentUser();

            return 1L; // 默认用户
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return 1L;
        }
    }
}