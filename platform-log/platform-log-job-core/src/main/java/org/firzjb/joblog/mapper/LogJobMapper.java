package org.firzjb.joblog.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.joblog.entity.LogJob;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.joblog.entity.LogJob;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@MyBatisMapper
public interface LogJobMapper extends BaseMapper<LogJob> {

    int updateStatusToStop();
}
