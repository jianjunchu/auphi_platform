package org.firzjb.mdm.model.response;

import org.firzjb.base.model.response.BaseResponse;
import lombok.Data;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-11-08 22:45
 */
@Data
public class DataCleanResponse extends BaseResponse {

    private Long id;

    private Long modelId;

    private String modelName;

    private String attributeModel;

    private String repositoryName;

    private String databaseId;

    private String schemaName;

    private String tableName;

    private String primaryKey;

    private String columnName;

    private String whereCondition;

    private Integer mapingMode;

    private Integer mapingIdDatabase;

    private String mapingSchemaName;

    private String mapingTableName;
}
