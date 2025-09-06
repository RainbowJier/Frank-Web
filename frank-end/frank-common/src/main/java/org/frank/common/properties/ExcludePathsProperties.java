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
@ConfigurationProperties(prefix = "interceptor")
public class ExcludePathsProperties {

    private List<String> exclude = new ArrayList<>();

    /**
     * 获取排除路径列表
     */
    public List<String> getExclude() {
        if (exclude == null) {
            exclude = new ArrayList<>();
        }
        return exclude;
    }

    /**
     * 设置排除路径列表
     */
    public void setExclude(List<String> exclude) {
        this.exclude = exclude != null ? exclude : new ArrayList<>();
        log.info("Exclude paths configured: {}", this.exclude);
    }

    /**
     * 转换为数组格式
     */
    public String[] getExcludeArray() {
        return getExclude().toArray(new String[0]);
    }

    /**
     * 检查路径是否被排除
     */
    public boolean isPathExcluded(String path) {
        return getExclude().contains(path);
    }
}