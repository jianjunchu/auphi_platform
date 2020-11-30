package com.aofei.query.service;

import com.alibaba.fastjson.JSONObject;
import com.aofei.query.entity.DataLoadT;
import com.aofei.query.model.request.DataLoadTRequest;
import com.aofei.query.model.response.DataLoadTResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface IDataLoadTService extends IService<DataLoadT> {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page<DataLoadTResponse> getPage(Page<DataLoadT> page, DataLoadTRequest request);

    Page<DataLoadTResponse> getBackupFrequencyPage(Page<DataLoadT> pagination, DataLoadTRequest request);

    JSONObject getBackupRecordChartData(DataLoadTRequest request);
}
