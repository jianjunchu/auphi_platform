package org.firzjb.dataservice.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import org.firzjb.base.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据接口管理
 * </p>
 *
 * @author Tony
 * @since 2019-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_INTERFACE")
public class ServiceInterface extends DataEntity<ServiceInterface> {

    private static final long serialVersionUID = 1L;

    public static final Integer INTERFACE_TYP_PUBLISH = 1;
    public static final Integer INTERFACE_TYP_RECEIVE = 2;

    public static final Integer RETURN_TYPE_FTP = 1;
    public static final Integer RETURN_TYPE_WEBSERVICE = 2;

    @TableId(value = "SERVICE_ID", type = IdType.ID_WORKER)
    private Long serviceId;

    /**
     * 接口类型1:发布数据 2:接受数据
     */
    @TableField("INTERFACE_TYP")
    private Integer interfaceTyp;

    @TableField("ORGANIZER_ID")
    private Long organizerId;

    @TableField(exist = false)
    private String organizerName;


    /**
     * 接口名称
     */
    @TableField("SERVICE_NAME")
    private String serviceName;
    /**
     * Client调用时唯一识别的标示
     */
    @TableField("SERVICE_IDENTIFY")
    private String serviceIdentify;
    @TableField("SERVICE_URL")
    private String serviceUrl;
    /**
     * 1表示job，2表示trans，3表示自定义
     */
    @TableField("JOB_TYPE")
    private Integer jobType;
    @TableField("TRANS_NAME")
    private String transName;
    /**
     * 用户可以自己选的，只支持FTP和Webservice
            1表示FTP，2表示Webservice

     */
    @TableField("RETURN_TYPE")
    private Integer returnType;
    @TableField("DATASOURCE")
    private String datasource;
    /**
     * 服务接口生成的结果数据超时时间，超过这个时间就要删除数据，单位分钟
     */
    @TableField("TIMEOUT")
    private Integer timeout;
    /**
     * 1表示压缩，0表示不压缩
     */
    @TableField("IS_COMPRESS")
    private Integer isCompress;
    /**
     * 数据源 ID
     */
    @TableField("ID_DATABASE")
    private Long databaseId;

    @TableField(exist = false)
    private String databaseName;

    /**
     * 连接模式名
     */
    @TableField("SCHEMA_NAME")
    private String schemaName;
    /**
     * 连接表名
     */
    @TableField("TABLE_NAME")
    private String tableName;
    /**
     * 分隔符
     */
    @TableField("DELIMITER")
    private String delimiter;
    /**
     * 输出字段
     */
    @TableField("FIELDS")
    private String fields;
    /**
     * 条件表达式
     */
    @TableField("CONDITIONS")
    private String conditions;
    /**
     * 接口说明
     */
    @TableField("INTERFACE_DESC")
    private String interfaceDesc;
    @TableField("JOB_CONFIG_ID")
    private Integer jobConfigId;



    @Override
    protected Serializable pkVal() {
        return this.serviceId;
    }

}
