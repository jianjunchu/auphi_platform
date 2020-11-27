package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.UploadUnitDBInfo;
import com.aofei.query.mapper.UploadUnitDBInfoMapper;
import com.aofei.query.model.request.UploadUnitDBInfoRequest;
import com.aofei.query.model.response.UploadUnitDBInfoResponse;
import com.aofei.query.service.IUploadUnitDBInfoService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadUnitDBInfoService extends BaseService<UploadUnitDBInfoMapper, UploadUnitDBInfo> implements IUploadUnitDBInfoService {

    @Override
    public Page<UploadUnitDBInfoResponse> getPage(Page<UploadUnitDBInfo> page, UploadUnitDBInfoRequest request) {
        List<UploadUnitDBInfo> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, UploadUnitDBInfoResponse.class);
    }
}
