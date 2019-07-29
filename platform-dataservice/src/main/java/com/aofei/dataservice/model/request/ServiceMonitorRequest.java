package com.aofei.dataservice.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.aofei.base.entity.BaseEntity;
import com.aofei.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2019-07-24
 */
@Data
public class ServiceMonitorRequest extends BaseRequest<ServiceMonitorRequest> {

    private static final long serialVersionUID = 1L;

    private Long monitorId;
    /**
     * 组织 ID
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize=false)
    private Long organizerId;
    /**
     * 服务接口 ID
     */
    private Long serviceId;


    private String serviceName;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 执行的用户
     */
    private Long serviceUserId;

    /**
     * 系统名称
     */
    private String systemName;




}
