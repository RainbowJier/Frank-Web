package org.frank.shared;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
}
