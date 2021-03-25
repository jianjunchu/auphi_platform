package org.firzjb.dataservice.service.impl;

import org.firzjb.dataservice.entity.ServiceInterfaceField;
import org.firzjb.dataservice.mapper.ServiceInterfaceFieldMapper;
import org.firzjb.dataservice.model.response.ServiceInterfaceFieldResponse;
import org.firzjb.dataservice.service.IServiceInterfaceFieldService;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.utils.BeanCopier;
import org.firzjb.dataservice.entity.ServiceInterfaceField;
import org.firzjb.dataservice.mapper.ServiceInterfaceFieldMapper;
import org.firzjb.dataservice.model.response.ServiceInterfaceFieldResponse;
import org.firzjb.dataservice.service.IServiceInterfaceFieldService;
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
