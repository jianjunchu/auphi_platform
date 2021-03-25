package org.firzjb.dataquality.service.impl;

import org.firzjb.base.service.impl.BaseService;
import org.firzjb.dataquality.entity.CompareSql;
import org.firzjb.dataquality.entity.CompareSqlField;
import org.firzjb.dataquality.entity.CompareSqlResult;
import org.firzjb.dataquality.mapper.CompareSqlFieldMapper;
import org.firzjb.dataquality.mapper.CompareSqlMapper;
import org.firzjb.dataquality.mapper.CompareSqlResultMapper;
import org.firzjb.dataquality.model.request.CompareSqlFieldRequest;
import org.firzjb.dataquality.model.request.CompareSqlResultRequest;
import org.firzjb.dataquality.model.response.CompareSqlResultResponse;
import org.firzjb.dataquality.service.ICompareSqlResultService;
import org.firzjb.kettle.App;
import org.firzjb.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.log4j.Log4j;
import org.firzjb.dataquality.mapper.CompareSqlFieldMapper;
import org.firzjb.dataquality.mapper.CompareSqlMapper;
import org.firzjb.dataquality.mapper.CompareSqlResultMapper;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Log4j
@Service
public class CompareSqlResultService extends BaseService<CompareSqlResultMapper, CompareSqlResult> implements ICompareSqlResultService {

    @Autowired
    private CompareSqlMapper compareSqlMapper;

    @Autowired
    private CompareSqlFieldMapper compareSqlFieldMapper;

    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("CompareSqlResultService", LoggingObjectType.DATABASE, null);


    @Override
    public Page<CompareSqlResultResponse> getPage(Page<CompareSqlResult> page, CompareSqlResultRequest request) {
        List<CompareSqlResult> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, CompareSqlResultResponse.class);
    }

    @Override
    public boolean execCompareSql(Long compareSqlId)  {

        CompareSql compareSql = compareSqlMapper.selectById(compareSqlId);

        CompareSqlFieldRequest fieldRequest = new CompareSqlFieldRequest();
        fieldRequest.setCompareSqlId(compareSqlId);
        List<CompareSqlField> fields = compareSqlFieldMapper.findList(fieldRequest);

        Repository repository = App.getInstance().getRepository();

        ResultSet resultSet = null;
        ResultSet refResultSet = null;
        Database testDatabase = null;
        Database refDatabase = null;

        ObjectId database_id = new LongObjectId(compareSql.getDatabaseId());
        ObjectId reference_id = new LongObjectId(compareSql.getRefDatabaseId());
        try{

            DatabaseMeta databaseMeta = repository.loadDatabaseMeta(database_id, null);
            DatabaseMeta refdatabaseMeta = repository.loadDatabaseMeta(reference_id, null);

            testDatabase = new Database(loggingObject, databaseMeta);
            testDatabase.connect();
            refDatabase =  new Database(loggingObject, refdatabaseMeta);
            refDatabase.connect();
            String sql= compareSql.getSql();
            String refeSql = compareSql.getRefSql();

            if(testDatabase!=null && refDatabase!= null   && compareSql!=null && sql != null && refeSql !=null && !"".equals(sql) && !"".equals(refeSql)){


                List<Map<String,String>> resultSetTmps = new ArrayList<>();
                List<Map<String,String>> refResultSetTmps = new ArrayList<>();
                if(fields!=null && !fields.isEmpty()){
                    resultSet  = testDatabase.openQuery(sql);//sql执行结果
                    while(resultSet.next()){
                        for(CompareSqlField field: fields){
                            if(field.getFieldName()!=null && !"".equals(field.getFieldName())){
                                Map<String,String> tmp = new HashMap<>();
                                tmp.put(field.getFieldName(),resultSet.getString(field.getFieldName()));
                                resultSetTmps.add(tmp);
                            }
                        }
                    }
                    refResultSet  = refDatabase.openQuery(refeSql);//参考sql执行结果
                    while(refResultSet.next()){
                        for(CompareSqlField field: fields){
                            if(field.getRefFieldName()!=null && !"".equals(field.getRefFieldName())){
                                Map<String,String> tmp = new HashMap<>();
                                tmp.put(field.getRefFieldName(),refResultSet.getString(field.getRefFieldName()));
                                refResultSetTmps.add(tmp);
                            }

                        }
                    }

                    //如果返回结果长度不一致着去多了一方循环
                    int count = resultSetTmps.size() > refResultSetTmps.size()?resultSetTmps.size():refResultSetTmps.size();

                    for(int i = 0 ; i < count;i++ ){
                        //如果已经取完了 这返回null
                        Map<String,String> resultSetTmp = i > resultSetTmps.size() ? null : resultSetTmps.get(i);
                        Map<String,String> refResultSetTmp = i > refResultSetTmps.size()? null:  refResultSetTmps.get(i);
                        CompareSqlField field = fields.get(i);
                        CompareSqlResult compareSqlResult = new CompareSqlResult();
                        compareSqlResult.setCompareSqlFieldId(field.getCompareSqlFieldId());
                        compareSqlResult.setCompareTime(new Date());
                        compareSqlResult.setOrganizerId(compareSql.getOrganizerId());

                        compareSqlResult.preInsert();

                        compareSqlResult.setFieldValue(resultSetTmp == null ? null : resultSetTmp.get(field.getFieldName()));
                        compareSqlResult.setRefFieldValue(refResultSetTmp ==null ? null:refResultSetTmp.get(field.getFieldName()));
                        //判断通过标准
                        if(field.getFieldCompareType() == 0){//等于参照值
                            if(compareSqlResult.getFieldValue()!=null && compareSqlResult.getRefFieldValue()!=null && compareSqlResult.getFieldValue().equals(compareSqlResult.getRefFieldValue())){
                                compareSqlResult.setCompareResult(1);
                            } else if (compareSqlResult.getFieldValue() ==null  && compareSqlResult.getRefFieldValue()==null){
                                compareSqlResult.setCompareResult(1);
                            }else{
                                compareSqlResult.setCompareResult(0);
                            }

                        }else {
                            //参照值之间
                            if(StringUtils.isDouble(compareSqlResult.getFieldValue()) &&  StringUtils.isDouble(compareSqlResult.getRefFieldValue())){

                                double a = Double.parseDouble(compareSqlResult.getFieldValue());
                                double b = Double.parseDouble(compareSqlResult.getRefFieldValue()) * field.getMinRatio();
                                double c = Double.parseDouble(compareSqlResult.getRefFieldValue()) * field.getMaxRatio();
                                if(a >=  b && a <= c){
                                    compareSqlResult.setCompareResult(1);
                                }else{
                                    compareSqlResult.setCompareResult(0);
                                }
                            }

                        }
                        insert(compareSqlResult);
                    }
                }
            }
            return true;
        } catch(Exception e){
            e.printStackTrace();
            log.error(e);
            return false;
        }finally {

            if (repository != null) {
                repository.disconnect();
            }
            if (testDatabase != null) {
                testDatabase.disconnect();
            }

            if (refDatabase != null) {
                refDatabase.disconnect();
            }

            if (refResultSet != null) {
                try {
                    refResultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
