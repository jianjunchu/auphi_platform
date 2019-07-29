package com.aofei.dataservice.service;

import com.aofei.dataservice.entity.ServiceInterface;
import com.aofei.dataservice.entity.ServiceInterfaceField;
import com.aofei.dataservice.model.request.ServiceInterfaceRequest;
import com.aofei.dataservice.model.response.ServiceInterfaceFieldResponse;
import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2019-07-29
 */
public interface IServiceInterfaceFieldService extends IService<ServiceInterfaceField> {


    List<ServiceInterfaceFieldResponse> getServiceInterfaceFields(Long serviceId);
}
