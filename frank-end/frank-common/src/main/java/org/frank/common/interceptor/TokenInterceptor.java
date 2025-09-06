package org.frank.common.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.AjaxResult;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Token验证拦截器
 * 用于拦截需要Token认证的请求
 *
 * @author Frank
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.debug("Processing request for URI: {}", requestURI);

        // 获取token
        String token = tokenService.getToken(request);
        if (token == null || token.trim().isEmpty()) {
            log.warn("Token is missing for URI: {}", requestURI);
            handleUnauthorized(response, "Token不能为空");
            return false;
        }

        // 验证token有效性
        if (tokenService.getLoginUser(request) == null) {
            log.warn("Token is invalid or expired for URI: {}", requestURI);
            handleUnauthorized(response, "Token无效或已过期");
            return false;
        }

        log.debug("Token validation successful for URI: {}", requestURI);
        return true;
    }

    /**
     * 处理未授权响应
     *
     * @param response HTTP响应对象
     * @param message  错误消息
     * @throws IOException IO异常
     */
    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        AjaxResult<?> result = AjaxResult.unauthorized(message);
        response.getWriter().write(String.valueOf(result));
    }
}