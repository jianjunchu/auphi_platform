package com.aofei.datasource.controller;


import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.datasource.model.request.InterfaceRequest;
import com.aofei.datasource.model.response.InterfaceResponse;
import com.aofei.datasource.service.IInterfaceService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 数据接口管理 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Api(tags = { "数据源管理-接口数据源管理" })
@Authorization
@RestController
@RequestMapping("/datasource/interface")
public class DataInterfaceController extends BaseController {

    @Autowired
    IInterfaceService interfaceService;

    /**
     * 接口数据源(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "接口数据源(分页查询)", notes = "接口数据源(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")

    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<InterfaceResponse>> page(
            @ApiIgnore InterfaceRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<InterfaceResponse> page = interfaceService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 接口数据源
     * @param request
     * @return
     */
    @ApiOperation(value = "所有接口数据源", notes = "所有接口数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "description", value = "描述(模糊查询)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ruleType", value = "规则类型", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<InterfaceResponse>> list(
            @ApiIgnore InterfaceRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        List<InterfaceResponse> list = interfaceService.getInterfaces(request);
        return Response.ok(list) ;
    }


    /**
     * 新建接口数据源
     * @param request
     * @return
     */
    @ApiOperation(value = "新建接口数据源", notes = "新建接口数据源")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<InterfaceResponse> add(
            @RequestBody InterfaceRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        return Response.ok(interfaceService.save(request)) ;
    }



    /**
     * 编辑接口数据源
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑接口数据源", notes = "编辑接口数据源")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<InterfaceResponse> edit(
            @RequestBody InterfaceRequest request)  {
        return Response.ok(interfaceService.update(request)) ;
    }

    /**
     * 删除接口数据源
     * @param id
     * @return
     */
    @ApiOperation(value = "删除接口数据源", notes = "删除接口数据源", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(interfaceService.del(id)) ;
    }

    /**
     * 根据Id查询接口数据源
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询接口数据源", notes = "根据Id查询接口数据源")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<InterfaceResponse> get(
            @PathVariable Long id)  {

        return Response.ok(interfaceService.get(id)) ;
    }
}

