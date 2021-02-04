package com.aofei.depend.service;

import com.aofei.depend.entity.LogJobDependencies;
import com.aofei.depend.model.request.LogJobDependenciesRequest;
import com.aofei.depend.model.response.LogJobDependenciesResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 事件调度日志
 服务类
 * </p>
 *
 * @author Tony
 * @since 2021-02-03
 */
public interface ILogJobDependenciesService extends IService<LogJobDependencies> {

    /**
     * 分页查询事件调度日志
     * @param pagination
     * @param request
     * @return
     */
    Page<LogJobDependenciesResponse> getPage(Page<LogJobDependencies> pagination, LogJobDependenciesRequest request);
}
