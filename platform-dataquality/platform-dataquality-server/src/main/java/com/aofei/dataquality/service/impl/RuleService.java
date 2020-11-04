package com.aofei.dataquality.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.dataquality.entity.Rule;
import com.aofei.dataquality.i18n.Messages;
import com.aofei.dataquality.mapper.RuleMapper;
import com.aofei.dataquality.model.request.RuleRequest;
import com.aofei.dataquality.model.response.RuleResponse;
import com.aofei.dataquality.service.IRuleService;
import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Log(module = "数据质量管理",description = "单独编辑数据质量规则是否启用状态")
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
            Rule Rule = BeanCopier.copy(request, Rule.class);
            Rule.preInsert();
            super.insert(Rule);
            return BeanCopier.copy(Rule, RuleResponse.class);
        }else{
            throw new ApplicationException(StatusCode.CONFLICT.getCode(), Messages.getString("DataQuality.Error.FieldNameExist ",request.getFieldName()));
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
                existing.setTableName(request.getTableName());
                existing.setFieldName(request.getFieldName());
                existing.setDescription(request.getDescription());
                existing.setIsEnable(request.getIsEnable());
                existing.preUpdate();
                super.insertOrUpdate(existing);
                return BeanCopier.copy(existing, RuleResponse.class);
            } else {
                //不存在
                throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
            }

        }else{
            throw new ApplicationException(StatusCode.CONFLICT.getCode(), Messages.getString("DataQuality.Error.FieldNameExist ",request.getFieldName()));
        }


    }
    @Log(module = "数据质量管理",description = "删除数据质量规则")
    @Override
    public int del(Long ruleId) {
        Rule existing = selectById(ruleId);
        if (existing != null) {
            super.deleteById(ruleId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



    @Override
    public RuleResponse get(Long deptId) {
        Rule existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, RuleResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
