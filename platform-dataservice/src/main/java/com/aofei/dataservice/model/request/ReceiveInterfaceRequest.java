package com.aofei.dataservice.model.request;

import com.aofei.base.model.request.BaseRequest;
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
public class ReceiveInterfaceRequest extends BaseRequest<ReceiveInterfaceRequest> {

    private static final long serialVersionUID = 1L;

    private Long serviceId;

    private String serviceName;
    /**
     * Client调用时唯一识别的标示
     */
    private String serviceIdentify;

    private String serviceUrl;

    private String datasource;

    private String tableName;

    private String delimiter;

    private String[] fields;

    private String[] jsonPath;

    private String conditions;

    private String interfaceDesc;

    private String databaseId;


}
