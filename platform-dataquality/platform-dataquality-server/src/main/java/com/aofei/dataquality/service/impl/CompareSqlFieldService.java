package com.aofei.dataquality.service.impl;


import com.aofei.base.service.impl.BaseService;
import com.aofei.dataquality.entity.CompareSqlField;
import com.aofei.dataquality.mapper.CompareSqlFieldMapper;
import com.aofei.dataquality.model.request.CompareSqlRequest;
import com.aofei.dataquality.model.response.CompareSqlFieldResponse;
import com.aofei.dataquality.service.ICompareSqlFieldService;
import com.aofei.kettle.App;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Service
public class CompareSqlFieldService extends BaseService<CompareSqlFieldMapper, CompareSqlField> implements ICompareSqlFieldService {


    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("CompareSqlFieldService", LoggingObjectType.DATABASE, null);

    /**
     * 获取SQL字段
     *
     * @param request
     * @return
     */
    @Override
    public List<CompareSqlFieldResponse> getCompareFields(CompareSqlRequest request) {

        Database database = null;
        Database reference = null;
        Repository repository = App.getInstance().getRepository();

        List<CompareSqlFieldResponse> list = new ArrayList<>();

        try {
            ObjectId database_id = new LongObjectId(request.getDatabaseId());
            ObjectId reference_id = new LongObjectId(request.getRefDatabaseId());
            DatabaseMeta databaseMeta = repository.loadDatabaseMeta(database_id, null);
            DatabaseMeta refdatabaseMeta = repository.loadDatabaseMeta(reference_id, null);

            database = new Database(loggingObject, databaseMeta);
            reference = new Database(loggingObject, refdatabaseMeta);
            database.connect();
            reference.connect();

            ResultSet sqlSet = database.openQuery(request.getSql());
            ResultSet refSqlSet = reference.openQuery(request.getRefSql());

            ResultSetMetaData sqlSetData = sqlSet.getMetaData();
            ResultSetMetaData referenceSqlSetData = refSqlSet.getMetaData();

            int cols = sqlSetData.getColumnCount();
            int cols2 = referenceSqlSetData.getColumnCount();

            if (cols == cols2) {
                for (int i = 1; i <= cols; i++) {

                    CompareSqlFieldResponse response = new CompareSqlFieldResponse();

                    String columnName = sqlSetData.getColumnName(i);
                    String referenceColumnName = referenceSqlSetData.getColumnName(i);

                    response.setFieldName(columnName);
                    response.setRefFieldName(referenceColumnName);

                    String columnType = sqlSetData.getColumnTypeName(i);
                    String referenceColumnType = referenceSqlSetData.getColumnTypeName(i);

                    if (columnType.equals(referenceColumnType)) {
                        response.setFieldType(columnType);
                    }

                    list.add(response);
                }
            }else{

            }

        } catch (KettleException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(database!=null){
                database.disconnect();
            }
            if(reference!=null){
                reference.disconnect();
            }
            if(repository!=null){
                repository.disconnect();
            }
        }


        return list;

    }
}
