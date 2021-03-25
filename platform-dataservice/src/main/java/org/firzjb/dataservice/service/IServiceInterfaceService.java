package org.firzjb.dataservice.service;

import org.firzjb.dataservice.entity.ServiceInterface;
import org.firzjb.dataservice.model.request.ServiceInterfaceRequest;
import org.firzjb.dataservice.model.response.ServiceInterfaceResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataservice.entity.ServiceInterface;
import org.firzjb.dataservice.model.request.ServiceInterfaceRequest;
import org.firzjb.dataservice.model.response.ServiceInterfaceResponse;

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

    Page<ServiceInterfaceResponse> getPage(Page<ServiceInterface> page, ServiceInterfaceRequest request);

    List<ServiceInterfaceResponse> getServiceInterfaces(ServiceInterfaceRequest request);

    ServiceInterfaceResponse save(ServiceInterfaceRequest request);

    ServiceInterfaceResponse update(ServiceInterfaceRequest request);

    int del(Long id);

    ServiceInterfaceResponse get(Long id);
}
