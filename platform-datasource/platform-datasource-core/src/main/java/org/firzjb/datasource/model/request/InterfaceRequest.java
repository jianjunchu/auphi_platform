package org.firzjb.datasource.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 数据接口管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
public class InterfaceRequest extends BaseRequest<InterfaceRequest> {

    private static final long serialVersionUID = 1L;

    private Long interfaceId;
    /**
     * 组织 id
     */
    private Long organizerId;
    /**
     * 接口名称
     */
    private String interfaceName;

    private String interfaceUrl;
    /**
     * 接口请求方式, 1 get 2 post
     */
    private String requestType;
    /**
     * 接口说明
     */
    private String interfaceDesc;
    /**
     * 返回数据格式的描述
     */
    private String returnDesc;
    /**
     * 访问超时时间,单位分钟
     */
    private Integer timeout;

    private List<InterfaceParameterRequest> interfaceParameters;

    private List<InterfaceResponseMappingRequest> interfaceResponseMappings;


}
