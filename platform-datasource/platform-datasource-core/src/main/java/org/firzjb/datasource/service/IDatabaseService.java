package org.firzjb.datasource.service;

import org.firzjb.datasource.entity.DatabaseEntity;
import org.firzjb.datasource.model.request.DatabaseRequest;
import org.firzjb.datasource.model.response.DatabaseResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.datasource.entity.DatabaseEntity;
import org.firzjb.datasource.model.request.DatabaseRequest;
import org.firzjb.datasource.model.response.DatabaseResponse;
import org.pentaho.di.core.exception.KettleException;

import java.sql.SQLException;
import java.util.List;

/**
 * 本地数据库管理
 *
 * @auther 制证数据实时汇聚系统
 * @create 2018-10-21 21:49
 */

public interface IDatabaseService extends IService<DatabaseEntity> {

    /**
     * 分页查询本地数据库列表
     * @param page
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    Page<DatabaseResponse> getPage(Page<DatabaseEntity> page, DatabaseRequest request) throws KettleException, SQLException;

    /**
     * 查询本地数据库列表
     * @param request
     * @return
     */
    List<DatabaseResponse> getDatabases(DatabaseRequest request);
}
