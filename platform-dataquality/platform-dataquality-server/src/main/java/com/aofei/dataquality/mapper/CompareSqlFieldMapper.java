package com.aofei.dataquality.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.dataquality.entity.CompareSqlField;
import com.aofei.base.mapper.BaseMapper;
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
