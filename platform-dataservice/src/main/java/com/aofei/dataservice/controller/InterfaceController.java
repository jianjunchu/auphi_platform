package com.aofei.dataservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.dataservice.entity.ServiceInterface;
import com.aofei.dataservice.exception.DataServiceError;
import com.aofei.dataservice.model.request.ServiceAuthRequest;
import com.aofei.dataservice.model.request.ServiceInterfaceRequest;
import com.aofei.dataservice.model.request.ServiceUserRequest;
import com.aofei.dataservice.model.response.ServiceAuthResponse;
import com.aofei.dataservice.model.response.ServiceInterfaceFieldResponse;
import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import com.aofei.dataservice.model.response.ServiceUserResponse;
import com.aofei.dataservice.service.IServiceAuthService;
import com.aofei.dataservice.service.IServiceInterfaceFieldService;
import com.aofei.dataservice.service.IServiceInterfaceService;
import com.aofei.dataservice.service.IServiceUserService;
import com.aofei.dataservice.utils.SqlUtil;
import com.aofei.kettle.App;
import com.aofei.utils.StringUtils;
import com.jayway.jsonpath.JsonPath;
import org.pentaho.di.core.database.Database;
import com.aofei.utils.IPUtils;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;


/**
 * <p>
 *  数据接口管理控制器
 * </p>
 *
 * @author Tony
 * @since 2018-11-10
 */
@Log4j
@RestController
@RequestMapping("/dataservice/interface")
@Api(tags = { "数据服务-数据接口管理" })
public class InterfaceController extends BaseController {

    @Autowired
    IServiceInterfaceService serviceInterfaceService;

    @Autowired
    IServiceUserService serviceUserService;

    @Autowired
    IServiceAuthService serviceAuthService;

    @Autowired
    IServiceInterfaceFieldService serviceInterfaceFieldService;

    /**
     * 对外数据接出接口列表(分页查询)
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-分页列表", notes = "数据接口列表(分页查询)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<ServiceInterfaceResponse>> page(
            @ApiIgnore ServiceInterfaceRequest request
            , @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<ServiceInterfaceResponse> page = serviceInterfaceService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 对外数据接出接口列表
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-全部列表", notes = "所有数据接口的列表", httpMethod = "GET")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<ServiceInterfaceResponse>> list(@ApiIgnore ServiceInterfaceRequest request)  {
        List<ServiceInterfaceResponse> list = serviceInterfaceService.getServiceInterfaces(request);
        return Response.ok(list) ;
    }


    /**
     * 新建对外数据接出接口
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<ServiceInterfaceResponse> add(
            @RequestBody ServiceInterfaceRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        request.setOrganizerId(user.getOrganizerId());

        return Response.ok(serviceInterfaceService.save(request)) ;
    }

    /**
     * 编辑对外数据接出接口
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-编辑", notes = "编辑数据接口", httpMethod = "POST")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<ServiceInterfaceResponse> edit(
            @RequestBody ServiceInterfaceRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        return Response.ok(serviceInterfaceService.update(request)) ;
    }

    /**
     * 删除对外数据接出接口
     * @param id
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-删除", notes = "删除数据接口", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(serviceInterfaceService.del(id)) ;
    }

    /**
     * 根据Id查询对外数据接出接口
     * @param id
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口-根据Id查询", notes = "根据Id查询数据接口", httpMethod = "GET")
    @RequestMapping(value = "/{id}/get", method = RequestMethod.GET)
    public Response<ServiceInterfaceResponse> get(
            @PathVariable Long id)  {

        return Response.ok(serviceInterfaceService.get(id)) ;
    }

    /**
     * 执行对外数据接口
     * @return
     */
    @ApiOperation(value = "数据接口-数据对外发布接口", notes = "数据对外发布接口")
    @RequestMapping(value = "/{id}/publish")
    public Response<JSON> publish(
            @ApiIgnore HttpServletRequest request,
            @ApiParam(value = "接口ID", required = true) @PathVariable Long id,
            @ApiParam(value = "用户名")  @RequestParam(value = "username") String username,
            @ApiParam(value = "密码")  @RequestParam(value = "password") String password) {

        ServiceInterfaceResponse interfaceResponse = serviceInterfaceService.get(id);

        testAuth(interfaceResponse,username,password,request);

        if(ServiceInterface.RETURN_TYPE_FTP.equals(interfaceResponse.getReturnType())){
            return Response.ok(returnFtp(interfaceResponse));
        }else if(ServiceInterface.RETURN_TYPE_WEBSERVICE.equals(interfaceResponse.getReturnType())){
            return Response.ok(returnWebservice(interfaceResponse));
        }else{
            throw new ApplicationException("接口错误,请联系接口提供方!");
        }

    }

