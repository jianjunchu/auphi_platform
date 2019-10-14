package com.auphi.ktrl.metadata.domain;

import com.auphi.ktrl.quality.base.BaseEntity;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Types;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tony
 * @since 2019-08-19
 */
public class MetadataMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 映射分组ID
     */
    private Long mappingGroupId;

    /**
     * 映射分组
     */
    private String mappingGroupName;

    /**
     * 源数据库ID
     */
    private Long sourceDbId;

    /**
     * 源数据库
     */
    private String sourceDbName;
    /**
     * 源模式名
     */
    private String sourceSchemaName;
    /**
     * 源表名
     */
    private String sourceTableName;
    /**
     * 源列名
     */
    private String sourceColumnName;
    /**
     * 源列数据类型
     */
    private String sourceColumnType;
    /**
     * 源列在Kettle 中的数据类型 (String,Date,BigNumber,Integer)
     */
    private String sourceColumnTypeEtl;
    /**
     * 长度或精度
     */
    private String sourceColumnLength;
    /**
     * 刻度
     */
    private String sourceColumnScale;
    /**
     * 源列注释
     */
    private String sourceColumnComments;
    /**
     * 源列顺序
     */
    private Long sourceColumnOrder;
    /**
     * 是否主键 ， 0不是，1是     */
    private Integer isPk;

    /**
     * 目的数据库ID
     */
    private Long destDbId;

    /**
     * 目的数据库
     */
    private String destDbName;
    /**
     * 目的模式名
     */
    private String destSchemaName;
    /**
     * 目的表名
     */
    private String destTableName;
    /**
     * 目的列名
     */
    private String destColumnName;
    /**
     * 目的列类型
     */
    private String destColumnType;
    /**
     * 目的列Kettle 数据类型
     */
    private String destColumnTypeEtl;
    /**
     * 目的列长度或精度
     */
    private String destColumnLength;
    /**
     * 目的列刻度
     */
    private String destColumnScale;
    /**
     * 抽取方式，0全量，1增量 ， 默认0
     */
    private Integer extractStyle;

    private Integer isIncrementalColumn;
    /**
     * 抽取状态，0未抽取，1正在抽取  默认0
     */
    private Integer extractStatus;
    private Date lastExtractStartTime;
    private Date lastExtractEndTime;
    private Date createTime;
    private Integer createUser;
    private Date updateTime;
    private Integer updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMappingGroupId() {
        return mappingGroupId;
    }

    public void setMappingGroupId(Long mappingGroupId) {
        this.mappingGroupId = mappingGroupId;
    }

    public Long getSourceDbId() {
        return sourceDbId;
    }

    public void setSourceDbId(Long sourceDbId) {
        this.sourceDbId = sourceDbId;
    }

    public String getSourceSchemaName() {
        return sourceSchemaName;
    }

    public void setSourceSchemaName(String sourceSchemaName) {
        this.sourceSchemaName = sourceSchemaName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getSourceColumnName() {
        return sourceColumnName;
    }

    public void setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public String getSourceColumnType() {
        return sourceColumnType;
    }

    public void setSourceColumnType(String sourceColumnType) {
        this.sourceColumnType = sourceColumnType;
    }

    public String getSourceColumnTypeEtl() {
        return sourceColumnTypeEtl;
    }

    public void setSourceColumnTypeEtl(String sourceColumnTypeEtl) {
        this.sourceColumnTypeEtl = sourceColumnTypeEtl;
    }

    public String getSourceColumnLength() {
        return sourceColumnLength;
    }

    public void setSourceColumnLength(String sourceColumnLength) {
        this.sourceColumnLength = sourceColumnLength;
    }

    public String getSourceColumnScale() {
        return sourceColumnScale;
    }

    public void setSourceColumnScale(String sourceColumnScale) {
        this.sourceColumnScale = sourceColumnScale;
    }

    public String getSourceColumnComments() {
        return sourceColumnComments;
    }

    public void setSourceColumnComments(String sourceColumnComments) {
        this.sourceColumnComments = sourceColumnComments;
    }

    public Long getSourceColumnOrder() {
        return sourceColumnOrder;
    }

    public void setSourceColumnOrder(Long sourceColumnOrder) {
        this.sourceColumnOrder = sourceColumnOrder;
    }

    public Integer getIsPk() {
        return isPk;
    }

    public void setIsPk(Integer isPk) {
        this.isPk = isPk;
    }

    public Long getDestDbId() {
        return destDbId;
    }

    public void setDestDbId(Long destDbId) {
        this.destDbId = destDbId;
    }

    public String getDestSchemaName() {
        return destSchemaName;
    }

    public void setDestSchemaName(String destSchemaName) {
        this.destSchemaName = destSchemaName;
    }

    public String getDestTableName() {
        return destTableName;
    }

    public void setDestTableName(String destTableName) {
        this.destTableName = destTableName;
    }

    public String getDestColumnName() {
        return destColumnName;
    }

    public void setDestColumnName(String destColumnName) {
        this.destColumnName = destColumnName;
    }

    public String getDestColumnType() {
        return destColumnType;
    }

    public void setDestColumnType(String destColumnType) {
        this.destColumnType = destColumnType;
    }

    public String getDestColumnTypeEtl() {
        return destColumnTypeEtl;
    }

    public void setDestColumnTypeEtl(String destColumnTypeEtl) {
        this.destColumnTypeEtl = destColumnTypeEtl;
    }

    public String getDestColumnLength() {
        return destColumnLength;
    }

    public void setDestColumnLength(String destColumnLength) {
        this.destColumnLength = destColumnLength;
    }

    public String getDestColumnScale() {
        return destColumnScale;
    }

    public void setDestColumnScale(String destColumnScale) {
        this.destColumnScale = destColumnScale;
    }

    public Integer getExtractStyle() {
        return extractStyle;
    }

    public void setExtractStyle(Integer extractStyle) {
        this.extractStyle = extractStyle;
    }

    public Integer getExtractStatus() {
        return extractStatus;
    }

    public void setExtractStatus(Integer extractStatus) {
        this.extractStatus = extractStatus;
    }

    public Date getLastExtractStartTime() {
        return lastExtractStartTime;
    }

    public void setLastExtractStartTime(Date lastExtractStartTime) {
        this.lastExtractStartTime = lastExtractStartTime;
    }

    public Date getLastExtractEndTime() {
        return lastExtractEndTime;
    }

    public void setLastExtractEndTime(Date lastExtractEndTime) {
        this.lastExtractEndTime = lastExtractEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public String getSourceDbName() {
        return sourceDbName;
    }

    public void setSourceDbName(String sourceDbName) {
        this.sourceDbName = sourceDbName;
    }

    public String getDestDbName() {
        return destDbName;
    }

    public void setDestDbName(String destDbName) {
        this.destDbName = destDbName;
    }

    public String getMappingGroupName() {
        return mappingGroupName;
    }

    public void setMappingGroupName(String mappingGroupName) {
        this.mappingGroupName = mappingGroupName;
    }

    public Integer getIsIncrementalColumn() {
        return isIncrementalColumn;
    }

    public void setIsIncrementalColumn(Integer isIncrementalColumn) {
        this.isIncrementalColumn = isIncrementalColumn;
    }
}
