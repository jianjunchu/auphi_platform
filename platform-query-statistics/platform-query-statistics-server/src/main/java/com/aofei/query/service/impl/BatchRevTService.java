package com.aofei.query.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.query.entity.BatchRevT;
import com.aofei.query.mapper.BatchRevTMapper;
import com.aofei.query.model.request.BatchRevTRequest;
import com.aofei.query.model.response.BatchRevTResponse;
import com.aofei.query.service.IBatchRevTService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 一所数据查询
 * 数据接收记录表Service
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Service
public class BatchRevTService extends BaseService<BatchRevTMapper, BatchRevT> implements IBatchRevTService {


    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page<BatchRevTResponse> getPage(Page<BatchRevT> page, BatchRevTRequest request) {
        List<BatchRevT> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, BatchRevTResponse.class);
    }
}
