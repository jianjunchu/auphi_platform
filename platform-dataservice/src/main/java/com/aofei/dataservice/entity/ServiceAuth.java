package com.aofei.dataservice.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.aofei.base.entity.DataEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务接口授权
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_AUTH")
public class ServiceAuth extends DataEntity<ServiceAuth> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "AUTH_ID", type = IdType.ID_WORKER)
    private Long authId;
    /**
     * 组织 ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 授权用户
     */
    @TableField("USER_ID")
    private Long userId;

    @TableField(exist = false)
    private String username;

    /**
     * 授权服务接口
     */
    @TableField("SERVICE_ID")
    private Long serviceId;

    /**
     * 授权服务接口
     */
    @TableField(exist = false)
    private String serviceName;

    /**
     * 接口服务地址
     */
    @TableField(exist = false)
    private String serviceUrl;



    /**
     * 授权 IP
     */
    @TableField("AUTH_IP")
    private String authIp;
    /**
     * 业务用途
     */
    @TableField("USE_DESC")
    private String useDesc;
    /**
     * 使用部门
     */
    @TableField("USE_DEPT")
    private String useDept;
    /**
     * 使用人员
     */
    @TableField("USER_NAME")
    private String userName;



    @Override
    protected Serializable pkVal() {
        return this.authId;
    }

}
