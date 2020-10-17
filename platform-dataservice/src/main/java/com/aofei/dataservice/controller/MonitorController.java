package com.aofei.dataservice.controller;


import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.dataservice.model.request.ServiceAuthRequest;
import com.aofei.dataservice.model.request.ServiceMonitorRequest;
import com.aofei.dataservice.model.response.ServiceAuthResponse;
import com.aofei.dataservice.model.response.ServiceMonitorResponse;
import com.aofei.dataservice.service.IServiceAuthService;
import com.aofei.dataservice.service.IServiceMonitorService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 *  服务接口监控控制器
 * </p>
 *
 * @author Tony
 * @since 2018-11-10
 */
@Log4j
@RestController("serviceMonitorController")
@RequestMapping("/dataservice/monitor")
@Api(tags = { "数据服务-数据接口调用监控" })
public class MonitorController extends BaseController {



    @Autowired
    IServiceMonitorService serviceMonitorService;


    /**
     * 服务接口监控控列表(分页查询)
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口调用监控-分页列表", notes = "数据接口调用监控(分页查询)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<ServiceMonitorResponse>> page(
            @ApiIgnore ServiceMonitorRequest request
            , @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<ServiceMonitorResponse> page = serviceMonitorService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 删除数据发布访问用户
     * @param id
     * @return
     */
    @Authorization
    @ApiOperation(value = "数据接口调用监控-删除", notes = "删除数据接口调用监控", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(serviceMonitorService.del(id)) ;
    }

}