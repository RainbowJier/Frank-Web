package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典类型表
 *
 * @TableName sys_dict_type
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_type")
@Data
@Accessors(chain = true)
public class SysDictType extends BaseEntity {
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    @TableField(value = "dict_name")
    private String dictName;

    @TableField(value = "dict_type")
    private String dictType;

    @TableField(value = "status")
    private Integer status;
}