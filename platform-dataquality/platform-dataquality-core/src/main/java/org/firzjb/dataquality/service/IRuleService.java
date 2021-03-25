package org.firzjb.dataquality.service;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.dataquality.entity.Rule;
import org.firzjb.dataquality.model.request.RuleRequest;
import org.firzjb.dataquality.model.response.RuleResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataquality.entity.Rule;
import org.firzjb.dataquality.model.request.RuleRequest;
import org.firzjb.dataquality.model.response.RuleResponse;

import java.util.List;

/**
 * <p>
 * 数据质量管理-规则管理 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-02
 */
public interface IRuleService extends IService<Rule> {


    /**
     * 获取 规则 列表
     * @param page
     * @param request
     * @return
     */
    Page<RuleResponse> getPage(Page<Rule> page, RuleRequest request);

    /**
     * 保存 规则 信息
     * @param request
     * @return
     */
    RuleResponse save(RuleRequest request);

    /**
     * 更新 规则 信息
     * @param request
     * @return
     */
    RuleResponse update(RuleRequest request);

    /**
     * 根据Id 查询 规则
     * @param ruleId
     * @return
     */
    RuleResponse get(Long ruleId);
    /**
     * 根据Id 删除 规则
     * @param ruleId
     * @return
     */
    int del(Long ruleId);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<RuleResponse> getRules(RuleRequest request);

    /**
     * 单独编辑数据质量规则是否启用状态
     * @param request
     * @return
     */
    RuleResponse updateIsEnable(RuleRequest request);

    Boolean refresh(RuleRequest request, CurrentUserResponse user);
}
