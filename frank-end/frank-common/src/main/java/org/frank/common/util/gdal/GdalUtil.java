package org.frank.common.util.gdal;

import cn.hutool.core.io.FileUtil;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * <h2>GDAL 地理空间数据处理工具类 (GdalUtil)</h2>
 * <p>
 * 核心组件：基于 GDAL/OGR 库 (Native C++) 实现对 GDB (File Geodatabase) 文件的高性能读写。
 * </p>
 *
 * <h3>🚀 核心功能</h3>
 * <ul>
 * <li><b>多图层合并查询:</b> 支持同时查询多个图层，自动计算全局分页偏移量 (Offset/Limit)，并发读取。</li>
 * <li><b>内存去重查询:</b> 针对跨图层存在重复数据的场景，支持基于字段内容的内存去重 (Set Deduplication)。</li>
 * <li><b>大数据导出:</b> 采用流式游标 (Streaming) 将数据导出到新 GDB，<b>强制不去重</b>以防止 OOM，自动添加时间戳后缀防止覆盖。</li>
 * </ul>
 *
 * <h3>📝 使用示例 (Usage Examples)</h3>
 *
 * <h4>场景 1：普通分页查询 (High Performance)</h4>
 * <pre>
 * {@code
 * @Resource private GdalUtil gdalUtil;
 *
 * public void search() {
 * GdalQueryParam param = new GdalQueryParam();
 * param.setGdbPath("C:/data/source.gdb");       // GDB 路径
 * param.setLayerNames(Arrays.asList("DLTB"));   // 图层名 (可多个)
 * param.setWhereClause("JULI > 100");           // SQL 过滤 (OGR SQL 语法)
 * param.setReturnFields(Arrays.asList("BSM"));  // 仅返回特定字段 (提速)
 * param.setPageNum(1);                          // 第 1 页
 * param.setPageSize(20);                        // 每页 20 条
 *
 * // 返回结果：包含总条数、当前页数据、查询耗时
 * PageResult<Map<String, Object>> result = gdalUtil.queryData(param);
 * }
 * }
 * </pre>
 *
 * <h4>场景 2：内存去重查询 (Use with Caution)</h4>
 * <pre>
 * {@code
 * public void distinctSearch() {
 * GdalQueryParam param = new GdalQueryParam();
 * param.setGdbPath("C:/data/source.gdb");
 * param.setDistinct(true); // <--- 开启去重 (注意：会全量加载符合条件的数据到内存)
 *
 * // 即使开启去重，依然支持返回分页结构，但底层是内存分页
 * PageResult<Map<String, Object>> result = gdalUtil.queryData(param);
 * }
 * }
 * </pre>
 *
 * <h4>场景 3：数据导出 (Export)</h4>
 * <pre>
 * {@code
 * public void export() {
 * GdalQueryParam param = new GdalQueryParam();
 * param.setGdbPath("C:/data/source.gdb");
 * param.setTargetGdbPath("D:/data/backup.gdb"); // 目标路径
 * param.setLayerNames(Arrays.asList("DLTB"));
 *
 * // 执行导出
 * // 结果：D:/data/backup.gdb 中将生成图层 "DLTB_20231122120000"
 * gdalUtil.exportToGdb(param);
 * }
 * }
 * </pre>
 *
 * @author Frank
 * @see GdalQueryParam
 * @see PageResult
 */
@Slf4j
@Component
public class GdalUtil {

    @Resource(name = "taskExecutor")
    private Executor executor;

    // ========================================================================
    // 1. 公共入口 (Public Entry Point)
    // ========================================================================

