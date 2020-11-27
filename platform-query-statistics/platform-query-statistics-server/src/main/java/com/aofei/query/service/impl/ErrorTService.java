package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.ErrorT;
import com.aofei.query.mapper.ErrorTMapper;
import com.aofei.query.model.request.ErrorTRequest;
import com.aofei.query.model.response.ErrorTResponse;
import com.aofei.query.service.IErrorTService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErrorTService extends BaseService<ErrorTMapper, ErrorT> implements IErrorTService {

    @Override
    public Page<ErrorTResponse> getPage(Page<ErrorT> page, ErrorTRequest request) {
        List<ErrorT> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, ErrorTResponse.class);
    }

}
