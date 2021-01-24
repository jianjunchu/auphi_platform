package com.aofei.query.service;

import com.aofei.query.entity.InfoT;
import com.aofei.query.model.request.InfoTRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
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
