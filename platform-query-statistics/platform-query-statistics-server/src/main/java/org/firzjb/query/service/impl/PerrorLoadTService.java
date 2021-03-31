package org.firzjb.query.service.impl;

import org.firzjb.query.model.request.SerrorTRequest;
import org.firzjb.query.service.IPerrorLoadTService;
import org.firzjb.query.utils.DatabaseLoader;
import org.firzjb.utils.StringUtils;
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
public class PerrorLoadTService implements IPerrorLoadTService {

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
                .append(" BATCH_NO,UNIT_NO,ACCEPT_NO,ERROR_CODE,ERROR_DESC,INSERT_TIME ")
                .append(" from P_ERROR_LOAD_T a where 1 = 1");

        if(!StringUtils.isEmpty(request.getAcceptNo())){

            sql.append(" and a.ACCEPT_NO like '%").append(request.getAcceptNo()).append("%'");
        }

        if(!StringUtils.isEmpty(request.getBatchNo())){

            sql.append(" and a.BATCH_NO like '%").append(request.getBatchNo()).append("%'");
        }

        if(!StringUtils.isEmpty(request.getUnitNo())){
            // sql.append(" and a.UNIT_NO = '").append(request.getUnitNo()).append("'");

            sql.append(" and a.UNIT_NO like '%").append(request.getUnitNo()).append("%'");
        }

        if (!StringUtils.isEmpty(request.getSearch_time())
                && "INSERT_TIME".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())) {
            sql.append(" AND a.INSERT_TIME between  '").append(request.getSearch_satrt()).append("' AND '").append(request.getSearch_end()).append("'");

        }

        return sql.toString();
    }

}