    /**
     * 多图层合并查询入口
     */
    public PageResult<Map<String, Object>> queryData(GdalQueryParam param) {
        log.info(">>> GDAL 查询开始 | 路径: [{}] | 去重: {} | 分页: {}/{} | 图层: {} | 条件: [{}]",
                param.getGdbPath(), param.getDistinct(), param.getPageNum(), param.getPageSize(),
                param.getLayerNames(), param.getWhereClause());

        try {
            validateFile(param.getGdbPath());

            PageResult<Map<String, Object>> result;
            if (Boolean.TRUE.equals(param.getDistinct())) {
                log.info("--- 执行策略：内存去重查询 (注意：数据量过大可能导致慢查询) ---");
                result = executeDistinctQuery(param);
            } else {
                log.info("--- 执行策略：普通分页查询 (计算偏移量) ---");
                result = executeNormalQuery(param);
            }

            log.info("<<< GDAL 查询结束 | 总数: {} | 返回记录数: {} | 耗时: {}",
                    result.getTotal(), result.getRecords().size(), result.getQueryTime());
            return result;

        } catch (Exception e) {
            log.error("!!! GDAL 查询异常 | 路径: {}", param.getGdbPath(), e);
            throw e;
        }
    }

    /**
     * 导出数据到 GDB (指定目录 + 自动命名版)
     * <p>
     * 1. <b>输出目录</b>：取自 param.getTargetGdbPath()。
     * 2. <b>文件名生成</b>：源GDB文件名 + _yyyyMMddHHmmss + .gdb。
     * 3. <b>图层命名</b>：保持原名 (因为是新文件，无冲突)。
     * </p>
     */
    public void exportToGdb(GdalQueryParam param) {
        long start = System.currentTimeMillis();

        // 1. 基础校验
        validateFile(param.getGdbPath()); // 校验源 GDB 是否存在
        String targetDirStr = param.getTargetGdbPath();

        if (targetDirStr == null || targetDirStr.isEmpty()) {
            throw new RuntimeException("导出失败：必须指定目标存放目录 (targetGdbPath)");
        }

        // 2. 准备目录与文件名
        File sourceFile = new File(param.getGdbPath());
        File targetDir = new File(targetDirStr);

        // 如果目标目录不存在，自动创建
        if (!targetDir.exists()) {
            boolean created = targetDir.mkdirs();
            if (!created) {
                throw new RuntimeException("无法创建目标目录: " + targetDirStr);
            }
        }

        // 3. 构建新 GDB 的完整路径
        // 逻辑：源文件名(去掉后缀) + 时间戳 + .gdb
        String sourceName = sourceFile.getName();
        String nameNoExt = sourceName.contains(".") ?
                sourceName.substring(0, sourceName.lastIndexOf(".")) : sourceName;
        String timeSuffix = new SimpleDateFormat("_yyyyMMddHHmmss").format(new Date());

        // 最终路径： D:/backup/source_20231122120000.gdb
        File finalTargetFile = new File(targetDir, nameNoExt + timeSuffix + ".gdb");
        String finalTargetPath = finalTargetFile.getAbsolutePath();

        log.info(">>> GDAL 导出开始");
        log.info("    源文件: [{}]", param.getGdbPath());
        log.info("    输出至: [{}]", finalTargetPath);

        if (Boolean.TRUE.equals(param.getDistinct())) {
            log.warn("!!! 提示：导出模式下自动忽略去重参数，采用流式全量传输。");
        }

        ogr.RegisterAll();
        Driver driver = ogr.GetDriverByName("OpenFileGDB");
        if (driver == null) driver = ogr.GetDriverByName("FileGDB");
        if (driver == null) throw new RuntimeException("未找到 GDB 驱动 (OpenFileGDB/FileGDB)");

        DataSource targetDs = null;
        try {
            // 4. 创建全新的目标 GDB
            targetDs = driver.CreateDataSource(finalTargetPath);
            if (targetDs == null) {
                throw new RuntimeException("无法创建 GDB 文件，请检查权限或路径: " + finalTargetPath);
            }

            // 获取需要导出的源图层列表
            List<String> sourceLayers = getTargetLayerNames(param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer());
            log.info("    包含图层: {} 个 -> 开始导出...", sourceLayers.size());

            for (String sourceLayerName : sourceLayers) {
                // 5. 关键：目标图层名直接使用源图层名 (无需改名)
                processExportLayer(param.getGdbPath(), sourceLayerName, sourceLayerName, param.getWhereClause(), targetDs);
            }
            log.info("<<< GDAL 导出成功 | 耗时: {}ms", System.currentTimeMillis() - start);

        } catch (Exception e) {
            log.error("!!! GDAL 导出失败", e);
            // 导出失败时，可选：清理生成的半成品文件
            FileUtil.del(finalTargetFile);
            throw e;
        } finally {
            if (targetDs != null) targetDs.delete(); // 释放资源
        }
    }

