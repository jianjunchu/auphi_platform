package com.aofei.dataservice.entity;

import com.aofei.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_RECEIVE_INTERFACE")
public class ReceiveInterface extends DataEntity<ReceiveInterface> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "SERVICE_ID", type = IdType.ID_WORKER)
    private Long serviceId;

    @TableField("SERVICE_NAME")
    private String serviceName;

    /**
     * Client调用时唯一识别的标示
     */
    @TableField("SERVICE_IDENTIFY")
    private String serviceIdentify;

    @TableField("SERVICE_URL")
    private String serviceUrl;

    @TableField("DATASOURCE")
    private String datasource;

    @TableField("TABLENAME")
    private String tablename;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("INTERFACE_DESC")
    private String interfaceDesc;

    @TableField("ID_DATABASE")
    private String databaseId;

    @TableField(exist = false)
    private String databaseName;

    @Override
    protected Serializable pkVal() {
        return this.serviceId;
    }

}
