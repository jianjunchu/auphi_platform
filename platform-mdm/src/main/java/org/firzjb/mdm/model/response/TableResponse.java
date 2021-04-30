package org.firzjb.mdm.model.response;

import org.firzjb.base.model.response.BaseResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-11-07 12:11
 */
@Data
public class TableResponse extends BaseResponse {

    private Long tableId;
    /**
     * ID in model table
     */
    @ApiModelProperty(value = "模型ID")
    private Long modelId;

    @ApiModelProperty(value = "资源库")
    private String repositoryName;
    /**
     * ID in r_databsae table
     *
     */
    @ApiModelProperty(value = "数据库ID")
    private String databaseId;

    @ApiModelProperty(value = "模式名")
    private String schemaName;

    @ApiModelProperty(value = "表名")
    private String tableName;
}
