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
@TableName("DATA_QUALITY_COMPARE_SQL")
public class CompareSql extends DataEntity<CompareSql> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "COMPARE_SQL_ID", type = IdType.ID_WORKER)
    private Long compareSqlId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;

    /**
     * 资源库ID
     */
    @TableField("REPOSITORY_NAME")
    private String repositoryName;
    /**
     * ID in r_databsae table
     */
    @TableField("DATABASE_ID")
    private Long databaseId;

    /**
     * ID in r_databsae table
     */
    @TableField(exist = false)
    private String databaseName;
    /**
     * 参考sql数据库ID
     */
    @TableField("REF_DATABASE_ID")
    private Long refDatabaseId;

    /**
     * 参考sql数据库ID
     */
    @TableField(exist = false)
    private String refDatabaseName;
    /**
     * ID in profile_table_group
     */
    @TableField("RULE_GROUP_ID")
    private Long ruleGroupId;

    /**
     * 分组名称
     */
    @TableField(exist = false)
    private String ruleGroupName;

    /**
     * profile_name
     */
    @TableField("COMPARE_NAME")
    private String compareName;
    /**
     * compare desc
     */
    @TableField("COMPARE_DESC")
    private String compareDesc;
    /**
     * 1 for one value compare, 2 for multi-value compare, default 1
     */
    @TableField("COMPARE_TYPE")
    private Integer compareType;
    @TableField("SQL")
    private String sql;
    @TableField("REF_SQL")
    private String refSql;



    @Override
    protected Serializable pkVal() {
        return this.compareSqlId;
    }

}
