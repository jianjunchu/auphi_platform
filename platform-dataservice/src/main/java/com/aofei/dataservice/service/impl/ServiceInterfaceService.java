package com.aofei.dataservice.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataservice.entity.ServiceInterface;
import com.aofei.dataservice.entity.ServiceInterfaceField;
import com.aofei.dataservice.mapper.PublishInterfaceMapper;
import com.aofei.dataservice.mapper.ServiceAuthMapper;
import com.aofei.dataservice.mapper.ServiceInterfaceFieldMapper;
import com.aofei.dataservice.model.request.ServiceInterfaceFieldRequest;
import com.aofei.dataservice.model.request.ServiceInterfaceRequest;
import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import com.aofei.dataservice.service.IServiceAuthService;
import com.aofei.dataservice.service.IServiceInterfaceService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Service
public class ServiceInterfaceService extends BaseService<PublishInterfaceMapper, ServiceInterface> implements IServiceInterfaceService {


    @Autowired
    ServiceInterfaceFieldMapper serviceInterfaceFieldMapper;

    @Autowired
    ServiceAuthMapper serviceAuthMapper;

    @Log(module = "数据服务",description = "分页查询数据发布接口")
    @Override
    public Page<ServiceInterfaceResponse> getPage(Page<ServiceInterface> page, ServiceInterfaceRequest request) {
        List<ServiceInterface> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ServiceInterfaceResponse.class);
    }

    @Log(module = "数据服务",description = "查询数据发布接口")
    @Override
    public List<ServiceInterfaceResponse> getServiceInterfaces(ServiceInterfaceRequest request) {
        List<ServiceInterface> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ServiceInterfaceResponse.class);
    }

    @Log(module = "数据服务",description = "新建数据发布接口")
    @Override
    public ServiceInterfaceResponse save(ServiceInterfaceRequest request) {
        ServiceInterface publishInterface = BeanCopier.copy(request, ServiceInterface.class);
        publishInterface.preInsert();
        publishInterface.setServiceId(IdWorker.getId());
        if(ServiceInterface.INTERFACE_TYP_PUBLISH.equals(publishInterface.getInterfaceTyp()) ){
            publishInterface.setServiceUrl("/dataservice/publish/"+publishInterface.getServiceId()+"/publish");
        }else{
            publishInterface.setServiceUrl("/dataservice/publish/"+publishInterface.getServiceId()+"/receive");
            List<ServiceInterfaceFieldRequest> interfaceFields = request.getInterfaceFields();
            for(ServiceInterfaceFieldRequest fieldRequest : interfaceFields){

                ServiceInterfaceField serviceInterfaceFiel = BeanCopier.copy(fieldRequest,ServiceInterfaceField.class);
                serviceInterfaceFiel.setServiceId(publishInterface.getServiceId());
                serviceInterfaceFiel.preInsert();
                serviceInterfaceFiel.insert();
            }
        }


        super.insert(publishInterface);
        return BeanCopier.copy(publishInterface, ServiceInterfaceResponse.class);
    }

    @Log(module = "数据服务",description = "修改数据发布接口")
    @Override
    public ServiceInterfaceResponse update(ServiceInterfaceRequest request) {
        ServiceInterface existing = selectById(request.getServiceId());
        if (existing != null) {
            existing.setServiceName(request.getServiceName());
            existing.setConditions(request.getConditions());
            existing.setDatabaseId(request.getDatabaseId());
            existing.setReturnType(request.getReturnType());
            existing.setTableName(request.getTableName());
            existing.setFields(request.getFields());


            serviceInterfaceFieldMapper.deleteByServiceId(request.getServiceId());



            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ServiceInterfaceResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "数据服务",description = "删除数据发布接口")
    @Override
    public int del(Long deptId) {
        ServiceInterface existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            serviceInterfaceFieldMapper.deleteByServiceId(existing.getServiceId());
            serviceAuthMapper.deleteByServiceId(existing.getServiceId());
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public ServiceInterfaceResponse get(Long deptId) {
        ServiceInterface existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ServiceInterfaceResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
