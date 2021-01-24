package com.aofei.dataquality.service.impl;

import com.aofei.base.common.Const;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.Rule;
import com.aofei.dataquality.entity.RuleAttr;
import com.aofei.dataquality.mapper.RuleAttrMapper;
import com.aofei.dataquality.mapper.RuleMapper;
import com.aofei.dataquality.model.request.RuleAttrRequest;
import com.aofei.dataquality.model.request.RuleRequest;
import com.aofei.dataquality.model.response.RuleAttrResponse;
import com.aofei.dataquality.model.response.RuleResponse;
import com.aofei.dataquality.service.ExecuteCheckService;
import com.aofei.dataquality.service.IRuleService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据质量管理-规则管理 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-02
 */
@Service
public class RuleService extends BaseService<RuleMapper, Rule> implements IRuleService {


    @Autowired
    private ExecuteCheckService executeCheckService;


    @Autowired
    private RuleAttrMapper ruleAttrMapper;

    @Override
    public Page<RuleResponse> getPage(Page<Rule> page, RuleRequest request) {
        List<Rule> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, RuleResponse.class);
    }

    @Override
    public List<RuleResponse> getRules(RuleRequest request) {
        List<Rule> list = baseMapper.findList(request);
        return BeanCopier.copy(list,RuleResponse.class);
    }

    @Log(module = "数据质量管理",description = "修改状态")
    @Override
    public RuleResponse updateIsEnable(RuleRequest request) {
        Rule existing = selectById(request.getRuleId());
        if (existing != null) {
            existing.setIsEnable(request.getIsEnable());
            existing.preUpdate();
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, RuleResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Log(module = "数据质量管理",description = "新建数据质量规则")
    @Override
    public RuleResponse save(RuleRequest request) {
        int count =  baseMapper.selectCount(new EntityWrapper<Rule>()
                .eq("ORGANIZER_ID",request.getOrganizerId())
                .eq("DATABASE_ID",request.getDatabaseId())
                .eq("SCHEMA_NAME",request.getSchemaName())
                .eq("TABLE_NAME",request.getTableName())
                .eq("FIELD_NAME",request.getFieldName())
                .eq("RULE_TYPE",request.getRuleType())
                .eq("DEL_FLAG",Rule.DEL_FLAG_NORMAL));



        if(count == 0){
            Rule rule = BeanCopier.copy(request, Rule.class);
            rule.preInsert();
            super.insert(rule);

            for(RuleAttrRequest ruleAttrRequest: request.getAttr()){
                RuleAttr ruleAttr = BeanCopier.copy(ruleAttrRequest, RuleAttr.class);
                ruleAttr.setOrganizerId(request.getOrganizerId());
                ruleAttr.setRuleId(rule.getRuleId());
                ruleAttr.preInsert();
                ruleAttrMapper.insert(ruleAttr);
            }


            return BeanCopier.copy(rule, RuleResponse.class);
        }else{
            throw new ApplicationException(StatusCode.CONFLICT.getCode(), "当前字段规则类型已存在!");
        }

    }

    @Log(module = "数据质量管理",description = "修改数据质量规则")
    @Override
    public RuleResponse update(RuleRequest request) {

        int count =  baseMapper.selectCount(new EntityWrapper<Rule>()
                .ne("RULE_ID",request.getRuleId())
                .eq("ORGANIZER_ID",request.getOrganizerId())
                .eq("DATABASE_ID",request.getDatabaseId())
                .eq("SCHEMA_NAME",request.getSchemaName())
                .eq("TABLE_NAME",request.getTableName())
                .eq("FIELD_NAME",request.getFieldName())
                .eq("RULE_TYPE",request.getRuleType())
                .eq("DEL_FLAG",Rule.DEL_FLAG_NORMAL));

        if(count == 0){
            Rule existing = selectById(request.getRuleId());
            if (existing != null) {
                existing.setRuleGroupId(request.getRuleGroupId());
                existing.setRuleType(request.getRuleType());
                existing.setDatabaseId(request.getDatabaseId());
                existing.setSchemaName(request.getSchemaName());
                existing.setFieldName(request.getFieldName());
                existing.setFieldType(request.getFieldType());
                existing.setFieldOriginalType(request.getFieldOriginalType());
                existing.setTableName(request.getTableName());
                existing.setFieldName(request.getFieldName());
                existing.setDescription(request.getDescription());
                existing.setIsEnable(request.getIsEnable());
                existing.setCondition(request.getCondition());
                existing.preUpdate();
                super.insertOrUpdate(existing);

                ruleAttrMapper.deleteByRuleId(existing.getRuleId());

                for(RuleAttrRequest ruleAttrRequest: request.getAttr()){
                    RuleAttr ruleAttr = BeanCopier.copy(ruleAttrRequest, RuleAttr.class);
                    ruleAttr.setOrganizerId(request.getOrganizerId());
                    ruleAttr.setRuleId(existing.getRuleId());
                    ruleAttr.preInsert();
                    ruleAttr.preUpdate();
                    ruleAttrMapper.insert(ruleAttr);
                }

                return BeanCopier.copy(existing, RuleResponse.class);
            } else {
                //不存在
                throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
            }

        }else{
            throw new ApplicationException(StatusCode.CONFLICT.getCode(), "当前字段规则类型已存在!");
        }


    }
    @Log(module = "数据质量管理",description = "删除数据质量规则")
    @Override
    public int del(Long ruleId) {
        Rule existing = selectById(ruleId);
        if (existing != null) {
            super.deleteById(ruleId);
            ruleAttrMapper.deleteByRuleId(ruleId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



    @Override
    public RuleResponse get(Long ruleId) {
        Rule existing = selectById(ruleId);
        if(existing!=null){
            RuleResponse ruleResponse =  BeanCopier.copy(existing, RuleResponse.class);

            RuleAttrRequest ruleAttrRequest = new RuleAttrRequest();
            ruleAttrRequest.setRuleId(ruleId);
            ruleAttrRequest.setOrder("a.VALUE_RANK");
            ruleAttrRequest.setSort("asc");
            List<RuleAttr> ruleAttrs = ruleAttrMapper.findList(ruleAttrRequest);

            ruleResponse.setAttr(BeanCopier.copy(ruleAttrs, RuleAttrResponse.class));

            return ruleResponse;

        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public Boolean refresh(RuleRequest request, CurrentUserResponse user) {

        request.setIsEnable(Const.YES);
        List<Rule> list = baseMapper.findList(request);
        Map<Long,List<Rule>> map = new HashMap<>();

        for(Rule rule : list){
            List<Rule> rules = map.get(rule.getDatabaseId());
            if(rules==null){
                rules = new ArrayList<>();
                rules.add(rule);
                map.put(rule.getDatabaseId(),rules);
            }else{
                rules.add(rule);
            }
        }

        executeCheckService.refreshCheckResult(map,user);

        return true;
    }

}
