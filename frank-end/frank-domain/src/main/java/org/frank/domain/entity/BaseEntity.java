package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {
    /**
     * delete flag, 1-normal 0-deleted.
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "remark")
    private String remark;
}
