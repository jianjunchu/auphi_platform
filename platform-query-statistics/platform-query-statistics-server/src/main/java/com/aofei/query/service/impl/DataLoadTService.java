package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.DataLoadT;
import com.aofei.query.mapper.DataLoadTMapper;
import com.aofei.query.model.request.DataLoadTRequest;
import com.aofei.query.model.response.DataLoadTResponse;
import com.aofei.query.service.IDataLoadTService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoadTService extends BaseService<DataLoadTMapper, DataLoadT> implements IDataLoadTService {

    @Override
    public Page<DataLoadTResponse> getPage(Page<DataLoadT> page, DataLoadTRequest request) {
        List<DataLoadT> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, DataLoadTResponse.class);
    }

    @Override
    public Page<DataLoadTResponse> getBackupFrequencyPage(Page<DataLoadT> page, DataLoadTRequest request) {
        List<DataLoadT> list = baseMapper.findBackupFrequencyList(page, request);
        page.setRecords(list);
        return convert(page, DataLoadTResponse.class);
    }
}
