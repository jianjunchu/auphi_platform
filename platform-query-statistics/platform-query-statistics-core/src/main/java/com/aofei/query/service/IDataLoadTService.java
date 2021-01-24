package com.aofei.query.service;

import com.alibaba.fastjson.JSONObject;
import com.aofei.query.model.request.DataLoadTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

public interface IDataLoadTService  {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, DataLoadTRequest request) throws KettleException, SQLException;

    Page getBackupFrequencyPage(Page pagination, DataLoadTRequest request) throws KettleException, SQLException;

    JSONObject getBackupRecordChartData(DataLoadTRequest request) throws KettleException, SQLException;
}
