package org.firzjb.datasource.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.datasource.entity.Hadoop;
import org.firzjb.datasource.mapper.HadoopMapper;
import org.firzjb.datasource.model.request.HadoopRequest;
import org.firzjb.datasource.model.response.HadoopResponse;
import org.firzjb.datasource.service.IHadoopService;
import org.firzjb.log.annotation.Log;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.datasource.mapper.HadoopMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * HADOOP管理 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@Service
public class HadoopService extends BaseService<HadoopMapper, Hadoop> implements IHadoopService {


    @Override
    public Page<HadoopResponse> getPage(Page<Hadoop> page, HadoopRequest request) {
        List<Hadoop> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, HadoopResponse.class);
    }

    @Override
    public List<HadoopResponse> getHadoops(HadoopRequest request) {
        List<Hadoop> list = baseMapper.findList(request);
        return BeanCopier.copy(list,HadoopResponse.class);
    }

    @Override
    @Log(module = "数据源管理", description = "新建Hadoop服务信息")
    public HadoopResponse save(HadoopRequest request) {
        Hadoop menu = BeanCopier.copy(request, Hadoop.class);
        menu.preInsert();
        super.insert(menu);
        return BeanCopier.copy(menu, HadoopResponse.class);
    }

    @Override
    @Log(module = "数据源管理", description = "修改Hadoop服务信息")
    public HadoopResponse update(HadoopRequest request) {
        Hadoop existing = selectById(request.getId());
        if (existing != null) {
            existing.setServer(request.getServer());
            existing.setPassword(request.getPassword());
            existing.setPort(request.getPort());

            existing.preUpdate();

            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, HadoopResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    @Log(module = "数据源管理", description = "删除Hadoop服务信息")
    public int del(Long id) {
        Hadoop existing = selectById(id);
        if (existing != null) {
            super.deleteById(id);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public HadoopResponse get(Long id) {

        Hadoop existing = selectById(id);
        if(existing!=null){
            return BeanCopier.copy(existing, HadoopResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }
}
