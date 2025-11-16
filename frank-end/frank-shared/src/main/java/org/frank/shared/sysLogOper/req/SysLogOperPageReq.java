package org.frank.shared.sysLogOper.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frank.common.core.page.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "System operation log pagination query request")
public class SysLogOperPageReq extends BasePage {
    @ApiModelProperty(value = "Operation IP address")
    private String operIp;

    @ApiModelProperty(value = "System module name")
    private String title;

    @ApiModelProperty(value = "Operator name")
    private String operName;

    @ApiModelProperty(value = "Business type (0-other 1-insert 2-update 3-delete)")
    private Integer businessType;

    @ApiModelProperty(value = "Operation status (1-normal 0-abnormal)")
    private String status;

    @ApiModelProperty(value = "Start time (yyyy-MM-dd HH:mm:ss)")
    private String beginTime;

    @ApiModelProperty(value = "End time (yyyy-MM-dd HH:mm:ss)")
    private String endTime;
}