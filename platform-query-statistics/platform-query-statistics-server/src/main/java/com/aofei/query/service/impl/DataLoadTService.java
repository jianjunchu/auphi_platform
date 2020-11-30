package com.aofei.query.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Override
    public JSONObject getBackupRecordChartData(DataLoadTRequest request) {

        JSONArray axis = new JSONArray();
        JSONArray series = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<DataLoadT> list = baseMapper.findBackupRecordChartDat(request);

        for(DataLoadT dataLoadT : list){
            axis.add(dataLoadT.getUnitNo());
            series.add(dataLoadT.getBackupCount());
        }
        jsonObject.put("axis",axis);
        jsonObject.put("series",series);

        return jsonObject;
    }
}
