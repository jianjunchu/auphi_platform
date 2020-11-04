package com.aofei.dataquality.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.dataquality.entity.RuleAttr;
import com.aofei.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数据质量管理-规则属性值 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
@MyBatisMapper
public interface RuleAttrMapper extends BaseMapper<RuleAttr> {


    int deleteByRuleId(@Param("ruleId") Long ruleId);
}
