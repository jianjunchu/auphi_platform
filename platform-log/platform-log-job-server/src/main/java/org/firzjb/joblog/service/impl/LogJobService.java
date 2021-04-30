package org.firzjb.joblog.service.impl;

import org.firzjb.joblog.entity.LogJob;
import org.firzjb.joblog.mapper.LogJobMapper;
import org.firzjb.joblog.model.request.LogJobRequest;
import org.firzjb.joblog.model.response.LogJobResponse;
import org.firzjb.joblog.service.ILogJobService;
import org.firzjb.base.service.impl.BaseService;
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
public class LogJobService extends BaseService<LogJobMapper, LogJob> implements ILogJobService {

    @Override
    public List<LogJobResponse> getLogJobs(LogJobRequest request) {
        return null;
    }

    @Override
    public int updateStatusToStop() {
        return baseMapper.updateStatusToStop();
    }
}
