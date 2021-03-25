package org.firzjb.dataquality.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.dataquality.entity.CheckResultErr;
import org.firzjb.dataquality.mapper.CheckResultErrMapper;
import org.firzjb.dataquality.model.request.CheckResultErrRequest;
import org.firzjb.dataquality.model.response.CheckResultErrResponse;
import org.firzjb.dataquality.service.ICheckResultErrService;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.utils.BeanCopier;
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


    @Log(module = "数据质量管理",description = "查询错误字段值记录")
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
