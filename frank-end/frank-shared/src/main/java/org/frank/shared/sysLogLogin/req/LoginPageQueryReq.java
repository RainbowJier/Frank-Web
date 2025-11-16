package org.frank.shared.sysLogLogin.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.frank.common.core.page.BasePage;

import java.util.Date;

@Data
@ApiModel(description = "登录日志分页查询请求")
public class LoginPageQueryReq extends BasePage {
    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "登录状态（1成功 0失败）")
    private String status;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}