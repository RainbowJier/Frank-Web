package org.frank.shared.sysDictData.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.page.BasePage;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysDictDataListReq extends BasePage {
    @ApiModelProperty(value = "Dict Type")
    private String dictType;

    @ApiModelProperty(value = "Dict Label")
    private String dictLabel;

    @ApiModelProperty(value = "Status")
    private Integer status;
}
