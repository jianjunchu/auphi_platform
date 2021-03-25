package org.firzjb.query.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.query.model.request.DataLoadTRequest;
import org.firzjb.query.service.IDataLoadTService;
import org.firzjb.query.utils.DatabaseLoader;
import org.firzjb.sys.model.request.OrganizerRequest;
import org.firzjb.sys.model.response.OrganizerResponse;
import org.firzjb.sys.service.IOrganizerService;
import org.firzjb.utils.StringUtils;
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

        String startTime =  request.getStartBackupTime();
        String endTime = request.getEndBackupTime();

        OrganizerRequest organizerRequest = new OrganizerRequest();
        organizerRequest.setParentId(1L);

        Map<String,JSONObject> maps = new HashMap<>();

        JSONObject res = new JSONObject();

        JSONObject geoCoordMap = new JSONObject();

        List<OrganizerResponse> organizers =  organizerService.getOrganizers(organizerRequest);

        for(OrganizerResponse response:organizers){
            JSONObject object = new JSONObject();
            object.put("name",response.getName());
            object.put("code",response.getCode());
            Object[] jsonArray = new Object[3];
            jsonArray[0] = response.getLongitude();
            jsonArray[1] = response.getLatitude();
            jsonArray[2] = 0;
            object.put("value",jsonArray);
            maps.put(response.getCode(),object);

        }

        String sql = "select unit_no,sum(backup_valid_count) AS backup_valid_count from s_data_load_t WHERE card_type = '"+request.getCardType()+"' AND backup_time > '"+startTime+"' AND backup_time < '"+endTime+"' GROUP BY unit_no";
        DatabaseLoader loader = null;
        try {
            loader = new DatabaseLoader();
            loader.getDb().connect();
            ResultSet rs =  loader.getDb().openQuery(sql);

            while(rs.next()) {
               String  unit_no = rs.getString("unit_no");
               if(maps.containsKey(unit_no)){
                   JSONObject object =maps.get(unit_no);
                   Object[] jsonArray = (Object[]) object.get("value");
                   jsonArray[2] = rs.getLong("backup_valid_count");
               }
            }
            rs.close();

            JSONArray data = new JSONArray();
            JSONArray data1 = new  JSONArray();

            for(JSONObject value : maps.values()){
                Object[] jsonArray = (Object[]) value.get("value");
                if(Long.valueOf(jsonArray[2].toString()) > 0){
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
