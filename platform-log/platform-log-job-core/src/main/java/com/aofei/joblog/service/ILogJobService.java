package com.aofei.joblog.service;

import com.aofei.joblog.entity.LogJob;
import com.aofei.joblog.model.request.LogJobRequest;
import com.aofei.joblog.model.response.LogJobResponse;
import com.baomidou.mybatisplus.service.IService;

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
