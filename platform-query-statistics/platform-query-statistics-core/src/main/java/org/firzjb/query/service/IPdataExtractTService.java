package org.firzjb.query.service;

import org.firzjb.query.model.request.PdataExactTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.query.model.request.PdataExactTRequest;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;
import java.util.List;

/**
 * 制证端  数据抽取记录查询
 */
public interface IPdataExtractTService {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, PdataExactTRequest request) throws KettleException, SQLException;

    List getList(PdataExactTRequest request) throws KettleException, SQLException;
}
