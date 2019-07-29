package com.aofei.dataservice.service;

import com.aofei.dataservice.entity.ServiceMonitor;
import com.aofei.dataservice.model.request.ServiceMonitorRequest;
import com.aofei.dataservice.model.response.ServiceMonitorResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
public interface IServiceMonitorService extends IService<ServiceMonitor> {


    Page<ServiceMonitorResponse> getPage(Page<ServiceMonitor> page, ServiceMonitorRequest request);

    ServiceMonitorResponse save(ServiceMonitorRequest request);

    ServiceMonitorResponse update(ServiceMonitorRequest request);

    int del(Long id);

    ServiceMonitorResponse get(Long id);
}
