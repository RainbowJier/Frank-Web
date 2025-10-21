package org.frank.shared.sysMenu.req;


import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.frank.shared.BaseReq;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuListReq extends BaseReq {

    @ApiModelProperty(value = "menu name")
    private String name;

    @ApiModelProperty(value = "menu status")
    private String de;
}
