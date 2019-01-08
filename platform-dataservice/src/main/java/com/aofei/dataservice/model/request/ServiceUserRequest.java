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
public class ServiceUserRequest extends BaseRequest<ServiceUserRequest> {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private String systemName;
    private String systemIp;
    private String systemDesc;




}
