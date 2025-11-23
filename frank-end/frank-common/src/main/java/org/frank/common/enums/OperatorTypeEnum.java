// /**
//  * 操作类别枚举
//  *
//  * @author Frank
//  * @since 2025-11-16
//  */
// @Getter
// @AllArgsConstructor
// @ApiModel(description = "操作类别枚举")
// public enum OperatorTypeEnum {
//
//     /**
//      * 其它
//      */
//     OTHER(0, "其它"),
//
//     /**
//      * 后台用户
//      */
//     BACKEND(1, "后台用户"),
//
//     /**
//      * 手机端用户
//      */
//     MOBILE(2, "手机端用户");
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
//     public static OperatorTypeEnum getByCode(Integer code) {
//         if (code == null) {
//             return null;
//         }
//         for (OperatorTypeEnum type : values()) {
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
//         OperatorTypeEnum type = getByCode(code);
//         return type != null ? type.getDescription() : null;
//     }
// }