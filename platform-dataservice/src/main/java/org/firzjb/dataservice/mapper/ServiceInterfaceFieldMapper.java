package org.firzjb.dataservice.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.dataservice.entity.ServiceInterfaceField;
import org.firzjb.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.firzjb.dataservice.entity.ServiceInterfaceField;

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
