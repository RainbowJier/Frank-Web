// package org.frank.client.serviceImpl.gdb;
//
// import lombok.extern.slf4j.Slf4j;
// import org.frank.app.service.gdb.GdbService;
// import org.frank.common.util.GdbUtil;
// import org.frank.shared.gdb.req.GdbInfoReq;
// import org.frank.shared.gdb.req.GdbLayerQueryReq;
// import org.frank.shared.gdb.resp.GdbInfoResp;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
//
// /**
//  * GDB文件读取服务实现类
//  *
//  * @author Frank
//  * @since 2025-11-17
//  */
// @Slf4j
// @Service
// public class GdbServiceImpl implements GdbService {
//
//     @Override
//     public GdbInfoResp getGdbInfo(GdbInfoReq req) {
//         log.info("开始获取GDB文件信息: {}", req.getGdbPath());
//
//         try {
//             // 使用GdbUtil获取GDB信息
//             Map<String, Object> gdbInfo = GdbUtil.getGdbInfo(req.getGdbPath());
//
//             // 转换为响应对象
//             return convertToGdbInfoResp(gdbInfo);
//
//         } catch (Exception e) {
//             log.error("获取GDB文件信息失败: {}", req.getGdbPath(), e);
//             return GdbInfoResp.builder()
//                     .path(req.getGdbPath())
//                     .error("获取GDB文件信息失败: " + e.getMessage())
//                     .build();
//         }
//     }
//
//     @Override
//     public List<String> getLayerList(String gdbPath) {
//         log.info("开始获取GDB文件图层列表: {}", gdbPath);
//
//         try {
//             List<Map<String, Object>> layersInfo = GdbUtil.getLayersInfo(gdbPath);
//             return layersInfo.stream()
//                     .map(layer -> (String) layer.get("name"))
//                     .collect(Collectors.toList());
//
//         } catch (Exception e) {
//             log.error("获取GDB文件图层列表失败: {}", gdbPath, e);
//             return List.of();
//         }
//     }
//
//     @Override
//     public List<Map<String, Object>> readLayerFeatures(GdbLayerQueryReq req) {
//         log.info("开始读取GDB图层要素: gdbPath={}, layerName={}, limit={}",
//                 req.getGdbPath(), req.getLayerName(), req.getLimit());
//
//         try {
//             return GdbUtil.readLayerFeatures(req.getGdbPath(), req.getLayerName(), req.getLimit());
//
//         } catch (Exception e) {
//             log.error("读取GDB图层要素失败: gdbPath={}, layerName={}",
//                     req.getGdbPath(), req.getLayerName(), e);
//             return List.of();
//         }
//     }
//
//     @Override
//     public boolean validateGdbFile(String gdbPath) {
//         log.debug("验证GDB文件有效性: {}", gdbPath);
//         return GdbUtil.isValidGdbFile(gdbPath);
//     }
//
//     /**
//      * 转换GDB信息为响应对象
//      *
//      * @param gdbInfo GDB信息Map
//      * @return GDB信息响应对象
//      */
//     private GdbInfoResp convertToGdbInfoResp(Map<String, Object> gdbInfo) {
//         GdbInfoResp.GdbInfoRespBuilder builder = GdbInfoResp.builder();
//
//         // 设置基本信息
//         builder.path((String) gdbInfo.get("path"))
//                .name((String) gdbInfo.get("name"))
//                .size(((Number) gdbInfo.getOrDefault("size", 0L)).longValue())
//                .lastModified(((Number) gdbInfo.getOrDefault("lastModified", 0L)).longValue())
//                .driver((String) gdbInfo.get("driver"))
//                .layerCount(((Number) gdbInfo.getOrDefault("layerCount", 0)).intValue())
//                .totalFeatures(((Number) gdbInfo.getOrDefault("totalFeatures", 0L)).longValue());
//
//         // 设置错误信息（如果有）
//         if (gdbInfo.containsKey("error")) {
//             builder.error((String) gdbInfo.get("error"));
//         }
//
//         // 转换图层信息
//         @SuppressWarnings("unchecked")
//         List<Map<String, Object>> layers = (List<Map<String, Object>>) gdbInfo.get("layers");
//         if (layers != null) {
//             List<GdbInfoResp.LayerInfo> layerInfoList = layers.stream()
//                     .map(this::convertToLayerInfo)
//                     .collect(Collectors.toList());
//             builder.layers(layerInfoList);
//         }
//
//         return builder.build();
//     }
//
//     /**
//      * 转换图层信息
//      *
//      * @param layerMap 图层信息Map
//      * @return 图层信息响应对象
//      */
//     private GdbInfoResp.LayerInfo convertToLayerInfo(Map<String, Object> layerMap) {
//         GdbInfoResp.LayerInfo.LayerInfoBuilder layerBuilder = GdbInfoResp.LayerInfo.builder();
//
//         layerBuilder.name((String) layerMap.get("name"))
//                    .featureCount(((Number) layerMap.getOrDefault("featureCount", 0)).intValue())
//                    .geomType((String) layerMap.get("geomType"))
//                    .srs((String) layerMap.get("srs"));
//
//         // 转换字段信息
//         @SuppressWarnings("unchecked")
//         List<Map<String, String>> fields = (List<Map<String, String>>) layerMap.get("fields");
//         if (fields != null) {
//             List<GdbInfoResp.ColumnInfo> columnInfoList = fields.stream()
//                     .map(this::convertToColumnInfo)
//                     .collect(Collectors.toList());
//             layerBuilder.fields(columnInfoList);
//         }
//
//         return layerBuilder.build();
//     }
//
//     /**
//      * 转换字段信息
//      *
//      * @param fieldMap 字段信息Map
//      * @return 字段信息响应对象
//      */
//     private GdbInfoResp.ColumnInfo convertToColumnInfo(Map<String, String> fieldMap) {
//         return GdbInfoResp.ColumnInfo.builder()
//                 .name(fieldMap.get("name"))
//                 .type(fieldMap.get("type"))
//                 .width(fieldMap.get("width"))
//                 .precision(fieldMap.get("precision"))
//                 .build();
//     }
// }