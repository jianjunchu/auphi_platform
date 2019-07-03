package com.aofei.dataservice.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataservice.entity.ReceiveInterface;
import com.aofei.dataservice.mapper.ReceiveInterfaceMapper;
import com.aofei.dataservice.model.request.ReceiveInterfaceRequest;
import com.aofei.dataservice.model.response.ReceiveInterfaceResponse;
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
public class ReceiveInterfaceService extends BaseService<ReceiveInterfaceMapper, ReceiveInterface> implements IReceiveInterfaceService {


    @Override
    public Page<ReceiveInterfaceResponse> getPage(Page<ReceiveInterface> page, ReceiveInterfaceRequest request) {
        List<ReceiveInterface> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ReceiveInterfaceResponse.class);
    }

    @Override
    public List<ReceiveInterfaceResponse> getServiceInterfaces(ReceiveInterfaceRequest request) {
        List<ReceiveInterface> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ReceiveInterfaceResponse.class);
    }

    @Log(module = "数据接口管理",description = "新建接收数据接口")
    @Override
    public ReceiveInterfaceResponse save(ReceiveInterfaceRequest request) {
        ReceiveInterface receiveInterface = BeanCopier.copy(request, ReceiveInterface.class);
        receiveInterface.preInsert();
        super.insert(receiveInterface);
        return BeanCopier.copy(receiveInterface, ReceiveInterfaceResponse.class);
    }

    @Log(module = "数据接口管理",description = "修改接收数据接口")
    @Override
    public ReceiveInterfaceResponse update(ReceiveInterfaceRequest request) {
        ReceiveInterface existing = selectById(request.getServiceId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ReceiveInterfaceResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Log(module = "数据接口管理",description = "删除接收数据接口")
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
    public ReceiveInterfaceResponse get(Long deptId) {
        ReceiveInterface existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ReceiveInterfaceResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
