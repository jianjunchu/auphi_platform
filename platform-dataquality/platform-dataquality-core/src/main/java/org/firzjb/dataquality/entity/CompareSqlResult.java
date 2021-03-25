package org.firzjb.dataquality.entity;

import org.firzjb.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName("DATA_QUALITY_COMPARE_SQL_RESULT")
public class CompareSqlResult extends DataEntity<CompareSqlResult> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "COMPARE_SQL_RESULT_ID", type = IdType.ID_WORKER)
    private Long compareSqlResultId;

    @TableField(exist = false)
    private Long compareSqlId;

    @TableField("COMPARE_SQL_FIELD_ID")
    private Long compareSqlFieldId;

    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;


    @TableField("FIELD_VALUE")
    private String fieldValue;
    @TableField("REF_FIELD_VALUE")
    private String refFieldValue;
    /**
     * 1 equals,   0  not equals
     */
    @TableField("COMPARE_RESULT")
    private Integer compareResult;


    /**
     * 资源库ID
     */
    @TableField(exist = false)
    private String repositoryName;
    /**
     * ID in r_databsae table
     */
    @TableField(exist = false)
    private Long databaseId;

    /**
     * ID in r_databsae table
     */
    @TableField(exist = false)
    private String databaseName;
    /**
     * 参考sql数据库ID
     */
    @TableField(exist = false)
    private Long refDatabaseId;

    /**
     * 参考sql数据库ID
     */
    @TableField(exist = false)
    private String refDatabaseName;
    /**
     * ID in profile_table_group
     */
    @TableField(exist = false)
    private Long ruleGroupId;

    /**
     * 分组名称
     */
    @TableField(exist = false)
    private String ruleGroupName;

    /**
     * profile_name
     */
    @TableField(exist = false)
    private String compareName;
    /**
     * compare desc
     */
    @TableField(exist = false)
    private String compareDesc;
    /**
     * 1 for one value compare, 2 for multi-value compare, default 1
     */
    @TableField(exist = false)
    private Integer compareType;

    /**
     * 字段名称
     */
    @TableField(exist = false)
    private String fieldName;
    /**
     * 字段类型
     */
    @TableField(exist = false)
    private String fieldType;
    /**
     * 比较的字段名称
     */
    @TableField(exist = false)
    private String refFieldName;
    /**
     * 字段说明
     */
    @TableField(exist = false)
    private String fieldDesc;

    @TableField(exist = false)
    private Date compareTime;



    @Override
    protected Serializable pkVal() {
        return this.compareSqlResultId;
    }

}
