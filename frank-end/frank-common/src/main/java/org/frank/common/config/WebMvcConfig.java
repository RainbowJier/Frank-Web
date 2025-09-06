package org.frank.common.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.interceptor.TokenInterceptor;
import org.frank.common.properties.ExcludePathsProperties;
import org.springframework.context.annotation.Configuration;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Configuring TokenInterceptor with exclude paths: {}", excludePathPatterns.getExclude());

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns.getExclude())
                .order(1); // 设置拦截器顺序

        log.info("TokenInterceptor configured successfully");
    }
}