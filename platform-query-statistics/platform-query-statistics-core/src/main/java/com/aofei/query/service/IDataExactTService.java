package com.aofei.query.service;

import com.aofei.query.entity.DataExactT;
import com.aofei.query.model.request.DataExactTRequest;
import com.aofei.query.model.response.DataExactTResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface IDataExactTService extends IService<DataExactT> {


    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page<DataExactTResponse> getPage(Page<DataExactT> page, DataExactTRequest request);

}
