package com.aofei.datasource.controller;

import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.datasource.model.request.DatabaseRequest;
import com.aofei.datasource.model.response.DatabaseNameResponse;
import com.aofei.datasource.model.response.DatabaseResponse;
import com.aofei.datasource.service.IDatabaseService;
import com.aofei.kettle.App;
import com.aofei.utils.BeanCopier;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseMetaInformation;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * <p>
 *  数据库管理 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2018-09-21
 */
@Log4j
@Api(tags = { "数据源管理-数据库管理模块接口" })
@Authorization
@RestController
@RequestMapping(value = "/datasource/database", produces = {"application/json;charset=UTF-8"})
public class DatabaseController extends BaseController {

    @Autowired
    private IDatabaseService databaseService;

    /**
     * 列表(分页查询)
     * @return
     */
    @ApiOperation(value = "数据库列表(分页查询)", notes = "数据库列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "数据源名称(like查询)", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<DatabaseResponse>> page(
            @ApiIgnore DatabaseRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {
        request.setOrganizerId(user.getOrganizerId());
        Page<DatabaseResponse> page = databaseService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 列表(分页查询)
     * @return
     */
    @ApiOperation(value = "数据库列表", notes = "数据库列表")

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<DataGrid<DatabaseResponse>> listAll(
            @ApiIgnore DatabaseRequest request
            , @ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {
        request.setOrganizerId(user.getOrganizerId());
        List<DatabaseResponse> page = databaseService.getDatabases( request);
        return Response.ok(page) ;
    }
    /**
     * 列表(分页查询)
     * @return
     */
    @ApiOperation(value = "数据库名称列表(只返回name)", notes = "数据库名称列表")
    @RequestMapping(value = "/listNames", method = RequestMethod.POST)
    public List<DatabaseNameResponse> listNames(
            @ApiIgnore DatabaseRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {
        request.setOrganizerId(user.getOrganizerId());
        List<DatabaseResponse> list = databaseService.getDatabases(request);
        return BeanCopier.copy(list,DatabaseNameResponse.class) ;
    }

    /**
     * 获取数据库中的所有模式名
     */
    @ApiOperation(value = "获取数据库中的所有模式名", notes = "获取数据库中的所有模式名")
    @RequestMapping(value = "/{id}/listSchemas", method = RequestMethod.GET)
    public Response<ArrayList<Map>> listSchemas(@PathVariable Long id) throws KettleException, SQLException {
        Repository repository = App.getInstance().getRepository();
        ObjectId id_database = new LongObjectId(id);
        DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);
        ArrayList<Map> list = new ArrayList<>();
        Database db = new Database( loggingObject, databaseMeta );
        try {
            db.connect();
            DatabaseMetaData dbmd = db.getDatabaseMetaData();
            Map<String, String> connectionExtraOptions = databaseMeta.getExtraOptions();
            if (dbmd.supportsSchemasInTableDefinitions()) {

                String schemaFilterKey = databaseMeta.getPluginId() + "." + DatabaseMetaInformation.FILTER_SCHEMA_LIST;
                if ((connectionExtraOptions != null) && connectionExtraOptions.containsKey(schemaFilterKey)) {
                    String schemasFilterCommaList = connectionExtraOptions.get(schemaFilterKey);
                    String[] schemasFilterArray = schemasFilterCommaList.split(",");
                    for (int i = 0; i < schemasFilterArray.length; i++) {
                        Map<String,String> item = new HashMap<>();
                        item.put("name",schemasFilterArray[i].trim());
                        list.add(item);
                    }
                }
                if (list.size() == 0) {
                    String sql = databaseMeta.getSQLListOfSchemas();
                    if (!Const.isEmpty(sql)) {
                        Statement schemaStatement = db.getConnection().createStatement();
                        ResultSet schemaResultSet = schemaStatement.executeQuery(sql);
                        while (schemaResultSet != null && schemaResultSet.next()) {
                            String schemaName = schemaResultSet.getString("name");
                            Map<String,String> item = new HashMap<>();
                            item.put("name",schemaName);
                            list.add(item);
                        }
                        schemaResultSet.close();
                        schemaStatement.close();
                    } else {
                        ResultSet schemaResultSet = dbmd.getSchemas();
                        while (schemaResultSet != null && schemaResultSet.next()) {
                            String schemaName = schemaResultSet.getString(1);
                            Map<String,String> item = new HashMap<>();
                            item.put("name",schemaName);
                            list.add(item);
                        }
                        schemaResultSet.close();
                    }
                }


            }

        } finally {
            db.disconnect();
            repository.disconnect();
        }

        return Response.ok(list) ;
    }

    /**
     * 获取数据库中的所有模式名
     */
    @ApiOperation(value = "获取数据库中的所有表", notes = "获取数据库中的所有表")
    @RequestMapping(value = "/listTableNames", method = RequestMethod.POST)
    public Response<ArrayList<String>> listTableNames( @RequestParam(value = "databaseId") Long databaseId,
                                                  @RequestParam(value = "schemaName") String schemaName) throws KettleException, SQLException {

        Repository repository = App.getInstance().getRepository();
        ObjectId id_database = new LongObjectId(databaseId);
        DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);
        ArrayList<Map> list = new ArrayList<>();
        Database db = new Database( loggingObject, databaseMeta );
        try {
            db.connect();
            Map<String, Collection<String>> tableMap = db.getTableMap();
//					Map<String, Collection<String>> tableMap = db.getTableMap(databaseMeta.getUsername());
            List<String> tableKeys = new ArrayList<String>(tableMap.keySet());
            Collections.sort(tableKeys);

            if(StringUtils.isEmpty(schemaName)){
                for (String schema : tableKeys) {
                    List<String> tables = new ArrayList<String>(tableMap.get(schema));
                    Collections.sort(tables);
                    for (String tableName : tables){
                        Map<String,String> item = new HashMap<>();
                        item.put("name",tableName);
                        list.add(item);
                    }
                }
            }else{
                List<String> tables = new ArrayList<String>(tableMap.get(schemaName));
                Collections.sort(tables);
                for (String tableName : tables){
                    Map<String,String> item = new HashMap<>();
                    item.put("name",tableName);
                    list.add(item);
                }
            }

        } finally {
            db.disconnect();
            repository.disconnect();
        }

        return Response.ok(list) ;
    }

    /**
     * 获取数据库中的所有模式名
     */
    @ApiOperation(value = "获取数据库中的表的所有字段", notes = "获取数据库中的表的所有字段")
    @RequestMapping(value = "/listFieldNames", method = RequestMethod.POST)
    public Response<ArrayList<String>> listFieldNames( @RequestParam(value = "databaseId") Long databaseId,
                                                        @RequestParam(value = "schemaName") String schemaName,
                                                       @RequestParam(value = "tableName") String tableName) throws KettleException, SQLException {

        Repository repository = App.getInstance().getRepository();
        ObjectId id_database = new LongObjectId(databaseId);
        DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);
        ArrayList<Map> list = new ArrayList<>();
        Database db = new Database( loggingObject, databaseMeta );
        try {
            db.connect();

            String schemaTable = databaseMeta.getQuotedSchemaTableCombination( schemaName, tableName );
            RowMetaInterface fields = db.getTableFields(schemaTable);
            if (fields != null) {
                for (int i = 0; i < fields.size(); i++) {
                    ValueMetaInterface field = fields.getValueMeta(i);
                    Map<String,Object> item = new HashMap<>();

                    item.put("fieldName",databaseMeta.quoteField(field.getName()));
                    item.put("typeDesc",field.getTypeDesc());
                    item.put("label",item.get("fieldName")+"("+item.get("typeDesc")+")");
                    item.put("type",field.getType());
                    item.put("originalType",field.getOriginalColumnTypeName());
                    list.add(item);
                }
            }

        } finally {
            db.disconnect();
            repository.disconnect();
        }

        return Response.ok(list) ;
    }

    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("DatabaseController", LoggingObjectType.DATABASE, null );
}