    private ServiceUserResponse testAuth(ServiceInterfaceResponse interfaceResponse,String username,String password,HttpServletRequest request) {

        if(StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
            return new ServiceUserResponse();
        }

        ServiceUserRequest userRequest = new ServiceUserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setOrganizerId(interfaceResponse.getOrganizerId());

        ServiceUserResponse userResponse =  serviceUserService.getAuthUser(userRequest);

        ServiceAuthRequest authRequest = new ServiceAuthRequest();
        authRequest.setServiceId(interfaceResponse.getServiceId());
        authRequest.setUserId(userResponse.getUserId());

        ServiceAuthResponse authResponse = serviceAuthService.getServiceAuth(authRequest);

        if(authResponse == null){
            return userResponse;
        }
        String clienIp = IPUtils.getIpAddr(request);
        String authIp = authResponse.getAuthIp();

        if("*".equalsIgnoreCase(authIp) || authIp.contains(clienIp) || authIp.indexOf(clienIp) > 0 || clienIp.matches(authIp)){
            return userResponse;
        }else{
            throw new ApplicationException(DataServiceError.PERMISSION_DENIED.getCode(),"您没有权限访问改接口,请联系接口提供方");
        }
    }


    private JSONArray returnWebservice(ServiceInterfaceResponse serviceInterface){

        Database database = null;
        try {

            database = getDatabase(serviceInterface);

            database.connect();
            database.setQueryLimit(10000);

            String sql = SqlUtil.getSQl(serviceInterface);
            ResultSet resultSet = database.openQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                for(int i = 1 ; i < resultSetMetaData.getColumnCount()+1;i++){
                    String column = resultSetMetaData.getColumnName(i);
                    jsonObject.put(column,resultSet.getString(column));
                }
                jsonArray.add(jsonObject);
            }
            database.closeQuery(resultSet);

            return jsonArray;

        }catch (Exception e){
            e.printStackTrace();
            throw  new ApplicationException(e.getMessage());
        }finally {
            if(database!=null)
                database.disconnect();
        }
    }

    private Database getDatabase(ServiceInterfaceResponse serviceInterface) throws KettleException {
        Repository repository = App.getInstance().getRepository();

        ObjectId conId =new LongObjectId(serviceInterface.getDatabaseId());
        DatabaseMeta databaseMeta =  repository.loadDatabaseMeta(conId,null);

        Database database = new Database(loggingObject,databaseMeta);

        return database;
    }



    private JSONObject returnFtp(ServiceInterfaceResponse serviceInterface){
        JSONObject jsonObject = new JSONObject();

        return jsonObject;

    }

    /**
     * 执行对外数据接口
     * @return
     */
    @ApiOperation(value = "数据接口-数据接受接口(单条数据)", notes = "数据接受接口(单条数据)")
    @RequestMapping(value = "/{id}/receive")
    public Response<Boolean> receive(@ApiIgnore HttpServletRequest request,
                                        @PathVariable Long id,
                                        @ApiParam(value = "用户名")  @RequestParam(value = "username") String username,
                                        @ApiParam(value = "密码")  @RequestParam(value = "password") String password,
                                        @RequestBody JSONObject jsonObject)  {

        ServiceInterfaceResponse interfaceResponse = serviceInterfaceService.get(id);
        testAuth(interfaceResponse,username,password,request);

        List<ServiceInterfaceFieldResponse> fieldResponses = serviceInterfaceFieldService.getServiceInterfaceFields(interfaceResponse.getServiceId());

        RowMetaInterface missing = new RowMeta();
        Object[] data = new Object[fieldResponses.size()];
        for( int i = 0;i < fieldResponses.size();i++){
            ServiceInterfaceFieldResponse fieldResponse= fieldResponses.get(i);
            missing.addValueMeta(new ValueMeta(fieldResponse.getFieldName(),ValueMetaInterface.TYPE_STRING));
            data[i] = JsonPath.parse(jsonObject.toJSONString()).read(fieldResponse.getJsonPath());
        }

        Database database = null;
        try {
            database = getDatabase(interfaceResponse);
            database.connect();
            database.prepareInsert(missing,interfaceResponse.getTableName());
            database.setValuesInsert( missing, data );
            database.insertRow();
            database.closeInsert();

        }catch (Exception e){
            e.printStackTrace();
            throw  new ApplicationException(e.getMessage());
        }finally {
            if(database!=null)
                database.disconnect();
        }


        return Response.ok(true) ;
    }

    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("InterfaceController", LoggingObjectType.DATABASE, null );


}
