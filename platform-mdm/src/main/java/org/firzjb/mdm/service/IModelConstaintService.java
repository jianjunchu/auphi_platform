package org.firzjb.mdm.service;

import org.firzjb.mdm.entity.ModelConstaint;
import org.firzjb.mdm.model.request.ModelConstaintRequest;
import org.firzjb.mdm.model.response.ModelConstaintResponse;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.mdm.entity.ModelConstaint;
import org.firzjb.mdm.model.request.ModelConstaintRequest;
import org.firzjb.mdm.model.response.ModelConstaintResponse;

import java.util.List;

/**
 * <p>
 * 主数据模型属性 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
public interface IModelConstaintService extends IService<ModelConstaint> {

    List<ModelConstaintResponse> getListByModelId(Long modelId);

    List<ModelConstaintResponse> getModelConstaints(ModelConstaintRequest request);

    ModelConstaintResponse save(ModelConstaintRequest request);

    ModelConstaintResponse update(ModelConstaintRequest request);

    int del(Long id);

    int delByAttributeId(Long attributeId);

    ModelConstaintResponse get(Long id);
}
