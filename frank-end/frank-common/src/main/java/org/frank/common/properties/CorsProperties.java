package org.frank.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 跨域配置属性类
 * 
 * @author Frank
 */
@Data
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    
    /**
     * 允许的源
     */
    private String allowedOrigins = "*";
    
    /**
     * 允许的HTTP方法
     */
    private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS,PATCH";
    
    /**
     * 允许的请求头
     */
    private String allowedHeaders = "*";
    
    /**
     * 暴露的响应头
     */
    private String exposedHeaders = "*";
    
    /**
     * 是否允许发送凭证信息
     */
    private boolean allowCredentials = false;
    
    /**
     * 预检请求结果缓存时间（秒）
     */
    private long maxAge = 3600;
}