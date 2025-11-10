package org.frank.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseReq {
    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "role id list")
    private List<Long> roleIds;

    @ApiModelProperty(value = "is admin")
    private Boolean isAdmin;
}
