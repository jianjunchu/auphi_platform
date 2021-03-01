package com.aofei.query.service.impl;

import com.aofei.query.model.request.SerrorTRequest;
import com.aofei.query.service.ISerrorTService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 服务器错误数据查询
 */
@Service
public class SerrorTService implements ISerrorTService {

    /**
     * 分页查询
     * @param page
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public Page getPage(Page page, SerrorTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();


        return loader.getPage(page,getSQl(request,loader));


    }

    /**
     * 查询列表
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> getList(SerrorTRequest request) throws KettleException, SQLException {
        DatabaseLoader loader = new DatabaseLoader();


        return loader.getList(getSQl(request,loader));
    }

    /**
     * 拼接sql
     * @param request
     * @param loader
     * @return
     */
    private String getSQl(SerrorTRequest request, DatabaseLoader loader){




        StringBuffer sql = new StringBuffer("select ")
                .append("   a.accept_no AS acceptNo, a.unit_no AS unitNo, a.backup_file AS backupFile, a.error_code AS errorCode, a.error_desc AS errorDesc, a.curr_operation AS currOperation, a.deal_flag AS dealFlag, a.create_time AS createTime, a.update_time AS updateTime ")
                .append(" from s_error_t a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getAcceptNo())){
            sql.append(" and a.accept_no = '").append(request.getAcceptNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && "backup_time".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.backup_time",request));

        }
        if(!StringUtils.isEmpty(request.getSearch_time())
                && "load_time".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.load_time",request));

        }

        return sql.toString();
    }

}
