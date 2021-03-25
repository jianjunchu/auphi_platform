package org.firzjb.translog.service.impl;

import org.firzjb.translog.entity.LogTrans;
import org.firzjb.translog.mapper.LogTransMapper;
import org.firzjb.translog.model.request.LogTransRequest;
import org.firzjb.translog.model.response.LogTransResponse;
import org.firzjb.translog.service.ILogTransService;
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
public class LogTransService extends BaseService<LogTransMapper, LogTrans> implements ILogTransService {

    @Override
    public List<LogTransResponse> getLogJobs(LogTransRequest request) {
        return null;
    }
}
