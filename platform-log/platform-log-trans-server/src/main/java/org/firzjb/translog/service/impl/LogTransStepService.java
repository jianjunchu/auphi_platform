package org.firzjb.translog.service.impl;

import org.firzjb.translog.entity.LogTransStep;
import org.firzjb.translog.mapper.LogTransStepMapper;
import org.firzjb.translog.service.ILogTransStepService;
import org.firzjb.base.service.impl.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Service
public class LogTransStepService extends BaseService<LogTransStepMapper, LogTransStep> implements ILogTransStepService {

}
