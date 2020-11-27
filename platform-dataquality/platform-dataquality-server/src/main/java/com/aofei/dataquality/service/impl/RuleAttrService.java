package com.aofei.dataquality.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.RuleAttr;
import com.aofei.dataquality.mapper.RuleAttrMapper;
import com.aofei.dataquality.model.request.RuleAttrRequest;
import com.aofei.dataquality.service.IRuleAttrService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, String> getAttrMapByRuleId(Long ruleId) {
        RuleAttrRequest request = new RuleAttrRequest();
        request.setRuleId(ruleId);

        Map<String,String> map = new HashMap<>();


        List<RuleAttr> list = baseMapper.findList(request);

        if(list!= null && !list.isEmpty()){
            for(RuleAttr ruleAttr:list){
                map.put(ruleAttr.getCode(),ruleAttr.getValueStr());
            }
        }


        return map;
    }
}
