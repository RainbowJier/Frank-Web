package org.frank.common.config.gdal;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * GDAL初始化配置
 *
 * @author Frank
 * @since 2025-11-17
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "gdal", name = "enable", havingValue = "true")
public class GdalInitializer {

    /**
     * 初始化 GDAL 库，在 Spring 容器启动后执行，确保GDAL库正确加载和配置
     * 只有在配置文件中设置 gdal.enable=true 时才会执行
     */
    @PostConstruct
    public void initializeGdal() {
        log.info("===========GDAL加载配置开始===============");
        log.info("GDAL版本信息: {}", gdal.VersionInfo("--version"));

        // 注册矢量数据驱动
        log.debug("注册矢量数据驱动...");
        ogr.RegisterAll();

        // 注册栅格数据驱动
        log.debug("注册栅格数据驱动...");
        gdal.AllRegister();

        // 配置支持中文路径
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");

        // 配置属性表字段支持中文
        gdal.SetConfigOption("SHAPE_ENCODING", "CP936");

        // 验证GDAL是否正确初始化
        int driverCount = gdal.GetDriverCount();
        log.info("GDAL已加载数据驱动数量: {}", driverCount);

        if (driverCount == 0) {
            throw new RuntimeException("GDAL数据驱动加载失败");
        }
        log.info("===========GDAL加载配置完成===============");
    }
}