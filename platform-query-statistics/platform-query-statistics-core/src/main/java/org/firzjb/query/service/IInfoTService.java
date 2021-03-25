package org.firzjb.query.service;

import org.firzjb.query.entity.InfoT;
import org.firzjb.query.model.request.InfoTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.query.model.request.InfoTRequest;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;

public interface IInfoTService  {

    /**
     * 获取 分页 列表
     * @param page
     * @param request
     * @return
     */
    Page getPage(Page page, InfoTRequest request) throws KettleException, SQLException;

}
