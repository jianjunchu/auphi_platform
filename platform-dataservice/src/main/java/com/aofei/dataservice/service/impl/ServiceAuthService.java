package com.aofei.dataservice.service.impl;

import com.aofei.base.common.Const;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.dataservice.entity.ServiceAuth;
import com.aofei.dataservice.entity.ServiceAuth;
import com.aofei.dataservice.exception.DataServiceError;
import com.aofei.dataservice.mapper.ServiceAuthMapper;
import com.aofei.dataservice.model.request.ServiceAuthRequest;
import com.aofei.dataservice.model.response.ServiceAuthResponse;
import com.aofei.dataservice.service.IServiceAuthService;
import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务接口授权 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
@Service
public class ServiceAuthService extends BaseService<ServiceAuthMapper, ServiceAuth> implements IServiceAuthService {


    @Log(module = "数据服务",description = "分页查询服务接口授权")
    @Override
    public Page<ServiceAuthResponse> getPage(Page<ServiceAuth> page, ServiceAuthRequest request) {
        List<ServiceAuth> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ServiceAuthResponse.class);
    }

    @Log(module = "数据服务",description = "查询服务接口授权")
    @Override
    public List<ServiceAuthResponse> getServiceAuths(ServiceAuthRequest request) {
        List<ServiceAuth> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ServiceAuthResponse.class);
    }

    @Log(module = "数据服务",description = "新建服务接口授权")
    @Override
    public ServiceAuthResponse save(ServiceAuthRequest request) {
        ServiceAuth publishInterface = BeanCopier.copy(request, ServiceAuth.class);

        ServiceAuth existing = selectOne(new EntityWrapper<ServiceAuth>()
                .eq("DEL_FLAG", Const.NO)
                .eq("SERVICE_ID",request.getServiceId())
                .eq("USER_ID",request.getUserId()));

        if(existing == null){
            publishInterface.preInsert();
            super.insert(publishInterface);
            return BeanCopier.copy(publishInterface, ServiceAuthResponse.class);
        }else{
            throw new ApplicationException(DataServiceError.SERVICE_AUTH_EXISTING.getCode(), "用户服务授权已存在");
        }

    }

    @Log(module = "数据服务",description = "修改服务接口授权")
    @Override
    public ServiceAuthResponse update(ServiceAuthRequest request) {
        ServiceAuth existing = selectById(request.getAuthId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ServiceAuthResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "数据服务",description = "删除服务接口授权")
    @Override
    public int del(Long deptId) {
        ServiceAuth existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public ServiceAuthResponse get(Long deptId) {
        ServiceAuth existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ServiceAuthResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public ServiceAuthResponse getServiceAuth(ServiceAuthRequest request) {
        ServiceAuth existing = selectOne(new EntityWrapper<ServiceAuth>()

                .eq("DEL_FLAG", Const.NO)
                .eq("SERVICE_ID",request.getServiceId())
                .eq("USER_ID",request.getUserId()));
        return existing ==null ? null : BeanCopier.copy(existing, ServiceAuthResponse.class);
    }
}
