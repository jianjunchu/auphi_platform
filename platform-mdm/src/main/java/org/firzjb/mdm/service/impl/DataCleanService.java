package org.firzjb.mdm.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.mdm.entity.DataClean;
import org.firzjb.mdm.mapper.DataCleanMapper;
import org.firzjb.mdm.model.request.DataCleanRequest;
import org.firzjb.mdm.model.response.DataCleanResponse;
import org.firzjb.mdm.service.IDataCleanService;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.mdm.entity.DataClean;
import org.firzjb.mdm.mapper.DataCleanMapper;
import org.firzjb.mdm.model.request.DataCleanRequest;
import org.firzjb.mdm.model.response.DataCleanResponse;
import org.firzjb.mdm.service.IDataCleanService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据映射 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@Service
public class DataCleanService extends BaseService<DataCleanMapper, DataClean> implements IDataCleanService {

    @Override
    public Page<DataCleanResponse> getPage(Page<DataClean> page, DataCleanRequest request) {
        List<DataClean> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, DataCleanResponse.class);
    }

    @Override
    public List<DataCleanResponse> getDataCleans(DataCleanRequest request) {
        List<DataClean> list = baseMapper.findList(request);
        return BeanCopier.copy(list,DataCleanResponse.class);
    }

    @Log(module = "主数据管理",description = "新建数据映射信息")
    @Override
    public DataCleanResponse save(DataCleanRequest request) {
        DataClean dataClean = BeanCopier.copy(request, DataClean.class);
        dataClean.preInsert();
        super.insert(dataClean);
        return BeanCopier.copy(dataClean, DataCleanResponse.class);
    }

    @Log(module = "主数据管理",description = "修改数据映射信息")
    @Override
    public DataCleanResponse update(DataCleanRequest request) {
        DataClean existing = selectById(request.getId());
        if (existing != null) {
            existing.setModelId(request.getModelId());
            super.insertOrUpdate(existing);
            return BeanCopier.copy(existing, DataCleanResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
    @Log(module = "主数据管理",description = "删除数据映射信息")
    @Override
    public int del(Long deptId) {
        DataClean existing = selectById(deptId);
        if (existing != null) {
            super.deleteById(deptId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public DataCleanResponse get(Long deptId) {
        DataClean existing = selectById(deptId);
        if(existing!=null){
            return BeanCopier.copy(existing, DataCleanResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
