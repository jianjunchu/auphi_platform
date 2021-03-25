package org.firzjb.joblog.service;

import org.firzjb.joblog.entity.LogJob;
import org.firzjb.joblog.model.request.LogJobRequest;
import org.firzjb.joblog.model.response.LogJobResponse;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.joblog.entity.LogJob;
import org.firzjb.joblog.model.request.LogJobRequest;
import org.firzjb.joblog.model.response.LogJobResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
public interface ILogJobService extends IService<LogJob> {

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<LogJobResponse> getLogJobs(LogJobRequest request);
}
