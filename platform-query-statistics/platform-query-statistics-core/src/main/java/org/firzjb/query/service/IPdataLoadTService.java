package org.firzjb.query.service;

import com.alibaba.fastjson.JSONObject;
import org.firzjb.query.model.request.DataLoadTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

/**
 * 数据装载日志表
 */
public interface IPdataLoadTService {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, DataLoadTRequest request) throws KettleException, SQLException;

    Page getBackupFrequencyPage(Page pagination, DataLoadTRequest request) throws KettleException, SQLException;

    JSONObject getBackupRecordChartData(DataLoadTRequest request) throws KettleException, SQLException;

    JSONObject get_sdata_loading_statistical(DataLoadTRequest request) throws KettleException, SQLException;
}
