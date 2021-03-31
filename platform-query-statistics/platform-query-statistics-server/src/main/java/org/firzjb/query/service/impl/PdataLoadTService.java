package org.firzjb.query.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.query.model.request.DataLoadTRequest;
import org.firzjb.query.service.IPdataLoadTService;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdataLoadTService implements IPdataLoadTService {

    @Autowired
    private IOrganizerService organizerService;

    @Override
    public Page getPage(Page page, DataLoadTRequest request) throws KettleException, SQLException {


        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
        .append(" BATCH_NO,BATCH_TYPE,UNIT_NO,EXTRACT_FILE,FILE_SIZE,QD_NAME,EXTRACT_VALID_COUNT,LOAD_STATUS,EXTRACT_START,EXTRACT_END,MAX_ACCEPT_NO,EXTRACT_TIME,LOAD_TOTAL_COUNT,LOAD_VALID_COUNT,LOAD_INVALID_COUNT,DB_TYPE,IS_SUCCESS,INSERT_TIME ")
        .append(" from P_DATA_LOAD_T a where 1 = 1" );
        if(!StringUtils.isEmpty(request.getBatchNo())){
            sql.append(" and a.BATCH_NO like '%").append(request.getBatchNo()).append("%'");
        }
        if(!StringUtils.isEmpty(request.getUnitNo())){
           // sql.append(" and a.UNIT_NO = '").append(request.getUnitNo()).append("'");

            sql.append(" and a.UNIT_NO like '%").append(request.getUnitNo()).append("%'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && "EXTRACT_TIME".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.EXTRACT_TIME",request));

        }
        if(!StringUtils.isEmpty(request.getSearch_time())
                && "INSERT_TIME".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.INSERT_TIME",request));

        }
        return loader.getPage(page,sql.toString());


    }

    @Override
    public Page getBackupFrequencyPage(Page page, DataLoadTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
                .append("    ")
                .append(" from P_DATA_LOAD_T a where 1 = 1" );

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

        String startTime =  request.getStartBackupTime();
        String endTime = request.getEndBackupTime();

        OrganizerRequest organizerRequest = new OrganizerRequest();
        organizerRequest.setParentId(1L);

        Map<String,Object> maps = new HashMap<>();

        JSONObject res = new JSONObject();

        List<String> geoCoordMap = new ArrayList<>();

        List<OrganizerResponse> organizers =  organizerService.getOrganizers(organizerRequest);

        for(OrganizerResponse response:organizers){
            geoCoordMap.add(response.getName());
            maps.put(response.getCode(),0);

        }

        String sql = "select UNIT_NO,sum(1) AS LOAD_VALID_COUNT from P_DATA_LOAD_T WHERE DB_TYPE = '"+request.getCardType()+"' AND INSERT_TIME > '"+startTime+"' AND INSERT_TIME < '"+endTime+"' GROUP BY UNIT_NO";
        DatabaseLoader loader = null;
        try {
            loader = new DatabaseLoader();
            loader.getDb().connect();
            ResultSet rs =  loader.getDb().openQuery(sql);

            while(rs.next()) {
                String  unit_no = rs.getString("UNIT_NO");
                if(maps.containsKey(unit_no)){
                    maps.put(unit_no,rs.getLong("LOAD_VALID_COUNT"));
                }
            }
            rs.close();


            res.put("series",maps.values());
            res.put("axis",geoCoordMap);
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

    /**
     * 服务端地图装载日志
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
            object.put("name",response.getShortName());
            object.put("code",response.getCode());
            Object[] jsonArray = new Object[3];
            jsonArray[0] = response.getLongitude();
            jsonArray[1] = response.getLatitude();
            jsonArray[2] = 0;
            object.put("value",jsonArray);
            maps.put(response.getCode(),object);

        }

        String sql = "select UNIT_NO,sum(LOAD_VALID_COUNT) AS LOAD_VALID_COUNT from P_DATA_LOAD_T WHERE DB_TYPE = '"+request.getCardType()+"' AND INSERT_TIME > '"+startTime+"' AND INSERT_TIME < '"+endTime+"' GROUP BY unit_no";
        DatabaseLoader loader = null;
        try {
            loader = new DatabaseLoader();
            loader.getDb().connect();
            ResultSet rs =  loader.getDb().openQuery(sql);

            while(rs.next()) {
               String  unit_no = rs.getString("UNIT_NO");
               if(maps.containsKey(unit_no)){
                   JSONObject object =maps.get(unit_no);
                   Object[] jsonArray = (Object[]) object.get("value");
                   jsonArray[2] = rs.getLong("LOAD_VALID_COUNT");
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
