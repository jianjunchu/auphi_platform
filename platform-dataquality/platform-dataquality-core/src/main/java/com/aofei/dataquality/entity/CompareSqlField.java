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
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_COMPARE_SQL_FIELD")
public class CompareSqlField extends DataEntity<CompareSqlField> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "COMPARE_SQL_FIELD_ID", type = IdType.ID_WORKER)
    private Long compareSqlFieldId;
    /**
     * 稽核ID
     */
    @TableField("COMPARE_SQL_ID")
    private Long compareSqlId;
    /**
     * 字段名称
     */
    @TableField("FIELD_NAME")
    private String fieldName;
    /**
     * 字段类型
     */
    @TableField("FIELD_TYPE")
    private String fieldType;
    /**
     * 比较的字段名称
     */
    @TableField("REF_FIELD_NAME")
    private String refFieldName;
    /**
     * 字段说明
     */
    @TableField("FIELD_DESC")
    private String fieldDesc;
    /**
     * 0 等值比较，1 范围比较
     */
    @TableField("FIELD_COMPARE_TYPE")
    private Integer fieldCompareType;

    @TableField("MIN_RATIO")
    private Double minRatio;

    @TableField("MAX_RATIO")
    private Double maxRatio;


    @Override
    protected Serializable pkVal() {
        return this.compareSqlFieldId;
    }

}
