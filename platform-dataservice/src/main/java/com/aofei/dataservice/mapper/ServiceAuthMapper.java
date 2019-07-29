package com.aofei.dataservice.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.dataservice.entity.ServiceAuth;
import com.aofei.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
