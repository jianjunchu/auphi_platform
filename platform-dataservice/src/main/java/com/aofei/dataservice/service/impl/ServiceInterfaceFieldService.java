package com.aofei.dataservice.service.impl;

import com.aofei.dataservice.entity.ServiceInterfaceField;
import com.aofei.dataservice.mapper.ServiceInterfaceFieldMapper;
import com.aofei.dataservice.model.response.ServiceInterfaceFieldResponse;
import com.aofei.dataservice.service.IServiceInterfaceFieldService;
import com.aofei.base.service.impl.BaseService;
import com.aofei.utils.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2019-07-29
 */
@Service
public class ServiceInterfaceFieldService extends BaseService<ServiceInterfaceFieldMapper, ServiceInterfaceField> implements IServiceInterfaceFieldService {

    @Override
    public List<ServiceInterfaceFieldResponse> getServiceInterfaceFields(Long serviceId) {
        List<ServiceInterfaceField> list = baseMapper.selectByServiceId(serviceId);
        return BeanCopier.copy(list, ServiceInterfaceFieldResponse.class);
    }
}
