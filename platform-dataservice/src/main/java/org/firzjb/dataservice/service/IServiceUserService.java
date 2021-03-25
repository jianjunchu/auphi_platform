package org.firzjb.dataservice.service;

import org.firzjb.dataservice.entity.ServiceUser;
import org.firzjb.dataservice.entity.ServiceUser;
import org.firzjb.dataservice.model.request.ServiceUserRequest;
import org.firzjb.dataservice.model.response.ServiceUserResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataservice.entity.ServiceUser;
import org.firzjb.dataservice.model.request.ServiceUserRequest;
import org.firzjb.dataservice.model.response.ServiceUserResponse;

import java.util.List;

/**
 * <p>
 * 数据发布访问用户
 服务类
 * </p>
 *
 * @author Tony
 * @since 2019-07-23
 */
public interface IServiceUserService extends IService<ServiceUser> {


    Page<ServiceUserResponse> getPage(Page<ServiceUser> page, ServiceUserRequest request);

    List<ServiceUserResponse> getServiceUsers(ServiceUserRequest request);

    ServiceUserResponse save(ServiceUserRequest request);

    ServiceUserResponse update(ServiceUserRequest request);

    int del(Long id);

    ServiceUserResponse get(Long id);

    ServiceUserResponse getAuthUser(ServiceUserRequest userRequest);
}
