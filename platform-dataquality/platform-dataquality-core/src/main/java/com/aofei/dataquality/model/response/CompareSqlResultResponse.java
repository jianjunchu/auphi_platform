package com.aofei.dataquality.model.response;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

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
public class CompareSqlResultResponse  {

    private static final long serialVersionUID = 1L;


    private Long compareSqlResultId;

    private Long compareSqlFieldId;

    private Long compareSqlId;

    private String fieldValue;

    private String refFieldValue;
    /**
     * 1 equals,   0  not equals
     */
    private Integer compareResult;


    /**
     * 资源库ID
     */
    private String repositoryName;
    /**
     * ID in r_databsae table
     */
    private Long databaseId;

    /**
     * ID in r_databsae table
     */
    private String databaseName;
    /**
     * 参考sql数据库ID
     */
    private Long refDatabaseId;

    /**
     * 参考sql数据库ID
     */
    private String refDatabaseName;
    /**
     * ID in profile_table_group
     */
    private Long ruleGroupId;

    /**
     * 分组名称
     */
    private String ruleGroupName;

    /**
     * profile_name
     */
    private String compareName;
    /**
     * compare desc
     */
    private String compareDesc;
    /**
     * 1 for one value compare, 2 for multi-value compare, default 1
     */
    private Integer compareType;

    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 比较的字段名称
     */
    private String refFieldName;
    /**
     * 字段说明
     */
    private String fieldDesc;

    private Date createTime;

    private Date compareTime;
}
