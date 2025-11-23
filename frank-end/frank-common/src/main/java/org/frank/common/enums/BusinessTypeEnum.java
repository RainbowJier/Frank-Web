// /**
//  * 业务类型枚举
//  *
//  * @author Frank
//  * @since 2025-11-16
//  */
// @Getter
// @AllArgsConstructor
// @ApiModel(description = "业务类型枚举")
// public enum BusinessTypeEnum {
//
//     /**
//      * 其它
//      */
//     OTHER(0, "其它"),
//
//     /**
//      * 新增
//      */
//     INSERT(1, "新增"),
//
//     /**
//      * 修改
//      */
//     UPDATE(2, "修改"),
//
//     /**
//      * 删除
//      */
//     DELETE(3, "删除"),
//
//     /**
//      * 授权
//      */
//     GRANT(4, "授权"),
//
//     /**
//      * 导出
//      */
//     EXPORT(5, "导出"),
//
//     /**
//      * 导入
//      */
//     IMPORT(6, "导入"),
//
//     /**
//      * 强退
//      */
//     FORCE(7, "强退"),
//
//     /**
//      * 生成代码
//      */
//     GENCODE(8, "生成代码"),
//
//     /**
//      * 清空数据
//      */
//     CLEAN(9, "清空数据");
//
//     private final Integer code;
//     private final String description;
//
//     /**
//      * 根据编码获取枚举
//      *
//      * @param code 编码
//      * @return 枚举
//      */
//     public static BusinessTypeEnum getByCode(Integer code) {
//         if (code == null) {
//             return null;
//         }
//         for (BusinessTypeEnum type : values()) {
//             if (type.getCode().equals(code)) {
//                 return type;
//             }
//         }
//         return null;
//     }
//
//     /**
//      * 根据编码获取描述
//      *
//      * @param code 编码
//      * @return 描述
//      */
//     public static String getDescriptionByCode(Integer code) {
//         BusinessTypeEnum type = getByCode(code);
//         return type != null ? type.getDescription() : null;
//     }
// }