package org.firzjb.dataquality.service;

import com.alibaba.fastjson.JSONArray;
import org.firzjb.dataquality.entity.CheckResult;
import org.firzjb.dataquality.model.request.CheckResultRequest;
import org.firzjb.dataquality.model.response.CheckResultResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataquality.entity.CheckResult;
import org.firzjb.dataquality.model.request.CheckResultRequest;
import org.firzjb.dataquality.model.response.CheckResultResponse;

import java.util.List;

/**
 * <p>
 * 数据质量管理-规则检查结果检查结果 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
public interface ICheckResultService extends IService<CheckResult> {

    /**
     * 获取 规则检查结果 列表
     * @param page
     * @param request
     * @return
     */
    Page<CheckResultResponse> getPage(Page<CheckResult> page, CheckResultRequest request);

    /**
     * 保存 规则检查结果 信息
     * @param request
     * @return
     */
    CheckResultResponse save(CheckResultRequest request);

    /**
     * 更新 规则检查结果 信息
     * @param request
     * @return
     */
    CheckResultResponse update(CheckResultRequest request);

    /**
     * 根据Id 查询 规则检查结果
     * @param checkResultId
     * @return
     */
    CheckResultResponse get(Long checkResultId);
    /**
     * 根据Id 删除 规则检查结果
     * @param checkResultId
     * @return
     */
    int del(Long checkResultId);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<CheckResultResponse> getCheckResults(CheckResultRequest request);


    List<CheckResultResponse> getRuleGroupResultList(CheckResultRequest request);

    /**
     * 获取违规数据统计
     * @param ruleGroupId
     * @return
     */
    JSONArray getBreakRuleChartDataByGroupId(Long ruleGroupId);

    /**
     * 获取分组下不违反规则最多的前3个规则数据
     * @param ruleGroupId
     * @return
     */
    JSONArray getCheckResultChartDataByGroupId(Long ruleGroupId);

    /**
     * 获取数据稽核状态
     * @param userId
     * @return
     */
    Integer getRefreshStatus(Long userId);
}
