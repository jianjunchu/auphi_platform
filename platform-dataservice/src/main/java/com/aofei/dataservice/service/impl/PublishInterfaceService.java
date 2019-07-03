package com.aofei.dataservice.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataservice.entity.PublishInterface;
import com.aofei.dataservice.mapper.PublishInterfaceMapper;
import com.aofei.dataservice.model.request.PublishInterfaceRequest;
import com.aofei.dataservice.model.response.PublishInterfaceResponse;
import com.aofei.dataservice.service.IPublishInterfaceService;
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
public class PublishInterfaceService extends BaseService<PublishInterfaceMapper, PublishInterface> implements IPublishInterfaceService {


    @Override
    public Page<PublishInterfaceResponse> getPage(Page<PublishInterface> page, PublishInterfaceRequest request) {
        List<PublishInterface> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, PublishInterfaceResponse.class);
    }

    @Override
    public List<PublishInterfaceResponse> getServiceInterfaces(PublishInterfaceRequest request) {
        List<PublishInterface> list = baseMapper.findList(request);
        return BeanCopier.copy(list, PublishInterfaceResponse.class);
    }

    @Log(module = "对外数据接出接口管理",description = "新建对外数据接出接口信息")
    @Override
    public PublishInterfaceResponse save(PublishInterfaceRequest request) {
        PublishInterface ServiceInterface = BeanCopier.copy(request, PublishInterface.class);
        ServiceInterface.preInsert();
        super.insert(ServiceInterface);
        return BeanCopier.copy(ServiceInterface, PublishInterfaceResponse.class);
    }

    @Log(module = "对外数据接出接口管理",description = "修改对外数据接出接口信息")
    @Override
    public PublishInterfaceResponse update(PublishInterfaceRequest request) {
        PublishInterface existing = selectById(request.getServiceId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, PublishInterfaceResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "对外数据接出接口管理",description = "删除对外数据接出接口信息")
    @Override
    public int del(Long deptId) {
        PublishInterface existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public PublishInterfaceResponse get(Long deptId) {
        PublishInterface existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, PublishInterfaceResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
