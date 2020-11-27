package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.InfoT;
import com.aofei.query.mapper.InfoTMapper;
import com.aofei.query.model.request.InfoTRequest;
import com.aofei.query.model.response.InfoTResponse;
import com.aofei.query.service.IInfoTService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoTService extends BaseService<InfoTMapper, InfoT> implements IInfoTService {

    @Override
    public Page<InfoTResponse> getPage(Page<InfoT> page, InfoTRequest request) {
        List<InfoT> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, InfoTResponse.class);
    }
}
