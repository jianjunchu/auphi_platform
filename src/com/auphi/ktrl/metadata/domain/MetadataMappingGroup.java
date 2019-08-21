package com.auphi.ktrl.metadata.domain;

import java.util.Date;

import com.auphi.ktrl.quality.base.BaseEntity;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tony
 * @since 2019-08-19
 */
public class MetadataMappingGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 映射组名称
     */
    private String mappingGroupName;
    /**
     * 映射组描述
     */
    private String mappingGroupDesc;
    private Date createTime;

    private Integer createUser;

    private Date updateTime;

    private Integer updateUser;

    public MetadataMappingGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMappingGroupName() {
        return mappingGroupName;
    }

    public void setMappingGroupName(String mappingGroupName) {
        this.mappingGroupName = mappingGroupName;
    }

    public String getMappingGroupDesc() {
        return mappingGroupDesc;
    }

    public void setMappingGroupDesc(String mappingGroupDesc) {
        this.mappingGroupDesc = mappingGroupDesc;
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
