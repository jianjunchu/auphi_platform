package com.auphi.ktrl.metadata.domain;

import com.auphi.ktrl.quality.base.BaseEntity;
import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author Tony
 * @since 2019-08-19
 */
public class MetadataVersion extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 映射分组ID
     */
    private Long mappingGroupId;
    /**
     * 源数据库ID
     */
    private Long sourceDbId;
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
     * 是否主键 ， 0不是，1是主键
     */
    private String isPk;
    /**
     * 上次刷新时间
     */
    private Date refreshTime;
    /**
     * 上次刷新时间long 型版本号
     */
    private String refreshTimeVersion;

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

    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getRefreshTimeVersion() {
        return refreshTimeVersion;
    }

    public void setRefreshTimeVersion(String refreshTimeVersion) {
        this.refreshTimeVersion = refreshTimeVersion;
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
}
