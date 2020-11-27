package com.aofei.dataquality.model.request;

import com.aofei.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class CompareSqlRequest extends BaseRequest<CompareSqlRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;

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
     * 参考sql数据库ID
     */
    private Long refDatabaseId;
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

    private List<CompareSqlFieldRequest> fields;

}
