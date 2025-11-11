package org.frank.client.serviceImpl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.util.sign.BCryptUtils;
import org.frank.domain.entity.SysUser;
import org.frank.domain.gateway.ISysUserGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据初始化服务
 * 用于系统启动时初始化基础数据
 *
 * @author Frank
 * @since 2025-11-11
 */
@Slf4j
@Service
public class DataInitServiceImpl {

    private final ISysUserGateway userGateway;

    public DataInitServiceImpl(ISysUserGateway userGateway) {
        this.userGateway = userGateway;
    }

    /**
     * 系统启动后初始化管理员账号
     */
    @PostConstruct
    @Transactional
    public void initAdminUser() {
        try {
            // 检查管理员账号是否已存在
            SysUser existAdmin = userGateway.getById(1L);
            if (existAdmin != null) {
                log.info("管理员账号已存在，跳过初始化");
                return;
            }

            // 创建管理员账号
            SysUser admin = new SysUser();
            admin.setUserId(1L); // 手动指定管理员ID为1
            admin.setUserName("admin");
            admin.setNickName("administrator");
            admin.setEmail("frank@163.com");
            admin.setSex(1);
            admin.setPassword(BCryptUtils.hashPassword("admin123")); // 默认密码
            admin.setStatus(1);

            boolean success = userGateway.save(admin);
            if (success) {
                log.info("管理员账号初始化成功，用户名: admin, 默认密码: admin123");
            } else {
                log.error("管理员账号初始化失败");
            }

        } catch (Exception e) {
            log.error("初始化管理员账号时发生异常", e);
        }
    }
}