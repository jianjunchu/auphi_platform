package com.aofei.dataservice.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.dataservice.entity.ServiceInterfaceField;
import com.aofei.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2019-07-27
 */
@MyBatisMapper
public interface ServiceInterfaceFieldMapper extends BaseMapper<ServiceInterfaceField> {

    int deleteByServiceId(@Param("serviceId")Long serviceId);

    List<ServiceInterfaceField> selectByServiceId(@Param("serviceId")Long serviceId);
}
