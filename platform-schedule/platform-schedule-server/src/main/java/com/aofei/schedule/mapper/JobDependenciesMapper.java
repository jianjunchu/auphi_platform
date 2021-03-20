package com.aofei.schedule.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.schedule.entity.JobDependencies;
import com.aofei.base.mapper.BaseMapper;

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
