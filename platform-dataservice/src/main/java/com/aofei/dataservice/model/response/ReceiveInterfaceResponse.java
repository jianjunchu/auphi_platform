package com.aofei.dataservice.model.response;

import com.aofei.base.model.response.BaseResponse;
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
public class ReceiveInterfaceResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    private Long serviceId;

    private String serviceName;
    /**
     * Client调用时唯一识别的标示
     */
    private String serviceIdentify;

    private String serviceUrl;

    private String datasource;

    private String tablename;

    private String[] fields;

    private String[] jasonPath;

    private String interfaceDesc;

    private String databaseId;

    private String databaseName;

}
