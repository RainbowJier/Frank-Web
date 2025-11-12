package org.frank.shared.sysDictData.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dictionary data response object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictDataResp implements Serializable {
    @ApiModelProperty("Dictionary code")
    private String dictCode;

    @ApiModelProperty("Dictionary sort order")
    private Integer dictSort;

    @ApiModelProperty("Dictionary label")
    private String dictLabel;

    @ApiModelProperty("Dictionary value")
    private String dictValue;

    @ApiModelProperty("Dictionary type")
    private String dictType;

    @ApiModelProperty("CSS class (other style extensions)")
    private String cssClass;

    @ApiModelProperty("Table display style")
    private String listClass;

    @ApiModelProperty("Is default (Y yes N no)")
    private String isDefault;

    @ApiModelProperty("Status (1 normal, 0 disabled)")
    private Integer status;

    @ApiModelProperty("Creator")
    private String createBy;

    @ApiModelProperty("Create time")
    private Date createTime;

    @ApiModelProperty("Updater")
    private String updateBy;

    @ApiModelProperty("Update time")
    private Date updateTime;

    @ApiModelProperty("Remark")
    private String remark;
}