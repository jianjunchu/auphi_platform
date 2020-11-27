package com.aofei.query.service;

import com.aofei.query.entity.InfoT;
import com.aofei.query.model.request.InfoTRequest;
import com.aofei.query.model.response.InfoTResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface IInfoTService extends IService<InfoT> {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page<InfoTResponse> getPage(Page<InfoT> page, InfoTRequest request);

}
