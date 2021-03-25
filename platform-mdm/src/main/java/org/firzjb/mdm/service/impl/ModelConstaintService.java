package org.firzjb.mdm.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.mdm.entity.ModelConstaint;
import org.firzjb.mdm.mapper.ModelConstaintMapper;
import org.firzjb.mdm.model.request.ModelConstaintRequest;
import org.firzjb.mdm.model.response.ModelConstaintResponse;
import org.firzjb.mdm.service.IModelConstaintService;
import org.firzjb.utils.BeanCopier;
import org.firzjb.mdm.entity.ModelConstaint;
import org.firzjb.mdm.mapper.ModelConstaintMapper;
import org.firzjb.mdm.model.request.ModelConstaintRequest;
import org.firzjb.mdm.model.response.ModelConstaintResponse;
import org.firzjb.mdm.service.IModelConstaintService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 主数据模型属性 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@Service
public class ModelConstaintService extends BaseService<ModelConstaintMapper, ModelConstaint> implements IModelConstaintService {

    @Override
    public List<ModelConstaintResponse> getListByModelId(Long modelId) {
        List<ModelConstaint> list = baseMapper.findListByModelId(modelId);

        return BeanCopier.copy(list, ModelConstaintResponse.class);
    }

    @Override
    public List<ModelConstaintResponse> getModelConstaints(ModelConstaintRequest request) {
        List<ModelConstaint> list = baseMapper.findList(request);
        return BeanCopier.copy(list, ModelConstaintResponse.class);
    }

    @Override
    public ModelConstaintResponse save(ModelConstaintRequest request) {
        ModelConstaint modelConstaint = BeanCopier.copy(request, ModelConstaint.class);
        modelConstaint.preInsert();
        super.insert(modelConstaint);
        return BeanCopier.copy(modelConstaint, ModelConstaintResponse.class);
    }

    @Override
    public ModelConstaintResponse update(ModelConstaintRequest request) {
        ModelConstaint existing = selectById(request.getConstaintId());
        if (existing != null) {

            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, ModelConstaintResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public int del(Long id) {
        ModelConstaint existing = selectById(id);
        if (existing != null) {
            super.deleteById(id);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public int delByAttributeId(Long attributeId) {
        return baseMapper.deleteByAttributeId(attributeId);
    }

    @Override
    public ModelConstaintResponse get(Long id) {
        ModelConstaint existing = selectById(id);
        if(existing!=null){
            return BeanCopier.copy(existing, ModelConstaintResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
