package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.RuleGroup;
import com.aofei.dataquality.model.request.RuleGroupRequest;
import com.aofei.dataquality.model.response.RuleGroupResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 数据质量规则分组 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
public interface IRuleGroupService extends IService<RuleGroup> {

    /**
     * 获取 Group 列表
     * @param page
     * @param request
     * @return
     */
    Page<RuleGroupResponse> getPage(Page<RuleGroup> page, RuleGroupRequest request);

    /**
     * 保存 Group 信息
     * @param request
     * @return
     */
    RuleGroupResponse save(RuleGroupRequest request);

    /**
     * 更新 Group 信息
     * @param request
     * @return
     */
    RuleGroupResponse update(RuleGroupRequest request);

    /**
     * 根据Id 查询 Group
     * @param deptId
     * @return
     */
    RuleGroupResponse get(Long deptId);
    /**
     * 根据Id 删除 Group
     * @param deptId
     * @return
     */
    int del(Long deptId);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<RuleGroupResponse> getRuleGroups(RuleGroupRequest request);
}
