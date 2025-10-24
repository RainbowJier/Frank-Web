package org.frank.shared.sysMenu.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.common.core.domain.BaseReq;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuListReq extends BaseReq {
    @ApiModelProperty(value = "menu name")
    private String menuName;

    @ApiModelProperty(value = "menu status")
    private Integer status;
}
