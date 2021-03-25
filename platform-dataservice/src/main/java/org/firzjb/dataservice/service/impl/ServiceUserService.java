package org.firzjb.dataservice.service.impl;

import org.firzjb.base.common.Const;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.dataservice.entity.ServiceUser;
import org.firzjb.dataservice.exception.DataServiceError;
import org.firzjb.dataservice.mapper.ServiceAuthMapper;
import org.firzjb.dataservice.mapper.ServiceUserMapper;
import org.firzjb.dataservice.model.request.ServiceUserRequest;
import org.firzjb.dataservice.model.response.ServiceUserResponse;
import org.firzjb.dataservice.service.IServiceUserService;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.dataservice.entity.ServiceUser;
import org.firzjb.dataservice.mapper.ServiceAuthMapper;
import org.firzjb.dataservice.model.request.ServiceUserRequest;
import org.firzjb.dataservice.model.response.ServiceUserResponse;
import org.firzjb.dataservice.service.IServiceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据发布访问用户
 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2019-07-23
 */
@Service
public class ServiceUserService extends BaseService<ServiceUserMapper, ServiceUser> implements IServiceUserService {


    @Autowired
    ServiceAuthMapper serviceAuthMapper;

    @Log(module = "数据服务",description = "分页查询数据发布访问用户")
    @Override
    public Page<ServiceUserResponse> getPage(Page<ServiceUser> page, ServiceUserRequest request) {
        List<ServiceUser> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ServiceUserResponse.class);
    }

    @Log(module = "数据服务",description = "查询数据发布访问用户")
    @Override
    public List<ServiceUserResponse> getServiceUsers(ServiceUserRequest request) {
        List<ServiceUser> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ServiceUserResponse.class);
    }

    @Log(module = "数据服务",description = "新建数据发布访问用户")
    @Override
    public ServiceUserResponse save(ServiceUserRequest request) {
        ServiceUser publishInterface = BeanCopier.copy(request, ServiceUser.class);

        ServiceUser existing = selectOne(new EntityWrapper<ServiceUser>()
                .eq("DEL_FLAG", Const.NO)
                .eq("USERNAME",request.getUsername())
                .eq("ORGANIZER_ID",request.getOrganizerId()));
        if(existing == null){
            publishInterface.preInsert();
            super.insert(publishInterface);
            return BeanCopier.copy(publishInterface, ServiceUserResponse.class);
        }else{
            throw new ApplicationException(DataServiceError.USERNAME_EXIST.getCode(),"用户名已存在!");
        }

    }

    @Log(module = "数据服务",description = "修改数据发布访问用户")
    @Override
    public ServiceUserResponse update(ServiceUserRequest request) {
        ServiceUser existing = selectById(request.getUserId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ServiceUserResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "数据服务",description = "删除数据发布访问用户")
    @Override
    public int del(Long deptId) {
        ServiceUser existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            serviceAuthMapper.deleteByUserId(existing.getUserId());
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public ServiceUserResponse get(Long deptId) {
        ServiceUser existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ServiceUserResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public ServiceUserResponse getAuthUser(ServiceUserRequest request) {
        ServiceUser existing = selectOne(new EntityWrapper<ServiceUser>()
                .eq("DEL_FLAG", Const.NO)
                .eq("USERNAME",request.getUsername())
                .eq("PASSWORD",request.getPassword())
                .eq("ORGANIZER_ID",request.getOrganizerId()));

        if(existing != null){
            return BeanCopier.copy(existing, ServiceUserResponse.class);
        }else{
            throw new ApplicationException(DataServiceError.LOGIN_FAILED.getCode(), "用户名密码错误!");
        }

    }
}
