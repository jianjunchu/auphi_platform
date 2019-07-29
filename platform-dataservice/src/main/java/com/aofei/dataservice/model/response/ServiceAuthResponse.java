package com.aofei.dataservice.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.aofei.base.model.response.BaseResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Data
public class ServiceAuthResponse  {

    private static final long serialVersionUID = 1L;

    private Long authId;
    /**
     * 组织 ID
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize=false)
    private Long organizerId;
    /**
     * 授权用户
     */
    private Long userId;

    private String username;
    /**
     * 授权服务接口
     */
    private Long serviceId;
    /**
     * 接口服务名称
     */
    private String serviceName;

    /**
     * 接口服务地址
     */
    private String serviceUrl;

    /**
     * 授权 IP
     */
    private String authIp;
    /**
     * 业务用途
     */
    private String useDesc;
    /**
     * 使用部门
     */
    private String useDept;
    /**
     * 使用人员
     */
    private String userName;




}
