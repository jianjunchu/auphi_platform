package org.firzjb.translog.service;

import org.firzjb.translog.entity.LogTrans;
import org.firzjb.translog.model.request.LogTransRequest;
import org.firzjb.translog.model.response.LogTransResponse;
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
