package org.firzjb.dataservice.controller;


import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.base.model.vo.DataGrid;
import org.firzjb.dataservice.model.request.ServiceAuthRequest;
import org.firzjb.dataservice.model.response.ServiceAuthResponse;
import org.firzjb.dataservice.service.IServiceAuthService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.firzjb.dataservice.model.request.ServiceAuthRequest;
import org.firzjb.dataservice.model.response.ServiceAuthResponse;
import org.firzjb.dataservice.service.IServiceAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 *  服务用户权限管理控制器
 * </p>
 *
 * @author Tony
 * @since 2018-11-10
 */
@Log4j
@RestController
@RequestMapping("/dataservice/auth")
@Api(tags = { "数据服务-数据发布用户权限管理" })
public class AuthController extends BaseController {


    @Autowired
    IServiceAuthService serviceAuthService;

    /**
     * 服务用户权限列表(分页查询)
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-分页列表", notes = "服务用户权限列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<ServiceAuthResponse>> page(
            @ApiIgnore ServiceAuthRequest request
            , @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<ServiceAuthResponse> page = serviceAuthService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 服务用户权限列表
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-全部列表", notes = "所有服务用户权限的列表")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<ServiceAuthResponse>> list(@ApiIgnore ServiceAuthRequest request)  {
        List<ServiceAuthResponse> list = serviceAuthService.getServiceAuths(request);
        return Response.ok(list) ;
    }


    /**
     * 新建服务用户权限
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<ServiceAuthResponse> add(
            @RequestBody ServiceAuthRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        request.setOrganizerId(user.getOrganizerId());

        return Response.ok(serviceAuthService.save(request)) ;
    }

    /**
     * 编辑服务用户权限
     * @param request
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-编辑", notes = "编辑服务用户权限")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<ServiceAuthResponse> edit(
            @RequestBody ServiceAuthRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        return Response.ok(serviceAuthService.update(request)) ;
    }

    /**
     * 删除服务用户权限
     * @param id
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-删除", notes = "删除服务用户权限", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(serviceAuthService.del(id)) ;
    }

    /**
     * 根据Id查询服务用户权限
     * @param id
     * @return
     */
    @Authorization
    @ApiOperation(value = "服务用户权限-根据Id查询", notes = "根据Id查询服务用户权限")
    @RequestMapping(value = "/{id}/get", method = RequestMethod.GET)
    public Response<ServiceAuthResponse> get(
            @PathVariable Long id)  {

        return Response.ok(serviceAuthService.get(id)) ;
    }

}

