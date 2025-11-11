package org.frank.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.util.ServletUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Resource
    private TokenService tokenService;

    private Long getCurrentUserId() {
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            return loginUser != null ? loginUser.getUserId() : null;
        } catch (Exception e) {
            log.warn("获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Long userId = getCurrentUserId();
        log.info("当前操作用户ID: {}", userId);

        // 确保时间字段不为null
        Date now = new Date();

        // 使用严格的插入填充，如果字段存在才填充
        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);

        // 为del_flag设置默认值（正常状态）
        this.strictInsertFill(metaObject, "delFlag", Integer.class, 1);
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