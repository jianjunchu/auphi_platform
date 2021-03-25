package org.firzjb.schedule.service.impl;

import org.firzjb.schedule.entity.JobDependencies;
import org.firzjb.schedule.mapper.JobDependenciesMapper;
import org.firzjb.schedule.service.IJobDependenciesService;
import org.firzjb.base.service.impl.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 依赖调度关系表 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2021-02-02
 */
@Service
public class JobDependenciesService extends BaseService<JobDependenciesMapper, JobDependencies> implements IJobDependenciesService {

}
