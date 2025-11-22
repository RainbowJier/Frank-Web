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
                log.info("--- 执行策略：内存去重查询 (注意：全量加载) ---");
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
     * 导出数据到 GDB (指定目录 + 自动命名 + 批量事务)
     */
    public void exportToGdb(GdalQueryParam param) {
        long start = System.currentTimeMillis();

        // 1. 基础校验
        validateFile(param.getGdbPath());
        String targetDirStr = param.getTargetGdbPath();
        if (targetDirStr == null || targetDirStr.isEmpty()) {
            throw new RuntimeException("导出失败：必须指定目标存放目录 (targetGdbPath)");
        }

        // 2. 准备目录与文件名
        File sourceFile = new File(param.getGdbPath());
        File targetDir = new File(targetDirStr);

        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                throw new RuntimeException("无法创建目标目录: " + targetDirStr);
            }
        }

        // 构造文件名: SourceName_yyyyMMddHHmmss.gdb
        String sourceName = sourceFile.getName();
        String nameNoExt = sourceName.contains(".") ?
                sourceName.substring(0, sourceName.lastIndexOf(".")) : sourceName;
        String timeSuffix = new SimpleDateFormat("_yyyyMMddHHmmss").format(new Date());

        File finalTargetFile = new File(targetDir, nameNoExt + timeSuffix + ".gdb");
        String finalTargetPath = finalTargetFile.getAbsolutePath();

        log.info(">>> GDAL 导出开始 | 源: [{}] -> 目标: [{}]", param.getGdbPath(), finalTargetPath);

        if (Boolean.TRUE.equals(param.getDistinct())) {
            log.warn("!!! 提示：导出模式下忽略去重参数");
        }

        // 3. 获取驱动 (优先尝试 OpenFileGDB，它不需要安装额外 SDK)
        Driver driver = ogr.GetDriverByName("OpenFileGDB");
        if (driver == null) driver = ogr.GetDriverByName("FileGDB");
        if (driver == null) throw new RuntimeException("未找到 GDB 驱动 (OpenFileGDB/FileGDB)");

        DataSource targetDs = null;
        try {
            // 4. 创建目标 GDB
            targetDs = driver.CreateDataSource(finalTargetPath);
            if (targetDs == null) {
                throw new RuntimeException("无法创建 GDB 文件: " + finalTargetPath);
            }

            List<String> sourceLayers = getTargetLayerNames(param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer());
            log.info("    包含图层: {} 个 -> 开始流式导出", sourceLayers.size());

            for (String sourceLayerName : sourceLayers) {
                // 目标图层名 = 源图层名 (新文件无冲突)
                processExportLayer(param.getGdbPath(), sourceLayerName, sourceLayerName, param.getWhereClause(), targetDs);
            }
            log.info("<<< GDAL 导出成功 | 耗时: {}ms", System.currentTimeMillis() - start);

        } catch (Exception e) {
            log.error("!!! GDAL 导出失败", e);
            // 失败清理：尝试删除生成的 GDB 文件夹
            try {
                if (targetDs != null) targetDs.delete(); // 先释放句柄
                FileUtil.del(finalTargetFile);
            } catch (Exception ex) {
                log.warn("清理垃圾文件失败: {}", finalTargetPath);
            }
            throw e;
        } finally {
            if (targetDs != null) targetDs.delete(); // 再次确保释放
        }
    }

    // ========================================================================
    // 2. 普通查询策略 (Optimized)
    // ========================================================================

    private PageResult<Map<String, Object>> executeNormalQuery(GdalQueryParam param) {
        long startTime = System.currentTimeMillis();

        List<String> targetLayers = getTargetLayerNames(
                param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer()
        );

        // 并发统计各图层总数
        Map<String, Long> layerCounts = getLayerCountsParallel(param, targetLayers);
        long globalTotal = layerCounts.values().stream().mapToLong(Long::longValue).sum();

        PageResult<Map<String, Object>> result = buildEmptyPageResult(param, globalTotal);
        if (globalTotal == 0) {
            result.setQueryTime(calcTime(startTime));
            return result;
        }

        // 计算分页任务分配
        List<LayerFetchTask> fetchTasks = planFetchTasks(layerCounts, result.getPageNum(), result.getPageSize());
        if (fetchTasks.isEmpty()) {
            result.setQueryTime(calcTime(startTime));
            return result;
        }

        // 并发读取数据 (Core)
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
            ds = ogr.Open(param.getGdbPath(), 0); // 0 = ReadOnly
            Layer layer = ds.GetLayer(task.layerName);
            if (layer == null) return results;

            if (param.getWhereClause() != null && !param.getWhereClause().isEmpty()) {
                layer.SetAttributeFilter(param.getWhereClause());
            }

            // [优化] 预先构建字段索引映射，避免循环内 GetFieldIndex (高性能关键)
            Map<String, Integer> fieldIndexMap = buildFieldIndexMap(layer, param.getReturnFields());

            layer.SetNextByIndex(task.offset);
            Feature feat;
            int count = 0;

            while (count < task.limit && (feat = layer.GetNextFeature()) != null) {
                // [优化] 使用索引读取
                results.add(convertFeatureOptimized(feat, fieldIndexMap));

                // [优化] 必须显式 delete，否则 Native 内存泄漏
                feat.delete();
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
    // 3. 去重查询策略 (双重去重优化版)
    // ========================================================================

    /**
     * 执行去重查询
     * 策略：SQL层去重 (Stage 1) -> 内存合并去重 (Stage 2) -> 内存分页
     */
    private PageResult<Map<String, Object>> executeDistinctQuery(GdalQueryParam param) {
        long startTime = System.currentTimeMillis();
        List<String> targetLayers = getTargetLayerNames(param.getGdbPath(), param.getLayerNames(), param.getFuzzyMatchLayer());

        // 并发执行：每个图层都在 SQL 层面先做一次 DISTINCT
        List<CompletableFuture<List<Map<String, Object>>>> futureList = targetLayers.stream()
                .map(layerName -> CompletableFuture.supplyAsync(() ->
                        readLayerWithSqlDistinct(param, layerName), executor))
                .collect(Collectors.toList());

        // Stage 2: 内存合并去重
        // 即使每个图层内部去重了，图层 A 和 图层 B 之间可能还有重复数据，所以这里 Set 不能省
        Set<Map<String, Object>> distinctSet = new LinkedHashSet<>();

        for (CompletableFuture<List<Map<String, Object>>> future : futureList) {
            // 这里 join 拿到的已经是瘦身后的数据了，内存压力骤减
            distinctSet.addAll(future.join());
        }

        // 内存分页逻辑 (保持不变)
        List<Map<String, Object>> distinctList = new ArrayList<>(distinctSet);
        int total = distinctList.size();
        int pageSize = (param.getPageSize() == null || param.getPageSize() < 1) ? (total > 0 ? total : 10) : param.getPageSize();
        int fromIndex = (param.getPageNum() - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<Map<String, Object>> pageRecords = (fromIndex >= total) ?
                new ArrayList<>() : distinctList.subList(fromIndex, toIndex);

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(param.getPageNum());
        result.setPageSize(pageSize);
        result.setTotalPages((total + pageSize - 1) / pageSize);
        result.setRecords(pageRecords);
        result.setQueryTime(calcTime(startTime));
        return result;
    }

    /**
     * 基于 SQL 的去重读取 (Stage 1 Deduplication)
     * 利用 OGR SQL 的 "SELECT DISTINCT" 能力，减少 JNI 数据传输量
     */
    private List<Map<String, Object>> readLayerWithSqlDistinct(GdalQueryParam param, String layerName) {
        DataSource ds = null;
        Layer sqlResultLayer = null;
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            // 注意：ExecuteSQL 需要以 ReadOnly 方式打开，否则可能锁文件
            ds = ogr.Open(param.getGdbPath(), 0);
            if (ds == null) return results;

            // 1. 构建 OGR SQL 语句
            // 语法: SELECT DISTINCT field1, field2 FROM layer_name WHERE ...
            String fieldsClause = "*";
            List<String> returnFields = param.getReturnFields();

            // 如果指定了字段，拼接字段名；否则默认 * (注意: DISTINCT * 性能可能较差，建议去重时必须指定字段)
            if (returnFields != null && !returnFields.isEmpty()) {
                fieldsClause = String.join(", ", returnFields);
            }

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT DISTINCT ").append(fieldsClause)
                    .append(" FROM ").append(layerName);

            if (param.getWhereClause() != null && !param.getWhereClause().isEmpty()) {
                sqlBuilder.append(" WHERE ").append(param.getWhereClause());
            }

            String sql = sqlBuilder.toString();
            log.debug("执行去重 SQL: {}", sql);

            // 2. 执行 SQL 查询
            // ExecuteSQL 返回的是一个临时的 Layer 对象
            sqlResultLayer = ds.ExecuteSQL(sql);

            if (sqlResultLayer != null) {
                // 3. 读取结果
                // 此时 sqlResultLayer 里的数据已经是去重后的，数量少了很多

                // 预构建索引优化
                FeatureDefn defn = sqlResultLayer.GetLayerDefn();
                Map<String, Integer> fieldIndexMap = new HashMap<>();
                int fieldCount = defn.GetFieldCount();
                for (int i = 0; i < fieldCount; i++) {
                    fieldIndexMap.put(defn.GetFieldDefn(i).GetName(), i);
                }

                sqlResultLayer.ResetReading();
                Feature feat;
                while ((feat = sqlResultLayer.GetNextFeature()) != null) {
                    results.add(convertFeatureOptimized(feat, fieldIndexMap));
                    feat.delete(); // 释放 Feature
                }
            }

        } catch (Exception e) {
            log.error("SQL 去重查询失败: " + layerName, e);
        } finally {
            // 4. 极其重要：释放 SQL 结果集图层
            if (ds != null && sqlResultLayer != null) {
                ds.ReleaseResultSet(sqlResultLayer);
            }
            if (ds != null) {
                ds.delete();
            }
        }
        return results;
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

            // [优化] 预构建索引
            Map<String, Integer> fieldIndexMap = buildFieldIndexMap(layer, param.getReturnFields());

            layer.ResetReading();
            Feature feat;
            while ((feat = layer.GetNextFeature()) != null) {
                results.add(convertFeatureOptimized(feat, fieldIndexMap));
                feat.delete(); // [优化] 释放
            }
        } catch (Exception e) {
            log.error("全量读取失败: " + layerName, e);
        } finally {
            if (ds != null) ds.delete();
        }
        return results;
    }

    // ========================================================================
    // 4. 核心转换与工具方法 (Core Utilities)
    // ========================================================================

    /**
     * 优化的导出处理逻辑 (支持批量事务)
     */
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
            if (featCount == 0) return;

            // 创建目标图层 (复制坐标系和几何类型)
            Layer targetLayer = targetDs.CreateLayer(targetLayerName, sourceLayer.GetSpatialRef(), sourceLayer.GetGeomType());
            if (targetLayer == null) {
                throw new RuntimeException("创建目标图层失败: " + targetLayerName);
            }

            // 复制字段结构
            FeatureDefn sourceDefn = sourceLayer.GetLayerDefn();
            for (int i = 0; i < sourceDefn.GetFieldCount(); i++) {
                targetLayer.CreateField(sourceDefn.GetFieldDefn(i));
            }

            log.debug("开始导出: {} ({} 条)", sourceLayerName, featCount);

            // [优化] 批量事务处理
            final int BATCH_SIZE = 20000;
            long currentBatch = 0;

            targetLayer.StartTransaction(); // 开启事务
            sourceLayer.ResetReading();
            Feature srcFeat;

            // 缓存 FeatureDefn 引用，避免循环中重复 JNI 调用
            FeatureDefn targetDefn = targetLayer.GetLayerDefn();

            while ((srcFeat = sourceLayer.GetNextFeature()) != null) {
                Feature targetFeat = new Feature(targetDefn);
                // [优化] SetFrom 是 C++ 层面的快速拷贝
                targetFeat.SetFrom(srcFeat);
                targetLayer.CreateFeature(targetFeat);

                // 显式释放 Native 对象
                targetFeat.delete();
                srcFeat.delete();

                currentBatch++;
                // 达到批次，提交并重启事务
                if (currentBatch % BATCH_SIZE == 0) {
                    targetLayer.CommitTransaction();
                    targetLayer.StartTransaction();
                }
            }

            // 提交剩余数据
            targetLayer.CommitTransaction();
            log.info("图层导出完毕: {}", targetLayerName);

        } catch (Exception e) {
            log.error("图层导出出错: " + sourceLayerName, e);
            throw new RuntimeException("Export layer failed: " + sourceLayerName, e);
        } finally {
            if (sourceDs != null) sourceDs.delete();
        }
    }

    /**
     * [优化] 构建字段名到索引的映射
     */
    private Map<String, Integer> buildFieldIndexMap(Layer layer, List<String> returnFields) {
        Map<String, Integer> map = new HashMap<>();
        FeatureDefn defn = layer.GetLayerDefn();

        if (returnFields != null && !returnFields.isEmpty()) {
            for (String fieldName : returnFields) {
                int idx = defn.GetFieldIndex(fieldName);
                if (idx != -1) {
                    map.put(fieldName, idx);
                }
            }
        } else {
            // 所有字段
            int fieldCount = defn.GetFieldCount();
            for (int i = 0; i < fieldCount; i++) {
                FieldDefn fieldDefn = defn.GetFieldDefn(i);
                map.put(fieldDefn.GetName(), i);
            }
        }
        return map;
    }

    /**
     * [优化] 基于索引快速读取属性
     */
    private Map<String, Object> convertFeatureOptimized(Feature feat, Map<String, Integer> fieldIndexMap) {
        Map<String, Object> attrs = new LinkedHashMap<>(fieldIndexMap.size());
        for (Map.Entry<String, Integer> entry : fieldIndexMap.entrySet()) {
            // GetFieldAsString(int) 比 (String) 快得多
            attrs.put(entry.getKey(), feat.GetFieldAsString(entry.getValue()));
        }
        return attrs;
    }

    // --- 下列辅助方法逻辑保持通用不变 ---

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
            throw new RuntimeException("文件不存在: " + path);
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