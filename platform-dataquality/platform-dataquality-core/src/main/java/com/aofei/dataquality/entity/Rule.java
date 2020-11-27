package com.aofei.dataquality.entity;

import com.aofei.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据质量管理-规则管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_RULE")
public class Rule extends DataEntity<Rule> {

    private static final long serialVersionUID = 1L;

    /**
     * 规则id
     */
    @TableId(value = "RULE_ID", type = IdType.ID_WORKER)
    private Long ruleId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 分组id
     */
    @TableField("RULE_GROUP_ID")
    private Long ruleGroupId;

    /**
     * 分组
     */
    @TableField(exist = false)
    private String ruleGroupName;

    /**
     * 规则描述
     */
    @TableField("DESCRIPTION")
    private String description;
    /**
     * 规则类型
     */
    @TableField("RULE_TYPE")
    private Integer ruleType;
    /**
     * 数据库ID
     */
    @TableField("DATABASE_ID")
    private Long databaseId;

    /**
     * 数据库
     */
    @TableField(exist = false)
    private String databaseName;
    /**
     * 模式名
     */
    @TableField("SCHEMA_NAME")
    private String schemaName;
    /**
     * 主题表名
     */
    @TableField("TABLE_NAME")
    private String tableName;
    /**
     * 字段名
     */
    @TableField("FIELD_NAME")
    private String fieldName;

    /**
     * 字段类型
     */
    @TableField("FIELD_TYPE")
    private Integer fieldType;

    @TableField("FIELD_ORIGINAL_TYPE")
    private String fieldOriginalType;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLE")
    private Integer isEnable;



    @Override
    protected Serializable pkVal() {
        return this.ruleId;
    }

}
