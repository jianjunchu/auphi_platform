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
public class ServiceAuthResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    private Long authId;

    private Long userId;

    private String username;

    private Long serviceId;

    private String serviceName;

    private String authIp;

    private String useDesc;

    private String useDept;

    private String userName;





}
