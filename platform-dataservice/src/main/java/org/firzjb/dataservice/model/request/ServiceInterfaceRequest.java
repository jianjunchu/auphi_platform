package org.firzjb.dataservice.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import org.firzjb.base.model.request.BaseRequest;
import org.firzjb.dataservice.entity.ServiceInterfaceField;
import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 数据接口管理
 * </p>
 *
 * @author Tony
 * @since 2019-07-22
 */
@Data
public class ServiceInterfaceRequest extends BaseRequest<ServiceInterfaceRequest> {

    private static final long serialVersionUID = 1L;

    private Long serviceId;

    /**
     * 接口类型1:发布数据 2:接受数据
     */
    private Integer interfaceTyp;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize=false)
    private Long organizerId;

    /**
     * 接口名称
     */
    private String serviceName;
    /**
     * Client调用时唯一识别的标示
     */
    private String serviceIdentify;
    private String serviceUrl;
    /**
     * 1表示job，2表示trans，3表示自定义
     */
    private Integer jobType;
    private String transName;
    /**
     * 用户可以自己选的，只支持FTP和Webservice
            1表示FTP，2表示Webservice

     */
    private Integer returnType;
    private String datasource;
    /**
     * 服务接口生成的结果数据超时时间，超过这个时间就要删除数据，单位分钟
     */
    private Integer timeout;
    /**
     * 1表示压缩，0表示不压缩
     */
    private Integer isCompress;
    /**
     * 数据源 ID
     */
    private Long databaseId;

    private String databaseName;

    /**
     * 连接模式名
     */
    private String schemaName;
    /**
     * 连接表名
     */
    private String tableName;
    /**
     * 分隔符
     */
    private String delimiter;
    /**
     * 输出字段
     */
    private String fields;
    /**
     * 条件表达式
     */
    private String conditions;
    /**
     * 接口说明
     */
    private String interfaceDesc;

    private Integer jobConfigId;

    List<ServiceInterfaceFieldRequest> interfaceFields;



}
