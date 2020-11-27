package com.aofei.dataquality.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
public class CompareSqlResponse  {

    private static final long serialVersionUID = 1L;

    private Long compareSqlId;
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

    private String sql;

    private String refSql;

    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 修改用户
     */
    private String updateUser;
    /**
     * 是否删除  1：已删除  0：正常
     */
    private Integer delFlag;

    private List<CompareSqlFieldResponse> fields;

}
