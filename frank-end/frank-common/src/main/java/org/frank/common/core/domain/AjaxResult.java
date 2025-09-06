package org.frank.common.core.domain;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frank.common.enums.ResultCodeEnum;

/**
 * 统一响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjaxResult<T> {

    @ApiModelProperty("返回码")
    private long code;

    @ApiModelProperty("消息")
    private String message;

    @ApiModelProperty("返回对象")
    private T data;

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static <T> AjaxResult<T> success(T data, String message) {
        return new AjaxResult<T>(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> AjaxResult<T> failed(String message) {
        return new AjaxResult<T>(ResultCodeEnum.FAILED.getCode(), message, null);
    }

    public boolean isSuccess() {
        return NumberUtil.equals(ResultCodeEnum.SUCCESS.getCode(), this.getCode());
    }

    public static <T> AjaxResult<T> failed() {
        return new AjaxResult<T>(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMessage(), null);
    }

    public static <T> AjaxResult<T> validateFailed() {
        return new AjaxResult<T>(ResultCodeEnum.VALIDATE_FAILED.getCode(), ResultCodeEnum.VALIDATE_FAILED.getMessage(), null);
    }

    public static <T> AjaxResult<T> validateFailed(String message) {
        return new AjaxResult<T>(ResultCodeEnum.VALIDATE_FAILED.getCode(), message, null);
    }

    public static <T> AjaxResult<T> unauthorized() {
        return new AjaxResult<T>(ResultCodeEnum.UNAUTHORIZED.getCode(), ResultCodeEnum.UNAUTHORIZED.getMessage(), null);
    }

    public static <T> AjaxResult<T> unauthorized(T data) {
        return new AjaxResult<T>(ResultCodeEnum.UNAUTHORIZED.getCode(), ResultCodeEnum.UNAUTHORIZED.getMessage(), data);
    }

    public static <T> AjaxResult<T> unauthorized(String message) {
        return new AjaxResult<T>(ResultCodeEnum.UNAUTHORIZED.getCode(), message, null);
    }

    public static <T> AjaxResult<T> unauthorized(T data, String message) {
        return new AjaxResult<T>(ResultCodeEnum.UNAUTHORIZED.getCode(), message, data);
    }

    public static <T> AjaxResult<T> forbidden(T data) {
        return new AjaxResult<T>(ResultCodeEnum.FORBIDDEN.getCode(), ResultCodeEnum.FORBIDDEN.getMessage(), data);
    }

    public static <T> AjaxResult<T> badRequest(T data) {
        return badRequest(ResultCodeEnum.BAD_REQUEST.getMessage(), data);
    }

    public static <T> AjaxResult<T> badRequest(String message, T data) {
        return new AjaxResult<T>(ResultCodeEnum.BAD_REQUEST.getCode(), message, data);
    }

    public static <T> AjaxResult<T> notFound(T data) {
        return new AjaxResult<T>(ResultCodeEnum.NOT_FOUND.getCode(), ResultCodeEnum.NOT_FOUND.getMessage(), data);
    }

    public static <T> AjaxResult<T> buildCodeAndMessage(long code, String message) {
        return new AjaxResult<T>(code, message, null);
    }

    public static <T> AjaxResult<T> buildResult(ResultCodeEnum resultCode) {
        return new AjaxResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> T callRemoteResultHandle(AjaxResult<T> ajaxResult) {
        if (ResultCodeEnum.SUCCESS.getCode() != ajaxResult.getCode()) {
            throw new IllegalArgumentException(ajaxResult.getMessage());
        } else {
            return ajaxResult.getData();
        }
    }
}