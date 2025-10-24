package org.frank.common.resolver;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.common.components.TokenService;
import org.frank.common.core.domain.BaseReq;
import org.frank.common.core.domain.LoginUser;
import org.frank.common.util.ServletUtil;
import org.frank.common.util.StringUtil;
import org.frank.domain.gateway.ISysUserRelRoleGateway;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

/**
 * BaseReq参数自动解析器
 * 自动将当前登录用户信息赋值给BaseReq子类的参数
 *
 * @author Frank
 * @date 2024-12-19
 */
@Slf4j
@Component
public class BaseReqArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private TokenService tokenService;

    @Resource
    private ISysUserRelRoleGateway sysUserRelRoleGateway;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只支持BaseReq及其子类作为Controller方法参数
        return BaseReq.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // 创建BaseReq实例
        BaseReq baseReq = createBaseReqInstance(parameter.getParameterType());
        if (baseReq == null) {
            return null;
        }

        // 先解析前端传入的参数并填充到对象中
        bindRequestParameters(baseReq, webRequest);

        // 再获取当前登录用户信息并自动赋值
        LoginUser loginUser = getLoginUser();
        if (!ObjectUtils.isEmpty(loginUser)) {
            baseReq.setUserId(getUserId(loginUser));
            baseReq.setRoleIds(getRoleIds(getUserId(loginUser)));
            baseReq.setIsAdmin(isAdmin(getUserId(loginUser)));
        } else {
            log.warn("未找到登录用户信息，仅使用前端传入参数");
        }

        return baseReq;
    }

    /**
     * 绑定请求参数到BaseReq对象
     */
    private void bindRequestParameters(BaseReq baseReq, NativeWebRequest webRequest) throws Exception {
        jakarta.servlet.http.HttpServletRequest request = ServletUtil.getRequest();

        // 处理GET请求参数
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            bindGetParameters(baseReq, request);
        }
        // 处理POST/PUT请求的JSON参数
        else if (isJsonContentType(request)) {
            bindJsonParameters(baseReq, request);
        }
        // 处理表单参数
        else {
            bindFormParameters(baseReq, request);
        }
    }

    /**
     * 绑定GET请求参数
     */
    private void bindGetParameters(BaseReq baseReq, jakarta.servlet.http.HttpServletRequest request) {
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                setPropertyValue(baseReq, key, values[0]);
            }
        });
    }

    /**
     * 绑定JSON请求体参数
     */
    private void bindJsonParameters(BaseReq baseReq, jakarta.servlet.http.HttpServletRequest request) {
        try {
            String jsonBody = request.getReader().lines().reduce("", String::concat);
            if (StringUtil.hasText(jsonBody)) {
                try {
                    // 使用Jackson将JSON映射到BaseReq对象
                    BaseReq jsonBaseReq = objectMapper.readValue(jsonBody, baseReq.getClass());
                    // 复制属性值，保留已设置的用户信息
                    copyProperties(jsonBaseReq, baseReq, false);
                } catch (Exception e) {
                    log.warn("解析JSON参数失败: {}, JSON内容: {}", e.getMessage(), jsonBody);
                    // 如果是数组或集合类型的JSON，尝试直接绑定字段
                    tryAlternativeBinding(baseReq, jsonBody);
                }
            }
        } catch (Exception e) {
            log.error("读取请求体失败: {}", e.getMessage());
        }
    }

    /**
     * 尝试备用的参数绑定方法
     */
    private void tryAlternativeBinding(BaseReq baseReq, String jsonBody) {
        try {
            // 如果是简单的键值对格式，尝试手动解析
            if (jsonBody.startsWith("{") && jsonBody.endsWith("}")) {
                // 尝试解析为Map再绑定
                java.util.Map<String, Object> map = objectMapper.readValue(jsonBody, java.util.Map.class);
                map.forEach((key, value) -> {
                    if (value != null) {
                        setPropertyValue(baseReq, key, value.toString());
                    }
                });
            }
        } catch (Exception e) {
            log.debug("备用参数绑定也失败: {}", e.getMessage());
        }
    }

    /**
     * 绑定表单参数
     */
    private void bindFormParameters(BaseReq baseReq, jakarta.servlet.http.HttpServletRequest request) {
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                setPropertyValue(baseReq, key, values[0]);
            }
        });
    }

    /**
     * 判断是否为JSON内容类型
     */
    private boolean isJsonContentType(jakarta.servlet.http.HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().contains("application/json");
    }

    /**
     * 设置对象属性值（简化实现）
     */
    private void setPropertyValue(BaseReq baseReq, String propertyName, String value) {
        try {
            // 使用反射设置属性值
            java.lang.reflect.Field field = findField(baseReq.getClass(), propertyName);
            if (field != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (fieldType == String.class) {
                    field.set(baseReq, value);
                } else if (fieldType == Long.class || fieldType == long.class) {
                    field.set(baseReq, Long.parseLong(value));
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(baseReq, Integer.parseInt(value));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    field.set(baseReq, Boolean.parseBoolean(value));
                }
                // 可以根据需要添加更多类型的处理
            }
        } catch (Exception e) {
            log.debug("设置属性失败: {} = {}", propertyName, value, e);
        }
    }

    /**
     * 查找字段（包括父类）
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 复制属性（不覆盖用户信息字段）
     */
    private void copyProperties(BaseReq source, BaseReq target, boolean overwriteUserInfo) {
        try {
            java.lang.reflect.Field[] fields = source.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    // 跳过用户信息字段，除非允许覆盖
                    if (!overwriteUserInfo && isUserInfoField(field.getName())) {
                        continue;
                    }
                    field.set(target, value);
                }
            }
        } catch (Exception e) {
            log.warn("复制属性失败: {}", e.getMessage());
        }
    }

    /**
     * 判断是否为用户信息字段
     */
    private boolean isUserInfoField(String fieldName) {
        return "userId".equals(fieldName) ||
                "roleIds".equals(fieldName) ||
                "isAdmin".equals(fieldName);
    }

    /**
     * 获取当前登录用户信息
     */
    private LoginUser getLoginUser() {
        jakarta.servlet.http.HttpServletRequest request = ServletUtil.getRequest();
        return tokenService.getLoginUser(request);
    }

    /**
     * 获取用户ID
     */
    private Long getUserId(LoginUser loginUser) {
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * 获取用户角色ID列表
     */
    private java.util.List<Long> getRoleIds(Long userId) {
        if (userId == null) {
            return java.util.Collections.emptyList();
        }
        return sysUserRelRoleGateway.selectRoleIdsByUserId(userId);
    }

    /**
     * 判断用户是否为管理员
     */
    private boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        return sysUserRelRoleGateway.isAdmin(userId);
    }

    /**
     * 创建BaseReq实例
     */
    private BaseReq createBaseReqInstance(Class<?> parameterType) {
        try {
            return (BaseReq) parameterType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("创建BaseReq实例失败: {}", parameterType.getName(), e);
            return null;
        }
    }
}