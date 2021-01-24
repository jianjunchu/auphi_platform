package com.aofei.dataquality.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.CompareSql;
import com.aofei.dataquality.entity.CompareSqlField;
import com.aofei.dataquality.mapper.CompareSqlFieldMapper;
import com.aofei.dataquality.mapper.CompareSqlMapper;
import com.aofei.dataquality.model.request.CompareSqlFieldRequest;
import com.aofei.dataquality.model.request.CompareSqlRequest;
import com.aofei.dataquality.model.response.CompareSqlFieldResponse;
import com.aofei.dataquality.model.response.CompareSqlResponse;
import com.aofei.dataquality.service.ICompareSqlService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Service
public class CompareSqlService extends BaseService<CompareSqlMapper, CompareSql> implements ICompareSqlService {

    @Autowired
    private CompareSqlFieldMapper compareSqlFieldMapper;


    @Log(module = "数据质量管理",description = "新建数据质量规则")
    @Override
    public CompareSqlResponse save(CompareSqlRequest request) {

        CompareSql rule = BeanCopier.copy(request, CompareSql.class);
        rule.preInsert();
        super.insert(rule);

        for(CompareSqlFieldRequest fieldRequest: request.getFields()){
            CompareSqlField field = BeanCopier.copy(fieldRequest, CompareSqlField.class);
            field.setCompareSqlId(rule.getCompareSqlId());
            field.preInsert();
            compareSqlFieldMapper.insert(field);
        }


        return BeanCopier.copy(rule, CompareSqlResponse.class);
    }

    @Log(module = "数据质量管理",description = "修改数据质量规则")
    @Override
    public CompareSqlResponse update(CompareSqlRequest request) {

        CompareSql existing = selectById(request.getCompareSqlId());
        if (existing != null) {
            existing.setRuleGroupId(request.getRuleGroupId());
            existing.setCompareDesc(request.getCompareDesc());
            existing.setDatabaseId(request.getDatabaseId());
            existing.setRefDatabaseId(request.getRefDatabaseId());
            existing.setSql(request.getSql());
            existing.setRefSql(request.getRefSql());
            existing.preUpdate();
            super.insertOrUpdate(existing);

            compareSqlFieldMapper.deleteByCompareSqlId(existing.getCompareSqlId());

            for(CompareSqlFieldRequest fieldRequest: request.getFields()){
                CompareSqlField field = BeanCopier.copy(fieldRequest, CompareSqlField.class);
                field.setCompareSqlId(existing.getCompareSqlId());
                field.preInsert();
                field.preUpdate();
                compareSqlFieldMapper.insert(field);
            }

            return BeanCopier.copy(existing, CompareSqlResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }

    }
    @Log(module = "数据质量管理",description = "删除数据质量规则")
    @Override
    public int del(Long compareSqlId) {
        CompareSql existing = selectById(compareSqlId);
        if (existing != null) {
            super.deleteById(compareSqlId);
            compareSqlFieldMapper.deleteByCompareSqlId(compareSqlId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public List<CompareSqlResponse> getCompareSqls(CompareSqlRequest request) {
        List<CompareSql> list = baseMapper.findList(request);
        return BeanCopier.copy(list, CompareSqlResponse.class);
    }


    @Log(module = "数据质量管理",description = "查询数据质量规则详情")
    @Override
    public CompareSqlResponse get(Long compareSqlId) {
        CompareSql existing = selectById(compareSqlId);
        if(existing!=null){
            CompareSqlResponse ruleResponse =  BeanCopier.copy(existing, CompareSqlResponse.class);

            CompareSqlFieldRequest fieldRequest = new CompareSqlFieldRequest();
            fieldRequest.setCompareSqlId(compareSqlId);
            fieldRequest.setOrder("a.VALUE_RANK");
            fieldRequest.setSort("asc");
            List<CompareSqlField> fields = compareSqlFieldMapper.findList(fieldRequest);

            ruleResponse.setFields(BeanCopier.copy(fields, CompareSqlFieldResponse.class));

            return ruleResponse;

        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
