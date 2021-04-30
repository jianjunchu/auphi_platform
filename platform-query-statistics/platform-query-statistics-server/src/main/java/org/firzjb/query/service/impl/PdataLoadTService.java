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


        DatabaseLoader loader = new DatabaseLoader(request.getSystype());

        StringBuffer sql = new StringBuffer("SELECT ")
        .append(" a.UNIT_NO AS UNIT_NO , b.UNIT_NAME AS UNIT_NAME ,BATCH_NO,EXTRACT_TIME,EXTRACT_START,EXTRACT_END,DB_TYPE,INSERT_TIME, SUM(EXTRACT_VALID_COUNT) EXTRACT_NUM,SUM(LOAD_VALID_COUNT) LOAD_NUM,SUM(LOAD_INVALID_COUNT) LOAD_INVALID_NUM ")
        .append(" FROM P_DATA_LOAD_T  a LEFT JOIN P_UNIT_T B ON a.UNIT_NO = b.UNIT_NO WHERE EXTRACT_FILE LIKE '%his_card_t%' " );

        if( !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND a.EXTRACT_TIME >= '").append(request.getSearch_satrt()).append("'").append(" AND a.EXTRACT_TIME <= '").append(request.getSearch_end()).append("'");

        }

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and b.UNIT_NAME LIKE '%").append(request.getUnitNo()).append("%'");
        }

        sql.append( " GROUP BY a.UNIT_NO,BATCH_NO,EXTRACT_TIME,EXTRACT_START,EXTRACT_END,DB_TYPE,INSERT_TIME,b.UNIT_NAME ");
        sql.append( " ORDER BY a.UNIT_NO,INSERT_TIME DESC ");

        return loader.getPage(page,sql.toString());


    }

    @Override
    public Page getBackupFrequencyPage(Page page, DataLoadTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader(request.getSystype());

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

        Map<String,JSONObject> maps = new HashMap<>();

        JSONObject res = new JSONObject();


        List<OrganizerResponse> organizers =  organizerService.getOrganizers(organizerRequest);

        for(OrganizerResponse response:organizers){
            JSONObject item = new JSONObject();
            item.put("name",response.getName());
            item.put("value",0);
            maps.put(response.getCode(),item);
        }

        String sql = "select UNIT_NO,sum(1) AS LOAD_VALID_COUNT from P_DATA_LOAD_T WHERE  INSERT_TIME > '"+startTime+"' AND INSERT_TIME < '"+endTime+"' GROUP BY UNIT_NO";
        DatabaseLoader loader = null;
        try {
            loader = new DatabaseLoader(request.getSystype());
            loader.getDb().connect();
            ResultSet rs =  loader.getDb().openQuery(sql);

            while(rs.next()) {
                String  unit_no = rs.getString("UNIT_NO");
                if(maps.containsKey(unit_no)){
                    JSONObject item = maps.get(unit_no);
                    item.put("value",rs.getLong("LOAD_VALID_COUNT"));
                }
            }
            rs.close();

            List<String> series = new ArrayList<>();
            List<String> axis = new ArrayList<>();
            for(JSONObject item:maps.values()){
                series.add(item.getString("value"));
                axis.add(item.getString("name"));
            }

            res.put("series",series);
            res.put("axis",axis);
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
            loader = new DatabaseLoader(request.getSystype());
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
