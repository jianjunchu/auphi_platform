package org.firzjb.dataservice.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.dataservice.entity.ServiceMonitor;
import org.firzjb.dataservice.mapper.ServiceMonitorMapper;
import org.firzjb.dataservice.model.request.ServiceMonitorRequest;
import org.firzjb.dataservice.model.response.ServiceMonitorResponse;
import org.firzjb.dataservice.service.IServiceMonitorService;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.dataservice.entity.ServiceMonitor;
import org.firzjb.dataservice.model.request.ServiceMonitorRequest;
import org.firzjb.dataservice.model.response.ServiceMonitorResponse;
import org.firzjb.dataservice.service.IServiceMonitorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
@Service
public class ServiceMonitorService extends BaseService<ServiceMonitorMapper, ServiceMonitor> implements IServiceMonitorService {



    @Log(module = "数据服务",description = "分页查询数据接口调用监控")
    @Override
    public Page<ServiceMonitorResponse> getPage(Page<ServiceMonitor> page, ServiceMonitorRequest request) {
        List<ServiceMonitor> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ServiceMonitorResponse.class);
    }



    @Log(module = "数据服务",description = "新建数据接口调用监控")
    @Override
    public ServiceMonitorResponse save(ServiceMonitorRequest request) {
        ServiceMonitor serviceMonitor = BeanCopier.copy(request, ServiceMonitor.class);
        serviceMonitor.preInsert();
        super.insert(serviceMonitor);
        return BeanCopier.copy(serviceMonitor, ServiceMonitorResponse.class);
    }

    @Log(module = "数据服务",description = "修改数据接口调用监控")
    @Override
    public ServiceMonitorResponse update(ServiceMonitorRequest request) {
        ServiceMonitor existing = selectById(request.getMonitorId());
        if (existing != null) {
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ServiceMonitorResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "数据服务",description = "删除数据接口调用监控")
    @Override
    public int del(Long deptId) {
        ServiceMonitor existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


    @Override
    public ServiceMonitorResponse get(Long deptId) {
        ServiceMonitor existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, ServiceMonitorResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
