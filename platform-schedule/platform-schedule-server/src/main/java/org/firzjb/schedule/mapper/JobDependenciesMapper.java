package org.firzjb.schedule.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.schedule.entity.JobDependencies;
import org.firzjb.base.mapper.BaseMapper;

/**
 * <p>
 * 依赖调度关系表 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2021-02-02
 */
@MyBatisMapper
public interface JobDependenciesMapper extends BaseMapper<JobDependencies> {

}
