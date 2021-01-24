package com.aofei.query.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.query.model.request.DataLoadTRequest;
import com.aofei.query.service.IDataLoadTService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class DataLoadTService  implements IDataLoadTService {

    @Override
    public Page getPage(Page page, DataLoadTRequest request) throws KettleException, SQLException {


        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
        .append(" a.batch_no AS batchNo, a.unit_no AS unitNo, a.backup_start AS backupStart, a.backup_end AS backupEnd, a.backup_time AS backupTime, a.backup_count AS backupCount, a.backup_valid_count AS backupValidCount, a.backup_invalid_count AS backupInvalidCount, a.load_time AS loadTime, a.load_count AS loadCount, a.load_valid_count AS loadValidCount, a.load_invalid_count AS loadInvalidCount, a.business AS business, a.operation AS operation ")
        .append(" from s_data_load_t a where 1 = 1" );
        if(!StringUtils.isEmpty(request.getBatchNo())){
            sql.append(" and a.batch_no like '%").append(request.getBatchNo()).append("%'");
        }
        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
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
        return loader.getPage(page,sql.toString());


    }

    @Override
    public Page getBackupFrequencyPage(Page page, DataLoadTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
                .append("   a.unit_no AS unitNo, SUM(a.backup_count) AS backupCount, SUM(a.backup_valid_count) AS backupValidCount, SUM(a.backup_invalid_count) AS backupInvalidCount, SUM(a.load_count) AS loadCount, SUM(a.load_valid_count) AS loadValidCount, SUM(a.load_invalid_count) AS loadInvalidCount ")
                .append(" from s_data_load_t a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
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
        sql.append("  group by a.unit_no,a.load_time");
        return loader.getPage(page,sql.toString());

    }

    @Override
    public JSONObject getBackupRecordChartData(DataLoadTRequest request) throws KettleException, SQLException {

        JSONArray axis = new JSONArray();
        JSONArray series = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
                .append("    a.unit_no AS unitNo, count(1) AS backupCount ")
                .append(" from s_data_load_t a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
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

        sql.append("  group by a.unit_no,a.load_time");

        List<Map<String,Object>> list = loader.getList(sql.toString());


        for(Map<String,Object> dataLoadT : list){
            axis.add(dataLoadT.get("unitNo"));
            series.add(dataLoadT.get("backupCount"));
        }
        jsonObject.put("axis",axis);
        jsonObject.put("series",series);

        return jsonObject;
    }
}
