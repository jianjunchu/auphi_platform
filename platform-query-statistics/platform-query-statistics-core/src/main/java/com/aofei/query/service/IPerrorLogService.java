package com.aofei.query.service;

import com.aofei.query.model.request.PerrorTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 制证端错误数据查询
 */
public interface IPerrorLogService {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, PerrorTRequest request) throws KettleException, SQLException;

    List<Map<String, Object>> getList(PerrorTRequest request) throws KettleException, SQLException;
}
