package org.frank.common.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.interceptor.TokenInterceptor;
import org.frank.common.properties.CorsProperties;
import org.frank.common.properties.ExcludePathsProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 配置拦截器和其他Web相关设置
 *
 * @author Frank
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private ExcludePathsProperties excludePathPatterns;
    
    @Resource
    private CorsProperties corsProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Configuring TokenInterceptor with exclude paths: {}", excludePathPatterns.getExclude());

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns.getExclude())
                .order(1); 

        log.info("TokenInterceptor configured successfully");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS mappings with properties: {}", corsProperties);
        
        // 解析允许的源
        String[] allowedOrigins = parseAllowedOrigins(corsProperties.getAllowedOrigins());
        
        // 解析允许的HTTP方法
        String[] allowedMethods = StringUtils.commaDelimitedListToStringArray(corsProperties.getAllowedMethods());
        
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(corsProperties.getAllowedHeaders())
                .exposedHeaders(corsProperties.getExposedHeaders())
                .allowCredentials(corsProperties.isAllowCredentials())
                .maxAge(corsProperties.getMaxAge());
        
        log.info("CORS configured successfully with origins: {}, methods: {}, allowCredentials: {}", 
                allowedOrigins, allowedMethods, corsProperties.isAllowCredentials());
    }
    
    /**
     * 解析允许的源配置
     * 支持通配符 * 和逗号分隔的多个源
     */
    private String[] parseAllowedOrigins(String allowedOrigins) {
        if (!StringUtils.hasText(allowedOrigins)) {
            return new String[]{"*"};
        }
        
        if ("*".equals(allowedOrigins.trim())) {
            return new String[]{"*"};
        }
        
        return StringUtils.commaDelimitedListToStringArray(allowedOrigins);
    }
}