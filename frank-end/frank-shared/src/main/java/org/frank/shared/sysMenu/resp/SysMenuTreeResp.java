package org.frank.shared.sysMenu.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuTreeResp {
    @ApiModelProperty(value = "Menu ID")
    private Long menuId;

    @ApiModelProperty(value = "Menu name")
    private String menuName;

    @ApiModelProperty(value = "Parent menu ID")
    private Long parentId;

    @ApiModelProperty(value = "Display order")
    private Integer orderNum;

    @ApiModelProperty(value = "Child menus")
    private List<SysMenuTreeResp> children = new ArrayList<>();
}
