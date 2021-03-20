package com.aofei.query.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.exception.ApplicationException;
import com.aofei.query.model.request.DataLoadTRequest;
import com.aofei.query.service.IDataLoadTService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.sys.model.request.OrganizerRequest;
import com.aofei.sys.model.response.OrganizerResponse;
import com.aofei.sys.service.IOrganizerService;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataLoadTService  implements IDataLoadTService {

    @Autowired
    private IOrganizerService organizerService;

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

    /**
     * 地图装载日志
     * @param request
     * @return
     */
    @Override
    public JSONObject get_sdata_loading_statistical(DataLoadTRequest request) throws KettleException, SQLException {

        String startTime = request.getStartBackupTime()+"000000";
        String endTime = request.getEndBackupTime()+"235959";

        OrganizerRequest organizerRequest = new OrganizerRequest();
        organizerRequest.setParentId(1L);

        Map<String,JSONObject> maps = new HashMap<>();

        JSONObject res = new JSONObject();

        JSONObject geoCoordMap = new JSONObject();

        List<OrganizerResponse> organizers =  organizerService.getOrganizers(organizerRequest);

        for(OrganizerResponse response:organizers){
            JSONObject object = new JSONObject();
            object.put("name",response.getName());
            object.put("value",0);
            maps.put(response.getCode(),object);

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(response.getLatitude());
            jsonArray.add(response.getLongitude());
            geoCoordMap.put(response.getName(),jsonArray);

        }

        String sql = "select unit_no,sum(backup_valid_count) AS backup_valid_count from s_data_load_t WHERE card_type = 'L' AND backup_time > '"+startTime+"' AND backup_time < '"+endTime+"' GROUP BY unit_no";
        DatabaseLoader loader = null;
        try {
            loader = new DatabaseLoader();
            ResultSet rs =  loader.getDb().openQuery(sql);

            while(rs.next()) {
               String  unit_no = rs.getString("unit_no");
               if(maps.containsKey(unit_no)){
                   JSONObject object =maps.get(unit_no);
                   object.put("value",rs.getLong("backup_valid_count"));
               }
            }
            rs.close();

            JSONArray data = new JSONArray();
            JSONArray data1 = new  JSONArray();

            for(JSONObject value : maps.values()){
                if(value.getLong("value") > 0){
                    data.add(value);
                }else{
                    data1.add(value);
                }
            }
            res.put("data",data);
            res.put("data1",data1);
            res.put("geoCoordMap",geoCoordMap);
            return res;
        }catch (ApplicationException e){
            throw  e;
        }catch (Exception e){
            throw  e;

        }finally {
            if(loader!=null){
                loader.disconnect();
            }
        }

    }
}
