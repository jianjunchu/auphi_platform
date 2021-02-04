package com.aofei.depend.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.depend.entity.LogJobDependencies;
import com.aofei.depend.mapper.LogJobDependenciesMapper;
import com.aofei.depend.model.request.LogJobDependenciesRequest;
import com.aofei.depend.model.response.LogJobDependenciesResponse;
import com.aofei.depend.service.ILogJobDependenciesService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 事件调度日志
 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2021-02-03
 */
@Service
public class LogJobDependenciesService extends BaseService<LogJobDependenciesMapper, LogJobDependencies> implements ILogJobDependenciesService {

    /**
     * 分页查询事件调度日志
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page<LogJobDependenciesResponse> getPage(Page<LogJobDependencies> page, LogJobDependenciesRequest request) {
        List<LogJobDependencies> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, LogJobDependenciesResponse.class);
    }
}
