package org.firzjb.dataservice.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.dataservice.entity.ServiceAuth;
import org.firzjb.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.firzjb.dataservice.entity.ServiceAuth;

/**
 * <p>
 * 服务接口授权 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
@MyBatisMapper
public interface ServiceAuthMapper extends BaseMapper<ServiceAuth> {

    int deleteByServiceId(@Param("serviceId")Long serviceId);

    int deleteByUserId(@Param("userId")Long userId);
}
