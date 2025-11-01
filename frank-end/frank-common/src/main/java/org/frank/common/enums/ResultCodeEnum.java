package org.frank.common.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(417, "参数检验失败"),
    UNAUTHORIZED(500, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    BAD_REQUEST(400, "客户端错误"),
    NOT_FOUND(404, "无法找到所请求的资源");

    private final int code;
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultCodeEnum of(String name) {
        return Arrays.stream(values()).filter((s) -> s.equalsTo(name)).findFirst().orElse(FAILED);
    }

    public static ResultCodeEnum of(Integer code) {
        return Arrays.stream(values()).filter((s) -> s.getCode() == (long) code).findFirst().orElse(FAILED);
    }

    private boolean equalsTo(String name) {
        return this.name().equalsIgnoreCase(name);
    }

}
