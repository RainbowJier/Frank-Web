package org.frank.shared.sysLogOper.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLogOperResp implements Serializable {

    @ApiModelProperty(value = "日志主键")
    private Long id;

    @ApiModelProperty(value = "模块标题")
    private String title;

    @ApiModelProperty(value = "业务类型")
    private Integer businessType;

    @ApiModelProperty(value = "业务类型描述")
    private String businessTypeDesc;

    @ApiModelProperty(value = "方法名称")
    private String method;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作类别")
    private Integer operatorType;

    @ApiModelProperty(value = "操作类别描述")
    private String operatorTypeDesc;

    @ApiModelProperty(value = "操作人员")
    private String operName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "请求URL")
    private String operUrl;

    @ApiModelProperty(value = "主机地址")
    private String operIp;

    @ApiModelProperty(value = "操作地点")
    private String operLocation;

    @ApiModelProperty(value = "请求参数")
    private String operParam;

    @ApiModelProperty(value = "返回参数")
    private String JsonResult;

    @ApiModelProperty(value = "操作状态")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "错误消息")
    private String errorMsg;

    @ApiModelProperty(value = "消耗时间")
    private Long costTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}