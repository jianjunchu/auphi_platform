package com.aofei.query.service;

import com.aofei.query.entity.ErrorT;
import com.aofei.query.model.request.ErrorTRequest;
import com.aofei.query.model.response.ErrorTResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface IErrorTService extends IService<ErrorT> {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page<ErrorTResponse> getPage(Page<ErrorT> page, ErrorTRequest request);

}
