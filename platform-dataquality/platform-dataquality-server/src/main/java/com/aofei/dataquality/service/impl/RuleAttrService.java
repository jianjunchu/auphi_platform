package com.aofei.dataquality.service.impl;

import com.aofei.dataquality.entity.RuleAttr;
import com.aofei.dataquality.mapper.RuleAttrMapper;
import com.aofei.dataquality.service.IRuleAttrService;
import com.aofei.base.service.impl.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据质量管理-规则属性值 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
@Service
public class RuleAttrService extends BaseService<RuleAttrMapper, RuleAttr> implements IRuleAttrService {




    @Override
    public int deleteByRuleId(Long ruleId) {
        return baseMapper.deleteByRuleId(ruleId);
    }
}
