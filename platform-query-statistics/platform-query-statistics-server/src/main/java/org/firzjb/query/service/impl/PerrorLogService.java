package org.firzjb.query.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.query.model.request.PerrorTRequest;
import org.firzjb.query.service.IPerrorLogService;
import org.firzjb.query.utils.DatabaseLoader;
import org.firzjb.utils.StringUtils;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 错误数据查询
 */
@Service
public class PerrorLogService implements IPerrorLogService {


    /**
     * 制证端错误数据查询
     * @param page
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public Page getPage(Page page, PerrorTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();


        return loader.getPage(page, getSQl(request));



    }

    /**
     * 制证端错误数据查询
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> getList(PerrorTRequest request) throws KettleException, SQLException {
        DatabaseLoader loader = new DatabaseLoader();

        return loader.getList(getSQl(request));
    }




    /**
     * 制证端拼接sql
     *
     * @param request
     * @return
     */
    private String getSQl(PerrorTRequest request) {


        StringBuffer sql = new StringBuffer("select ")
                .append(" UNIT_NO , EXTRACT_FILE, ACCEPT_GROUP_NO, ERROR_CODE, ERROR_DESC, OPERATION, DEAL_STATUS, INSERT_TIME ")
                .append(" from P_ERROR_LOG a where 1 = 1");

        if (!StringUtils.isEmpty(request.getUnitNo())) {
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if (!StringUtils.isEmpty(request.getSearch_time())
                && "insert_date".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())) {
            sql.append(" AND a.INSERT_TIME between  '").append(request.getSearch_satrt()).append("' AND '").append(request.getSearch_end()).append("'");

        }

        return sql.toString();
    }

}
