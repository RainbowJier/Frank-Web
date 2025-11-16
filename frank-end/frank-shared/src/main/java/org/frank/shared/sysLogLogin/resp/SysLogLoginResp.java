package org.frank.shared.sysLogLogin.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录日志响应")
public class SysLogLoginResp implements Serializable {
    @ApiModelProperty(value = "日志ID")
    private String id;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "登录地点")
    private String loginLocation;

    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "登录状态（1成功 0失败）")
    private String status;

    @ApiModelProperty(value = "提示消息")
    private String msg;

    @ApiModelProperty(value = "访问时间")
    private Date loginTime;
}