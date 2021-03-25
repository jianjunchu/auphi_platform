package org.firzjb.dataservice.service;

import org.firzjb.dataservice.entity.ServiceInterface;
import org.firzjb.dataservice.entity.ServiceInterfaceField;
import org.firzjb.dataservice.model.request.ServiceInterfaceRequest;
import org.firzjb.dataservice.model.response.ServiceInterfaceFieldResponse;
import org.firzjb.dataservice.model.response.ServiceInterfaceResponse;
import org.firzjb.log.annotation.Log;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataservice.entity.ServiceInterfaceField;
import org.firzjb.dataservice.model.response.ServiceInterfaceFieldResponse;

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
