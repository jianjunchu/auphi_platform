package org.firzjb.query.service;

import org.firzjb.query.entity.BatchRevT;
import org.firzjb.query.model.request.BatchRevTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.query.entity.BatchRevT;
import org.firzjb.query.model.request.BatchRevTRequest;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

/**
 * 一所数据查询
 * 数据接收记录表Service
 * @auther 制证数据实时汇聚系统
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
