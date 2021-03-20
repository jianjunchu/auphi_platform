package com.aofei.query.utils;


import com.aofei.base.exception.ApplicationException;
import com.aofei.base.model.request.PageRequest;
import com.aofei.kettle.App;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.plugins.pagination.dialects.MySqlDialect;
import com.baomidou.mybatisplus.plugins.pagination.dialects.OracleDialect;
import com.baomidou.mybatisplus.plugins.pagination.dialects.SQLServer2005Dialect;
import com.baomidou.mybatisplus.plugins.pagination.dialects.SQLServerDialect;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.*;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseLoader {

    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("SourceUtils", LoggingObjectType.DATABASE, null );

    private static String db_name = "db_query_statistics";

    private  Database db;
    private String sql;
    private Repository repository;
    private String countSQl = "";
    private String pageSQl = "";



    public DatabaseLoader() throws KettleException {
        this.repository = App.getInstance().getRepository();
        initDatabase();
        if(db == null){
            throw new ApplicationException("请创建名称为:db_query_statistics 的查询数据库连接");
        }
    }

    private void initPageSQl(Pagination page) {


        if(db.getDatabaseMeta().getDatabaseInterface() instanceof MySQLDatabaseMeta){
            pageSQl = MySqlDialect.INSTANCE.buildPaginationSql(sql, PageHelper.offsetCurrent(page),page.getLimit());

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof OracleDatabaseMeta){
            pageSQl = OracleDialect.INSTANCE.buildPaginationSql(sql, PageHelper.offsetCurrent(page),page.getLimit());

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerDatabaseMeta){
            pageSQl = SQLServer2005Dialect.INSTANCE.buildPaginationSql(sql, PageHelper.offsetCurrent(page),page.getLimit());

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerNativeDatabaseMeta){
            pageSQl = SQLServerDialect.INSTANCE.buildPaginationSql(sql, PageHelper.offsetCurrent(page),page.getLimit());

        }


    }

    private void initCountSQl() {
        StringBuffer sqlBuffer = new StringBuffer();

        if(db.getDatabaseMeta().getDatabaseInterface() instanceof MySQLDatabaseMeta){

            sqlBuffer.append("SELECT COUNT(1) FROM ( ").append(sql).append(" ) as total");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof OracleDatabaseMeta){
            sqlBuffer.append("SELECT COUNT(1) FROM ( ").append(sql).append(" ) as total");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerDatabaseMeta){
            sqlBuffer.append("SELECT COUNT(1) FROM ( ").append(sql).append(" ) as total");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerNativeDatabaseMeta){
            sqlBuffer.append("SELECT COUNT(1) FROM ( ").append(sql).append(" ) as total");

        }
        countSQl = sqlBuffer.toString();
    }

    public  void initDatabase() throws ApplicationException {
        try {
            ObjectId id_database = repository.getDatabaseID(db_name);
            DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);
            ArrayList<Map> list = new ArrayList<>();
            db = new Database( loggingObject, databaseMeta );

        }catch (Exception e){
            throw new ApplicationException("请创建名称为:db_query_statistics 的查询数据库连接");
        }
    }


    public Page getPage(Page pagination,String sql) throws KettleValueException, KettleDatabaseException, SQLException {
        try {
            this.sql = sql;
            initCountSQl();
            initPageSQl(pagination);
            db.connect();

            RowMetaAndData countRow =  db.getOneRow(countSQl);
            Long total = countRow.getInteger(0);
            ResultSet rs = db.openQuery( pageSQl );
            List<Map<String,Object>> list = new ArrayList<>();

            while(rs.next()) {
                Map<String,Object> item = new HashMap<>();
                ResultSetMetaData rsmd1 = rs.getMetaData();
                int count = rsmd1.getColumnCount();
                for (int i = 1 ; i <= count;i++){
                    item.put(rsmd1.getColumnLabel(i),rs.getObject(i));
                }
                list.add(item);
            }
            rs.close();
            pagination.setTotal(total.intValue());
            pagination.setRecords(list);
            return  pagination;

        }catch (Exception e){
            throw e;
        }finally {
            disconnect();
        }


    }

    public List getList(String sql) throws KettleValueException, KettleDatabaseException, SQLException {
        try {
            this.sql = sql;
            db.connect();
            ResultSet rs = db.openQuery( sql );
            List<Map<String,Object>> list = new ArrayList<>();
            while(rs.next()) {
                Map<String,Object> item = new HashMap<>();
                ResultSetMetaData rsmd1 = rs.getMetaData();
                int count = rsmd1.getColumnCount();
                for (int i = 1 ; i <= count;i++){
                    item.put(rsmd1.getColumnLabel(i),rs.getObject(i));
                }
                list.add(item);
            }
            rs.close();
            return  list;

        }catch (Exception e){
            throw e;
        }finally {
            disconnect();

        }


    }


    public String getDateBetween(String column ,  PageRequest request) {

        StringBuffer sqlBuffer = new StringBuffer();

        if(db.getDatabaseMeta().getDatabaseInterface() instanceof MySQLDatabaseMeta){

            sqlBuffer.append(" str_to_date(").append(column).append(",'%Y%m%d%H%i%s')  between str_to_date('").append(request.getSearch_satrt()).append("','%Y-%m-%d %H:%i:%s') and str_to_date('").append(request.getSearch_end()).append("','%Y-%m-%d %H:%i:%s')");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof OracleDatabaseMeta){
            sqlBuffer.append("to_date(").append(column).append(",'yyyy-MM-dd HH:mm:ss') BETWEEN ").append("to_date('").append(request.getSearch_satrt()).append("','yyyy-MM-dd HH:mm:ss') AND to_date('").append(request.getSearch_end()).append("','yyyy-MM-dd HH:mm:ss')");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerDatabaseMeta){
            sqlBuffer.append("to_date(").append(column).append(",'yyyy-MM-dd HH:mm:ss') BETWEEN ").append("to_date('").append(request.getSearch_satrt()).append("','yyyy-MM-dd HH:mm:ss') AND to_date('").append(request.getSearch_end()).append("','yyyy-MM-dd HH:mm:ss')");

        } else if(db.getDatabaseMeta().getDatabaseInterface() instanceof MSSQLServerNativeDatabaseMeta){
            sqlBuffer.append("to_date(").append(column).append(",'yyyy-MM-dd HH:mm:ss') BETWEEN ").append("to_date('").append(request.getSearch_satrt()).append("','yyyy-MM-dd HH:mm:ss') AND to_date('").append(request.getSearch_end()).append("','yyyy-MM-dd HH:mm:ss')");

        }else {
            sqlBuffer.append(" 1=1 ");
        }

        return sqlBuffer.toString();
    }

    public Database getDb() {
        return db;
    }

    public void disconnect(){
        if(db!=null){
            db.disconnect();

        }
        if(repository!=null){
            repository.disconnect();
        }
    }
}
