package com.aofei.datasource.model.request;

import com.aofei.base.model.request.BaseRequest;
import lombok.Data;

/**
 * <p>
 * 数据接口返回值字段映射
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
public class InterfaceResponseMappingRequest extends BaseRequest<InterfaceResponseMappingRequest> {

    private static final long serialVersionUID = 1L;

    private Long interfaceResponseMappingId;
    /**
     * 组织 id
     */
    private Long organizerId;
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 要解析的JSON路径
     */
    private String jsonPath;
    /**
     * JSON路径对应的新的字段名
     */
    private String fieldName;
    /**
     * 字段说明
     */
    private String fieldDesc;


}
