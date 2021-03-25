package org.firzjb.depend.service.impl;

import org.firzjb.base.service.impl.BaseService;
import org.firzjb.depend.entity.LogJobDependencies;
import org.firzjb.depend.mapper.LogJobDependenciesMapper;
import org.firzjb.depend.model.request.LogJobDependenciesRequest;
import org.firzjb.depend.model.response.LogJobDependenciesResponse;
import org.firzjb.depend.service.ILogJobDependenciesService;
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
