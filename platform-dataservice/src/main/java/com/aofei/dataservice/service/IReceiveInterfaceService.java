package com.aofei.dataservice.service;

import com.aofei.dataservice.entity.ReceiveInterface;
import com.aofei.dataservice.model.request.ReceiveInterfaceRequest;
import com.aofei.dataservice.model.response.ReceiveInterfaceResponse;
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
public interface IReceiveInterfaceService extends IService<ReceiveInterface> {

    Page<ReceiveInterfaceResponse> getPage(Page<ReceiveInterface> page, ReceiveInterfaceRequest request);

    List<ReceiveInterfaceResponse> getServiceInterfaces(ReceiveInterfaceRequest request);

    ReceiveInterfaceResponse save(ReceiveInterfaceRequest request);

    ReceiveInterfaceResponse update(ReceiveInterfaceRequest request);

    int del(Long id);

    ReceiveInterfaceResponse get(Long id);
}
