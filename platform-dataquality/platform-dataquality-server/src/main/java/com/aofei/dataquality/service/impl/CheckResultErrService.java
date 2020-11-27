package com.aofei.dataquality.service.impl;

import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.dataquality.entity.CheckResultErr;
import com.aofei.dataquality.mapper.CheckResultErrMapper;
import com.aofei.dataquality.model.request.CheckResultErrRequest;
import com.aofei.dataquality.model.response.CheckResultErrResponse;
import com.aofei.dataquality.service.ICheckResultErrService;
import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据质量管理-错误字段值记录 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Service
public class CheckResultErrService extends BaseService<CheckResultErrMapper, CheckResultErr> implements ICheckResultErrService {


    @Override
    public Page<CheckResultErrResponse> getPage(Page<CheckResultErr> page, CheckResultErrRequest request) {
        List<CheckResultErr> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, CheckResultErrResponse.class);
    }

    @Override
    public List<CheckResultErrResponse> getCheckResultErrs(CheckResultErrRequest request) {
        List<CheckResultErr> list = baseMapper.findList(request);
        return BeanCopier.copy(list,CheckResultErrResponse.class);
    }



    @Log(module = "数据质量管理",description = "保存错误字段值记录")
    @Override
    public CheckResultErrResponse save(CheckResultErrRequest request) {

        CheckResultErr rule = BeanCopier.copy(request, CheckResultErr.class);
        rule.preInsert();
        super.insert(rule);

        return BeanCopier.copy(rule, CheckResultErrResponse.class);

    }

    @Log(module = "数据质量管理",description = "修改错误字段值记录")
    @Override
    public CheckResultErrResponse update(CheckResultErrRequest request) {

        CheckResultErr existing = selectById(request.getCheckResultErrId());
        if (existing != null) {

            existing.preUpdate();
            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, CheckResultErrResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }


    }
    @Log(module = "数据质量管理",description = "删除错误字段值记录")
    @Override
    public int del(Long ruleId) {
        CheckResultErr existing = selectById(ruleId);
        if (existing != null) {
            super.deleteById(ruleId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



    @Override
    public CheckResultErrResponse get(Long ruleId) {
        CheckResultErr existing = selectById(ruleId);
        if(existing!=null){
            CheckResultErrResponse ruleResponse =  BeanCopier.copy(existing, CheckResultErrResponse.class);

            return ruleResponse;

        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
