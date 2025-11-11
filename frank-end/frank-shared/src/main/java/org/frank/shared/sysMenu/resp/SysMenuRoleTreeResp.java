package org.frank.shared.sysMenu.resp;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuRoleTreeResp {
    @ApiModelProperty("Menu tree")
    private List<Tree<Long>> tree;

    @ApiModelProperty("Checked keys.")
    private List<Long> checkedKeys;
}
