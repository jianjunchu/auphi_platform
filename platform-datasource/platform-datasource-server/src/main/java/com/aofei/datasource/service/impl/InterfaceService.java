package com.aofei.datasource.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.datasource.entity.Interface;
import com.aofei.datasource.entity.InterfaceParameter;
import com.aofei.datasource.entity.InterfaceResponseMapping;
import com.aofei.datasource.mapper.InterfaceMapper;
import com.aofei.datasource.mapper.InterfaceParameterMapper;
import com.aofei.datasource.mapper.InterfaceResponseMappingMapper;
import com.aofei.datasource.model.request.InterfaceParameterRequest;
import com.aofei.datasource.model.request.InterfaceRequest;
import com.aofei.datasource.model.request.InterfaceResponseMappingRequest;
import com.aofei.datasource.model.response.InterfaceParameterResponse;
import com.aofei.datasource.model.response.InterfaceResponse;
import com.aofei.datasource.model.response.InterfaceResponseMappingResponse;
import com.aofei.datasource.service.IInterfaceService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据接口管理 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Service
public class InterfaceService extends BaseService<InterfaceMapper, Interface> implements IInterfaceService {

    @Autowired
    private InterfaceParameterMapper interfaceParameterMapper;

    @Autowired
    private InterfaceResponseMappingMapper interfaceResponseMappingMapper;

    @Override
    public Page<InterfaceResponse> getPage(Page<Interface> page, InterfaceRequest request) {
        List<Interface> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, InterfaceResponse.class);
    }

    @Override
    public List<InterfaceResponse> getInterfaces(InterfaceRequest request) {
        List<Interface> list = baseMapper.findList(request);
        return BeanCopier.copy(list,InterfaceResponse.class);
    }



    @Log(module = "数据源管理",description = "新建数据接口规则")
    @Override
    public InterfaceResponse save(InterfaceRequest request) {

        Interface anInterface = BeanCopier.copy(request, Interface.class);
        anInterface.preInsert();
        super.insert(anInterface);

        if(request.getInterfaceParameters()!=null){
           for(InterfaceParameterRequest parameter : request.getInterfaceParameters()){
               InterfaceParameter interfaceParameter = BeanCopier.copy(parameter, InterfaceParameter.class);
               interfaceParameter.setInterfaceId(anInterface.getInterfaceId());
               interfaceParameter.preInsert();
               interfaceParameterMapper.insert(interfaceParameter);
           }
        }

        if(request.getInterfaceResponseMappings()!=null){
            for(InterfaceResponseMappingRequest mappingRequest : request.getInterfaceResponseMappings()){
                InterfaceResponseMapping interfaceResponseMapping = BeanCopier.copy(mappingRequest, InterfaceResponseMapping.class);
                interfaceResponseMapping.setInterfaceId(anInterface.getInterfaceId());
                interfaceResponseMapping.preInsert();
                interfaceResponseMappingMapper.insert(interfaceResponseMapping);
            }
        }

        return BeanCopier.copy(anInterface, InterfaceResponse.class);

    }

    @Log(module = "数据源管理",description = "修改数据接口规则")
    @Override
    public InterfaceResponse update(InterfaceRequest request) {


        Interface existing = selectById(request.getInterfaceId());
        if (existing != null) {

            existing.setInterfaceName(request.getInterfaceName());
            existing.setInterfaceDesc(request.getInterfaceDesc());
            existing.setInterfaceUrl(request.getInterfaceUrl());
            existing.setRequestType(request.getRequestType());
            existing.setReturnDesc(request.getReturnDesc());
            existing.setTimeout(request.getTimeout());

            existing.preUpdate();

            super.insertOrUpdate(existing);

            interfaceParameterMapper.delete(new EntityWrapper<InterfaceParameter>().eq("INTERFACE_ID",existing.getInterfaceId()));
            interfaceResponseMappingMapper.delete(new EntityWrapper<InterfaceResponseMapping>().eq("INTERFACE_ID",existing.getInterfaceId()));
            if(request.getInterfaceParameters()!=null){
                for(InterfaceParameterRequest parameter : request.getInterfaceParameters()){
                    InterfaceParameter interfaceParameter = BeanCopier.copy(parameter, InterfaceParameter.class);
                    interfaceParameter.setInterfaceId(existing.getInterfaceId());
                    interfaceParameter.preInsert();
                    interfaceParameterMapper.insert(interfaceParameter);
                }
            }

            if(request.getInterfaceResponseMappings()!=null){
                for(InterfaceResponseMappingRequest mappingRequest : request.getInterfaceResponseMappings()){
                    InterfaceResponseMapping interfaceResponseMapping = BeanCopier.copy(mappingRequest, InterfaceResponseMapping.class);
                    interfaceResponseMapping.setInterfaceId(existing.getInterfaceId());
                    interfaceResponseMapping.preInsert();
                    interfaceResponseMappingMapper.insert(interfaceResponseMapping);
                }
            }
            return BeanCopier.copy(existing, InterfaceResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }

    }
    @Log(module = "数据源管理",description = "删除数据接口规则")
    @Override
    public int del(Long ruleId) {
        Interface existing = selectById(ruleId);
        if (existing != null) {
            super.deleteById(ruleId);
            interfaceParameterMapper.delete(new EntityWrapper<InterfaceParameter>().eq("INTERFACE_ID",existing.getInterfaceId()));
            interfaceResponseMappingMapper.delete(new EntityWrapper<InterfaceResponseMapping>().eq("INTERFACE_ID",existing.getInterfaceId()));
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



    @Override
    public InterfaceResponse get(Long interfaceId) {
        Interface existing = selectById(interfaceId);
        if(existing!=null){
            InterfaceResponse interfaceResponse =  BeanCopier.copy(existing, InterfaceResponse.class);

            List<InterfaceParameter> parameters = interfaceParameterMapper.selectList(new EntityWrapper<InterfaceParameter>().eq("INTERFACE_ID",existing.getInterfaceId()));

            List<InterfaceResponseMapping> responseMappings = interfaceResponseMappingMapper.selectList(new EntityWrapper<InterfaceResponseMapping>().eq("INTERFACE_ID",existing.getInterfaceId()));

            interfaceResponse.setInterfaceParameters(BeanCopier.copy(parameters, InterfaceParameterResponse.class));
            interfaceResponse.setInterfaceResponseMappings(BeanCopier.copy(responseMappings, InterfaceResponseMappingResponse.class));

            return interfaceResponse;

        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

}
