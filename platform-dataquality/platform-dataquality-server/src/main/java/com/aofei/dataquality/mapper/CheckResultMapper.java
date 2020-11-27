package com.aofei.dataquality.mapper;

import com.aofei.base.annotation.MyBatisMapper;
import com.aofei.dataquality.entity.CheckResult;
import com.aofei.base.mapper.BaseMapper;
import com.aofei.dataquality.model.request.CheckResultRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据质量管理-规则检查结果 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@MyBatisMapper
public interface CheckResultMapper extends BaseMapper<CheckResult> {

    List<CheckResult> findRuleGroupResultList(CheckResultRequest request);

    int countCompliance(@Param("ruleGroupId") Long ruleGroupId);

    int countBreakRule1(@Param("ruleGroupId") Long ruleGroupId);

    int countBreakRule2(@Param("ruleGroupId") Long ruleGroupId);

    List<CheckResult> findCheckResults(@Param("ruleGroupId")Long ruleGroupId);
}
