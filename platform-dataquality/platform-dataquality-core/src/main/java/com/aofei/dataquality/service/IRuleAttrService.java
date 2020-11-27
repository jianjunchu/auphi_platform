package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.RuleAttr;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 * 数据质量管理-规则属性值 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
public interface IRuleAttrService extends IService<RuleAttr> {

    int deleteByRuleId(Long ruleId);

    Map<String, String> getAttrMapByRuleId(Long ruleId);
}
