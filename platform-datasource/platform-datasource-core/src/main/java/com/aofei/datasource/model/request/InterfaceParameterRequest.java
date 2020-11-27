package com.aofei.datasource.model.request;

import com.aofei.base.model.request.BaseRequest;
import lombok.Data;

/**
 * <p>
 * 数据接口参数管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
public class InterfaceParameterRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long interfaceParameterId;
    /**
     * 组织 id
     */
    private Long organizerId;
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 参数名称
     */
    private String parameterName;
    /**
     * 参数数据类型
     */
    private String parameterType;
    /**
     * 参数说明
     */
    private String parameterDesc;

}
