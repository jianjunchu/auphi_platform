package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.DataExactT;
import com.aofei.query.mapper.DataExactTMapper;
import com.aofei.query.model.request.DataExactTRequest;
import com.aofei.query.model.response.DataExactTResponse;
import com.aofei.query.service.IDataExactTService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataExactTService extends BaseService<DataExactTMapper, DataExactT> implements IDataExactTService {


    @Override
    public Page<DataExactTResponse> getPage(Page<DataExactT> page, DataExactTRequest request) {
        List<DataExactT> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, DataExactTResponse.class);
    }
}
