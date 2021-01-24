package com.aofei.query.service;

import com.aofei.query.model.request.DataExactTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

public interface IDataExactTService  {


    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, DataExactTRequest request) throws KettleException, SQLException;

}
