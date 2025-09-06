package org.frank.common.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器排除路径配置属性
 * 用于配置不需要进行Token验证的路径
 *
 * @author Frank
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "interceptor")
public class ExcludePathsProperties {
    
    /**
     * 不需要验证token的路径列表
     * 支持Ant风格的通配符，如：/frankweb/doc.html/**
     */
    private List<String> exclude = new ArrayList<>();

    /**
     * 获取排除路径列表
     * 如果列表为空，返回默认的排除路径
     *
     * @return 排除路径列表
     */
    public List<String> getExclude() {
        if (exclude == null || exclude.isEmpty()) {
            log.warn("No exclude paths configured, using default empty list");
            return new ArrayList<>();
        }
        return exclude;
    }

    /**
     * 设置排除路径列表
     *
     * @param exclude 排除路径列表
     */
    public void setExclude(List<String> exclude) {
        this.exclude = exclude != null ? exclude : new ArrayList<>();
        log.debug("Exclude paths configured: {}", this.exclude);
    }
}