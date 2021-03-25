package org.firzjb.query.service;

import org.firzjb.query.model.request.DataExactTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.query.model.request.DataExactTRequest;
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
