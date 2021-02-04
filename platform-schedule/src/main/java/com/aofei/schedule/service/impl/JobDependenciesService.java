package com.aofei.schedule.service.impl;

import com.aofei.schedule.entity.JobDependencies;
import com.aofei.schedule.mapper.JobDependenciesMapper;
import com.aofei.schedule.service.IJobDependenciesService;
import com.aofei.base.service.impl.BaseService;
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
