// package org.frank.app.service.gdb;
//
// import org.frank.shared.gdb.req.GdbInfoReq;
// import org.frank.shared.gdb.req.GdbLayerQueryReq;
// import org.frank.shared.gdb.resp.GdbInfoResp;
//
// import java.util.List;
// import java.util.Map;
//
// /**
//  * GDB文件读取服务接口
//  *
//  * @author Frank
//  * @since 2025-11-17
//  */
// public interface GdbService {
//
//     /**
//      * 获取GDB文件信息
//      *
//      * @param req GDB文件信息请求
//      * @return GDB文件信息响应
//      */
//     GdbInfoResp getGdbInfo(GdbInfoReq req);
//
//     /**
//      * 获取GDB文件中的所有图层列表
//      *
//      * @param gdbPath GDB文件路径
//      * @return 图层名称列表
//      */
//     List<String> getLayerList(String gdbPath);
//
//     /**
//      * 读取指定图层的要素数据
//      *
//      * @param req 图层查询请求
//      * @return 要素数据列表
//      */
//     List<Map<String, Object>> readLayerFeatures(GdbLayerQueryReq req);
//
//     /**
//      * 验证GDB文件是否有效
//      *
//      * @param gdbPath GDB文件路径
//      * @return 是否有效
//      */
//     boolean validateGdbFile(String gdbPath);
// }