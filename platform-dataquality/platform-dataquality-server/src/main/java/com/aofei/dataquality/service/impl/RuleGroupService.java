package com.aofei.dataquality.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.RuleGroup;
import com.aofei.dataquality.i18n.Messages;
import com.aofei.dataquality.mapper.RuleGroupMapper;
import com.aofei.dataquality.model.request.RuleGroupRequest;
import com.aofei.dataquality.model.response.RuleGroupResponse;
import com.aofei.dataquality.service.IRuleGroupService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据质量规则分组 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
@Service
public class RuleGroupService extends BaseService<RuleGroupMapper, RuleGroup> implements IRuleGroupService {

    /**
     * 分页返回数据质量规则分组
     * @param request
     * @return
     */
    @Log(module = "数据质量管理",description = "数据质量规则查询")
    @Override
    public Page<RuleGroupResponse> getPage(Page<RuleGroup> page, RuleGroupRequest request) {
        List<RuleGroup> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, RuleGroupResponse.class);
    }

    /**
     * 返回所有数据质量规则分组
     * @param request
     * @return
     */
    @Override
    public List<RuleGroupResponse> getRuleGroups(RuleGroupRequest request) {
        List<RuleGroup> list = baseMapper.findList(request);
        return BeanCopier.copy(list,RuleGroupResponse.class);
    }

    /**
     * 新建数据质量规则分组
     * @param request
     * @return
     */
    @Log(module = "数据质量管理",description = "新建数据质量规则分组")
    @Override
    public RuleGroupResponse save(RuleGroupRequest request) {
        int count =  baseMapper.selectCount(new EntityWrapper<RuleGroup>()
                .eq("GROUP_NAME",request.getGroupName())
                .eq("ORGANIZER_ID",request.getOrganizerId())
                .eq("DEL_FLAG",RuleGroup.DEL_FLAG_NORMAL));
        if(count == 0){
            RuleGroup ruleGroup = BeanCopier.copy(request, RuleGroup.class);
            ruleGroup.preInsert();
            super.insert(ruleGroup);
            return BeanCopier.copy(ruleGroup, RuleGroupResponse.class);
        }else{
            throw new ApplicationException(StatusCode.CONFLICT.getCode(), Messages.getString("DataQuality.Error.RuleGroupExist ",request.getGroupName()));
        }

    }

    /**
     * 修改数据质量规则分组
     * @param request
     * @return
     */
    @Log(module = "数据质量管理",description = "修改数据质量规则分组")
    @Override
    public RuleGroupResponse update(RuleGroupRequest request) {
        RuleGroup existing = selectById(request.getGroupId());
        if (existing != null) {
            existing.setGroupName(request.getGroupName());
            existing.setDescription(request.getDescription());
            existing.preUpdate();
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, RuleGroupResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    /**
     * 删除数据质量规则分组
     * @param deptId
     * @return
     */
    @Log(module = "数据质量管理",description = "删除数据质量规则分组")
    @Override
    public int del(Long deptId) {
        RuleGroup existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    /**
     * 数据质量规则详情
     * @param deptId
     * @return
     */
    @Log(module = "数据质量管理",description = "数据质量规则详情")
    @Override
    public RuleGroupResponse get(Long deptId) {
        RuleGroup existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, RuleGroupResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
