package com.aofei.translog.service;

import com.aofei.translog.entity.LogTrans;
import com.aofei.translog.model.request.LogTransRequest;
import com.aofei.translog.model.response.LogTransResponse;
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
public interface ILogTransService extends IService<LogTrans> {

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<LogTransResponse> getLogJobs(LogTransRequest request);
}
