package org.firzjb.dataquality.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.dataquality.entity.RuleAttr;
import org.firzjb.base.mapper.BaseMapper;
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
