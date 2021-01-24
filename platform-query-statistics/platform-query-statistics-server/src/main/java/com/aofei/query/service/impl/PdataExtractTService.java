package com.aofei.query.service.impl;

import com.aofei.query.model.request.PdataExactTRequest;
import com.aofei.query.service.IPdataExtractTService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


/**
 * 制证端  数据抽取记录查询
 */
@Service
public class PdataExtractTService implements IPdataExtractTService {

    /**
     * 分页查询制证端  数据抽取记录查询
     * @param page
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public Page getPage(Page page, PdataExactTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();

        return loader.getPage(page,getSQl(request));
    }

    /**
     * 查询所有
     * @param request
     * @return
     * @throws KettleException
     * @throws SQLException
     */
    @Override
    public List getList(PdataExactTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();


        return loader.getList(getSQl(request));
    }

    /**
     * 拼接sql
     * @param request
     * @return
     */
    private String getSQl(PdataExactTRequest request){



        StringBuffer sql = new StringBuffer("select ")
                .append("  BATCH_NO,BATCH_TYPE,UNIT_NO,EXTRACT_FILE,EXTRACT_TOTAL_COUNT,EXTRACT_VALID_COUNT,EXTRACT_INVALID_COUNT,EXTRACT_START,EXTRACT_END,MAX_ACCEPT_NO,INSERT_TIME ")
                .append(" from P_DATA_EXTRACT_T a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && "insert_date".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND a.INSERT_DATE between  '").append(request.getSearch_satrt()).append("' AND '").append(request.getSearch_end()).append("'");

        }

        return sql.toString();
    }
}
