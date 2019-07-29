package com.aofei.dataservice.service;

import com.aofei.dataservice.entity.ServiceAuth;
import com.aofei.dataservice.model.request.ServiceAuthRequest;
import com.aofei.dataservice.model.response.ServiceAuthResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 服务接口授权 服务类
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
public interface IServiceAuthService extends IService<ServiceAuth> {

    Page<ServiceAuthResponse> getPage(Page<ServiceAuth> page, ServiceAuthRequest request);

    List<ServiceAuthResponse> getServiceAuths(ServiceAuthRequest request);

    ServiceAuthResponse save(ServiceAuthRequest request);

    ServiceAuthResponse update(ServiceAuthRequest request);

    int del(Long id);

    ServiceAuthResponse get(Long id);

    ServiceAuthResponse getServiceAuth(ServiceAuthRequest authRequest);
}
