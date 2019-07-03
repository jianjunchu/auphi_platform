package com.aofei.dataservice.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataservice.entity.ReceiveInterface;
import com.aofei.dataservice.mapper.ReceiveServiceInterfaceMapper;
import com.aofei.dataservice.model.request.ReceiveInterfaceRequest;
import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import com.aofei.dataservice.service.IReceiveInterfaceService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
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
public class ReceiveServiceInterfaceService extends BaseService<ReceiveServiceInterfaceMapper, ReceiveInterface> implements IReceiveInterfaceService {


    @Override
    public Page<ServiceInterfaceResponse> getPage(Page<ReceiveInterface> page, ReceiveInterfaceRequest request) {
        List<ReceiveInterface> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ServiceInterfaceResponse.class);
    }

    @Override
    public List<ServiceInterfaceResponse> getServiceInterfaces(ReceiveInterfaceRequest request) {
        List<ReceiveInterface> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ServiceInterfaceResponse.class);
    }

    @Log(module = "对外数据接出接口管理",description = "新建对外数据接出接口信息")
    @Override
    public ServiceInterfaceResponse save(ReceiveInterfaceRequest request) {
        ReceiveInterface serviceInterface = BeanCopier.copy(request, ReceiveInterface.class);
        serviceInterface.preInsert();
        super.insert(serviceInterface);
        return BeanCopier.copy(serviceInterface, ServiceInterfaceResponse.class);
    }

    @Log(module = "对外数据接出接口管理",description = "修改对外数据接出接口信息")
    @Override
    public ServiceInterfaceResponse update(ReceiveInterfaceRequest request) {
        ReceiveInterface existing = selectById(request.getServiceId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ServiceInterfaceResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "对外数据接出接口管理",description = "删除对外数据接出接口信息")
    @Override
    public int del(Long deptId) {
        ReceiveInterface existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public ServiceInterfaceResponse get(Long deptId) {
        ReceiveInterface existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ServiceInterfaceResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
