package org.frank.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型表
 *
 * @TableName sys_dict_type
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_type")
@Data
public class SysDictType extends BaseEntity {
    /**
     * 字典主键
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    private String dictName;

    /**
     * 字典类型
     */
    @TableField(value = "dict_type")
    private String dictType;

    /**
     * 状态（1正常，-1停用）
     */
    @TableField(value = "status")
    private Integer status;
}