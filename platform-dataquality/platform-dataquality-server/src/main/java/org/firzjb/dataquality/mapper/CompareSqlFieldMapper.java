package org.firzjb.dataquality.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.dataquality.entity.CompareSqlField;
import org.firzjb.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@MyBatisMapper
public interface CompareSqlFieldMapper extends BaseMapper<CompareSqlField> {

    int deleteByCompareSqlId(@Param("compareSqlId")Long compareSqlId);
}
