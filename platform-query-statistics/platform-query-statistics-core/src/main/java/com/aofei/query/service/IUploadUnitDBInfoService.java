package com.aofei.query.service;

import com.aofei.query.entity.UploadUnitDBInfo;
import com.aofei.query.model.request.UploadUnitDBInfoRequest;
import com.aofei.query.model.response.UploadUnitDBInfoResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface IUploadUnitDBInfoService extends IService<UploadUnitDBInfo> {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page<UploadUnitDBInfoResponse> getPage(Page<UploadUnitDBInfo> page, UploadUnitDBInfoRequest request);

}
