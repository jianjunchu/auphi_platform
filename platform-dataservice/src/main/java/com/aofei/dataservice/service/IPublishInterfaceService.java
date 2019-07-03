package com.aofei.dataservice.service;

import com.aofei.dataservice.entity.ServiceInterface;
import com.aofei.dataservice.model.request.PublishInterfaceRequest;
import com.aofei.dataservice.model.response.PublishInterfaceResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
public interface IServiceInterfaceService extends IService<ServiceInterface> {

    Page<PublishInterfaceResponse> getPage(Page<ServiceInterface> page, PublishInterfaceRequest request);

    List<PublishInterfaceResponse> getServiceInterfaces(PublishInterfaceRequest request);

    PublishInterfaceResponse save(PublishInterfaceRequest request);

    PublishInterfaceResponse update(PublishInterfaceRequest request);

    int del(Long id);

    PublishInterfaceResponse get(Long id);
}
