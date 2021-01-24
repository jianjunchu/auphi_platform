package com.aofei.query.service.impl;

import com.aofei.query.entity.BatchRevT;
import com.aofei.query.model.request.BatchRevTRequest;
import com.aofei.query.service.IBatchRevTService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * 一所数据查询
 * 数据接收记录表Service
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Service
public class BatchRevTService  implements IBatchRevTService {


    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page getPage(Page<BatchRevT> page, BatchRevTRequest request) throws KettleException, SQLException {

        DatabaseLoader  loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select a.batch_no AS batchNo , a.file_count AS fileCount , a.rev_time AS revTime from s_batch_rev_t a where 1 = 1" );//where a.batch_no like '%"+request.getBatchNo()+"%'";
        if(!StringUtils.isEmpty(request.getBatchNo())){
            sql.append(" and a.batch_no like '%").append(request.getBatchNo()).append("%'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.backup_time",request));

        }
        return loader.getPage(page,sql.toString());
    }
}