    // ========================================================================
    // 2. 普通查询策略 (保持不变)
    // ========================================================================

    private PageResult<Map<String, Object>> executeNormalQuery(GdalQueryParam param) {
        long startTime = System.currentTimeMillis();

        List<String> targetLayers = getTargetLayerNames(
                param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer()
        );
        log.debug("识别目标图层: {}", targetLayers);

        Map<String, Long> layerCounts = getLayerCountsParallel(param, targetLayers);
        long globalTotal = layerCounts.values().stream().mapToLong(Long::longValue).sum();
        log.info("统计完成。总记录数: {} | 各图层明细: {}", globalTotal, layerCounts);

        PageResult<Map<String, Object>> result = buildEmptyPageResult(param, globalTotal);
        if (globalTotal == 0) {
            result.setQueryTime(calcTime(startTime));
            return result;
        }

        List<LayerFetchTask> fetchTasks = planFetchTasks(layerCounts, result.getPageNum(), result.getPageSize());
        if (fetchTasks.isEmpty()) {
            result.setQueryTime(calcTime(startTime));
            return result;
        }

        List<CompletableFuture<List<Map<String, Object>>>> fetchFutures = fetchTasks.stream()
                .map(task -> CompletableFuture.supplyAsync(() ->
                        readLayerSegment(param, task), executor))
                .collect(Collectors.toList());

        List<Map<String, Object>> allRecords = fetchFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        result.setRecords(allRecords);
        result.setQueryTime(calcTime(startTime));
        return result;
    }

