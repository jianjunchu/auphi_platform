package com.aofei.dataquality.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.common.Const;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.CheckResult;
import com.aofei.dataquality.entity.CheckResultErr;
import com.aofei.dataquality.entity.Rule;
import com.aofei.dataquality.entity.RuleGroup;
import com.aofei.dataquality.mapper.CheckResultErrMapper;
import com.aofei.dataquality.mapper.CheckResultMapper;
import com.aofei.dataquality.mapper.RuleGroupMapper;
import com.aofei.dataquality.mapper.RuleMapper;
import com.aofei.dataquality.model.request.CheckResultErrRequest;
import com.aofei.dataquality.model.request.CheckResultRequest;
import com.aofei.dataquality.model.response.CheckResultResponse;
import com.aofei.dataquality.service.ExecuteCheckService;
import com.aofei.dataquality.service.ICheckResultService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据质量管理-规则检查结果 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Service
public class CheckResultService extends BaseService<CheckResultMapper, CheckResult> implements ICheckResultService {


    @Autowired
    private CheckResultErrMapper checkResultErrMapper;

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private RuleGroupMapper ruleGroupMapper;

    @Autowired
    private ExecuteCheckService executeCheckService;

    @Override
    public Page<CheckResultResponse> getPage(Page<CheckResult> page, CheckResultRequest request) {
        List<CheckResult> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, CheckResultResponse.class);
    }

    @Override
    public List<CheckResultResponse> getCheckResults(CheckResultRequest request) {
        List<CheckResult> list = baseMapper.findList(request);
        return BeanCopier.copy(list,CheckResultResponse.class);
    }

    @Override
    public List<CheckResultResponse> getRuleGroupResultList(CheckResultRequest request) {

        List<CheckResult> list = baseMapper.findRuleGroupResultList(request);

        if(list==null || list.isEmpty()){
            List<RuleGroup> ruleGroups = ruleGroupMapper.selectList(new EntityWrapper<RuleGroup>()
                    .eq("DEL_FLAG",0).eq("ORGANIZER_ID",request.getOrganizerId()));
            list = new ArrayList<>();
            for(RuleGroup ruleGroup : ruleGroups){
                CheckResult checkResult = new CheckResult();

                checkResult.setRuleGroupId(ruleGroup.getGroupId().toString());
                checkResult.setRuleGroupName(ruleGroup.getGroupName());
                checkResult.setNotPassedNum(0L);
                checkResult.setPassedNum(0L);
                checkResult.setTotalCheckedNum(0L);
                list.add(checkResult);
            }
        }

        return BeanCopier.copy(list,CheckResultResponse.class);
    }

    /**
     * 获取违规数据统计
     * @param ruleGroupId
     * @return
     */
    @Override
    public JSONArray getBreakRuleChartDataByGroupId(Long ruleGroupId) {

        JSONArray jsonArray = new JSONArray();

        int countCompliance = baseMapper.countCompliance(ruleGroupId);
        int countBreakRule1 = baseMapper.countBreakRule1(ruleGroupId);
        int countBreakRule2 = baseMapper.countBreakRule2(ruleGroupId);

        JSONObject map = new JSONObject();
        map.put("value",countBreakRule2);
        map.put("name","违反一条以上规则");
        jsonArray.add(map);

        JSONObject map2 = new JSONObject();
        map2.put("value",countBreakRule1);
        map2.put("name","违反一条规则");
        jsonArray.add(map2);

        JSONObject map3 = new JSONObject();
        map3.put("value",countCompliance);
        map3.put("name","合规数据");
        jsonArray.add(map3);

        return jsonArray;
    }

    /**
     * 获取分组下不违反规则最多的前3个规则数据
     * @param ruleGroupId
     * @return
     */
    @Override
    public JSONArray getCheckResultChartDataByGroupId(Long ruleGroupId) {

        JSONArray jsonArray = new JSONArray();

        List<CheckResult> list = baseMapper.findCheckResults(ruleGroupId);

        if(list==null || list.isEmpty()){
            list = new ArrayList<>();
            List<Rule> rules = ruleMapper.selectList(new EntityWrapper<Rule>()
                    .eq("RULE_GROUP_ID",ruleGroupId).eq("DEL_FLAG",0));
            for(Rule rule : rules){
                CheckResult result = new CheckResult();
                result.setRuleId(rule.getRuleId());
                result.setDescription(rule.getDescription());
                result.setTotalCheckedNum(0L);
                result.setNotPassedNum(0L);
                result.setPassedNum(0L);
                list.add(result);
            }
        }

        for(CheckResult result : list){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ruleId",result.getRuleId().toString());
            jsonObject.put("description",result.getDescription());

            JSONArray datas = new JSONArray();

            JSONObject map3 = new JSONObject();
            map3.put("value",result.getNotPassedNum());
            map3.put("ruleId",result.getRuleId().toString());
            map3.put("name","不合规数据");
            datas.add(map3);

            JSONObject map2 = new JSONObject();
            map2.put("value",result.getPassedNum());
            map2.put("ruleId",result.getRuleId().toString());
            map2.put("name","合规数据");
            datas.add(map2);




            jsonObject.put("datas",datas);

            jsonArray.add(jsonObject);
        }


        return jsonArray;
    }

    @Override
    public Integer getRefreshStatus(Long userId) {
        return executeCheckService.getThreadStatus(userId);
    }


    @Override
    public CheckResultResponse save(CheckResultRequest request) {

        CheckResult rule = BeanCopier.copy(request, CheckResult.class);
        rule.setCreateTime(new Date());
        rule.setUpdateTime(rule.getCreateTime());
        rule.setDelFlag(Const.NO);
        super.insert(rule);

        if(request.getErrs()!=null && !request.getErrs().isEmpty()){
            for(CheckResultErrRequest err : request.getErrs()){
                CheckResultErr checkResultErr = BeanCopier.copy(err, CheckResultErr.class);
                checkResultErr.setCheckResultId(rule.getCheckResultId());
                checkResultErr.setCreateUser(rule.getCreateUser());
                checkResultErr.setUpdateUser(rule.getUpdateUser());

                checkResultErr.setCreateTime(new Date());
                checkResultErr.setUpdateTime(checkResultErr.getCreateTime());
                checkResultErr.setDelFlag(Const.NO);
                checkResultErrMapper.insert(checkResultErr);
            }
        }

        return BeanCopier.copy(rule, CheckResultResponse.class);

    }

    @Log(module = "数据质量管理",description = "修改规则检查结果")
    @Override
    public CheckResultResponse update(CheckResultRequest request) {

        CheckResult existing = selectById(request.getCheckResultId());
        if (existing != null) {

            existing.preUpdate();
            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, CheckResultResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }


    }
    @Log(module = "数据质量管理",description = "删除规则检查结果")
    @Override
    public int del(Long checkResultId) {
        CheckResult existing = selectById(checkResultId);
        if (existing != null) {
            super.deleteById(checkResultId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



    @Override
    public CheckResultResponse get(Long checkResultId) {
        CheckResult existing = selectById(checkResultId);
        if(existing!=null){
            CheckResultResponse ruleResponse =  BeanCopier.copy(existing, CheckResultResponse.class);

            return ruleResponse;

        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }


}
