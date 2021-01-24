package com.aofei.query.service;

import com.aofei.query.entity.BatchRevT;
import com.aofei.query.model.request.BatchRevTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

/**
 * 一所数据查询
 * 数据接收记录表Service
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public interface IBatchRevTService {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page<BatchRevT> page, BatchRevTRequest request) throws KettleException, SQLException;

}