    private List<Map<String, Object>> readLayerSegment(GdalQueryParam param, LayerFetchTask task) {
        DataSource ds = null;
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            ds = ogr.Open(param.getGdbPath(), 0);
            Layer layer = ds.GetLayer(task.layerName);
            if (layer == null) return results;

            if (param.getWhereClause() != null && !param.getWhereClause().isEmpty()) {
                layer.SetAttributeFilter(param.getWhereClause());
            }

            layer.SetNextByIndex(task.offset);
            Feature feat;
            int count = 0;
            List<String> returnFields = param.getReturnFields();

            while (count < task.limit && (feat = layer.GetNextFeature()) != null) {
                results.add(convertFeature(feat, returnFields));
                count++;
            }
        } catch (Exception e) {
            log.error("分段读取失败: " + task.layerName, e);
        } finally {
            if (ds != null) ds.delete();
        }
        return results;
    }

    // ========================================================================
    // 3. 去重查询策略 (保持不变)
    // ========================================================================

    private PageResult<Map<String, Object>> executeDistinctQuery(GdalQueryParam param) {
        long startTime = System.currentTimeMillis();
        List<String> targetLayers = getTargetLayerNames(param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer());

        List<CompletableFuture<List<Map<String, Object>>>> futureList = targetLayers.stream()
                .map(layerName -> CompletableFuture.supplyAsync(() ->
                        readLayerAll(param, layerName), executor))
                .collect(Collectors.toList());

        Set<Map<String, Object>> distinctSet = new LinkedHashSet<>();
        for (CompletableFuture<List<Map<String, Object>>> future : futureList) {
            distinctSet.addAll(future.join());
        }

        List<Map<String, Object>> distinctList = new ArrayList<>(distinctSet);
        int total = distinctList.size();
        int pageSize = (param.getPageSize() == null || param.getPageSize() < 1) ? (total > 0 ? total : 10) : param.getPageSize();
        int fromIndex = (param.getPageNum() - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<Map<String, Object>> pageRecords;
        if (fromIndex >= total) {
            pageRecords = new ArrayList<>();
        } else {
            pageRecords = distinctList.subList(fromIndex, toIndex);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(param.getPageNum());
        result.setPageSize(pageSize);
        result.setTotalPages((total + pageSize - 1) / pageSize);
        result.setRecords(pageRecords);
        result.setQueryTime(calcTime(startTime));
        return result;
    }

    private List<Map<String, Object>> readLayerAll(GdalQueryParam param, String layerName) {
        DataSource ds = null;
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            ds = ogr.Open(param.getGdbPath(), 0);
            Layer layer = ds.GetLayer(layerName);
            if (layer == null) return results;

            if (param.getWhereClause() != null && !param.getWhereClause().isEmpty()) {
                layer.SetAttributeFilter(param.getWhereClause());
            }
            layer.ResetReading();
            Feature feat;
            List<String> returnFields = param.getReturnFields();
            while ((feat = layer.GetNextFeature()) != null) {
                results.add(convertFeature(feat, returnFields));
            }
        } catch (Exception e) {
            log.error("全量读取失败: " + layerName, e);
        } finally {
            if (ds != null) ds.delete();
        }
        return results;
    }

    // ========================================================================
    // 4. 核心转换与工具方法 (Utilities)
    // ========================================================================

    private void processExportLayer(String sourcePath, String sourceLayerName, String targetLayerName, String whereClause, DataSource targetDs) {
        DataSource sourceDs = null;
        try {
            sourceDs = ogr.Open(sourcePath, 0);
            Layer sourceLayer = sourceDs.GetLayer(sourceLayerName);
            if (sourceLayer == null) {
                log.warn("源图层不存在: {}", sourceLayerName);
                return;
            }

            if (whereClause != null && !whereClause.isEmpty()) {
                sourceLayer.SetAttributeFilter(whereClause);
            }

            long featCount = sourceLayer.GetFeatureCount();
            if (featCount == 0) {
                log.debug("跳过空图层: {}", sourceLayerName);
                return;
            }

            log.info("正在导出: {} -> {} | 记录数: {}", sourceLayerName, targetLayerName, featCount);

            Layer targetLayer = targetDs.CreateLayer(targetLayerName, sourceLayer.GetSpatialRef(), sourceLayer.GetGeomType());
            if (targetLayer == null) {
                throw new RuntimeException("创建目标图层失败: " + targetLayerName);
            }

            FeatureDefn sourceDefn = sourceLayer.GetLayerDefn();
            for (int i = 0; i < sourceDefn.GetFieldCount(); i++) {
                targetLayer.CreateField(sourceDefn.GetFieldDefn(i));
            }

            targetLayer.StartTransaction();
            sourceLayer.ResetReading();
            Feature srcFeat;
            int exportedCount = 0;

            while ((srcFeat = sourceLayer.GetNextFeature()) != null) {
                Feature targetFeat = new Feature(targetLayer.GetLayerDefn());
                targetFeat.SetFrom(srcFeat);
                targetLayer.CreateFeature(targetFeat);

                targetFeat.delete();
                srcFeat.delete();
                exportedCount++;
            }
            targetLayer.CommitTransaction();
            log.debug("图层导出完毕: {} ({} 行)", targetLayerName, exportedCount);

        } catch (Exception e) {
            log.error("图层导出出错: " + sourceLayerName, e);
            throw new RuntimeException("Export layer failed: " + sourceLayerName, e);
        } finally {
            if (sourceDs != null) sourceDs.delete();
        }
    }

    private Map<String, Object> convertFeature(Feature feat, List<String> returnFields) {
        Map<String, Object> attrs = new LinkedHashMap<>();
        if (returnFields != null && !returnFields.isEmpty()) {
            for (String fieldName : returnFields) {
                int idx = feat.GetFieldIndex(fieldName);
                if (idx != -1) {
                    attrs.put(fieldName, feat.GetFieldAsString(idx));
                }
            }
        } else {
            int fieldCount = feat.GetFieldCount();
            for (int i = 0; i < fieldCount; i++) {
                FieldDefn defn = feat.GetFieldDefnRef(i);
                attrs.put(defn.GetName(), feat.GetFieldAsString(i));
            }
        }
        return attrs;
    }

    private Map<String, Long> getLayerCountsParallel(GdalQueryParam param, List<String> targetLayers) {
        Map<String, Long> layerCounts = new LinkedHashMap<>();
        List<CompletableFuture<LayerCount>> countFutures = targetLayers.stream()
                .map(layerName -> CompletableFuture.supplyAsync(() ->
                        countLayer(param.getGdbPath(), layerName, param.getWhereClause()), executor))
                .collect(Collectors.toList());

        countFutures.stream().map(CompletableFuture::join).forEach(lc -> layerCounts.put(lc.layerName, lc.count));
        return layerCounts;
    }

    private LayerCount countLayer(String gdbPath, String layerName, String whereClause) {
        DataSource ds = null;
        try {
            ds = ogr.Open(gdbPath, 0);
            Layer layer = ds.GetLayer(layerName);
            if (layer == null) return new LayerCount(layerName, 0L);
            if (whereClause != null && !whereClause.isEmpty()) layer.SetAttributeFilter(whereClause);
            return new LayerCount(layerName, layer.GetFeatureCount());
        } catch (Exception e) {
            log.error("统计图层失败: " + layerName, e);
            return new LayerCount(layerName, 0L);
        } finally {
            if (ds != null) ds.delete();
        }
    }

    private List<LayerFetchTask> planFetchTasks(Map<String, Long> layerCounts, int pageNum, int pageSize) {
        List<LayerFetchTask> tasks = new ArrayList<>();
        if (pageSize <= 0) return tasks;

        long globalStart = (long) (pageNum - 1) * pageSize;
        long globalEnd = globalStart + pageSize;
        long currentScanIndex = 0;

        for (Map.Entry<String, Long> entry : layerCounts.entrySet()) {
            String layerName = entry.getKey();
            long count = entry.getValue();
            long layerEndIndex = currentScanIndex + count;

            if (layerEndIndex > globalStart && currentScanIndex < globalEnd) {
                long localOffset = Math.max(0, globalStart - currentScanIndex);
                long effectiveEnd = Math.min(globalEnd, layerEndIndex);
                long localLimit = effectiveEnd - (currentScanIndex + localOffset);

                if (localLimit > 0) {
                    tasks.add(new LayerFetchTask(layerName, localOffset, localLimit));
                }
            }
            currentScanIndex += count;
            if (currentScanIndex >= globalEnd) break;
        }
        return tasks;
    }

    private PageResult<Map<String, Object>> buildEmptyPageResult(GdalQueryParam param, long total) {
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(param.getPageNum());
        result.setPageSize(param.getPageSize());
        result.setRecords(new ArrayList<>());
        int pageSize = (param.getPageSize() == null || param.getPageSize() < 1) ? 0 : param.getPageSize();
        if (pageSize > 0) {
            result.setTotalPages((int) ((total + pageSize - 1) / pageSize));
        } else {
            result.setTotalPages(total > 0 ? 1 : 0);
        }
        return result;
    }

    private List<String> getTargetLayerNames(String gdbPath, List<String> userLayers, Boolean fuzzyMatch) {
        DataSource ds = ogr.Open(gdbPath, 0);
        if (ds == null) {
            log.error("无法打开 GDB 进行图层检查: {}", gdbPath);
            throw new RuntimeException("无法打开 GDB 文件: " + gdbPath);
        }
        List<String> allLayers = new ArrayList<>();
        int count = ds.GetLayerCount();
        for (int i = 0; i < count; i++) allLayers.add(ds.GetLayer(i).GetName());
        ds.delete();

        if (userLayers == null || userLayers.isEmpty()) {
            return allLayers;
        }

        if (Boolean.TRUE.equals(fuzzyMatch)) {
            return allLayers.stream().filter(l -> userLayers.stream().anyMatch(l::contains)).collect(Collectors.toList());
        } else {
            return userLayers.stream().filter(allLayers::contains).collect(Collectors.toList());
        }
    }

    private void validateFile(String path) {
        if (path == null || !new File(path).exists()) {
            log.error("GDB 文件校验失败。路径不存在: {}", path);
            throw new RuntimeException("File not found: " + path);
        }
    }

    private String calcTime(long startTime) {
        return (System.currentTimeMillis() - startTime) + "ms";
    }

    @Data
    @AllArgsConstructor
    private static class LayerCount {
        String layerName;
        Long count;
    }

    @Data
    @AllArgsConstructor
    private static class LayerFetchTask {
        String layerName;
        long offset;
        long limit;
    }
}