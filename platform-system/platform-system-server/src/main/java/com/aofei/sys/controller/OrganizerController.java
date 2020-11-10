package com.aofei.sys.controller;


import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.sys.model.request.OrganizerRequest;
import com.aofei.sys.model.response.OrganizerResponse;
import com.aofei.sys.service.IOrganizerService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Log4j
@Api(tags = { "系统管理-组织管理模块接口" })
@RestController
@RequestMapping(value = "/sys/org", produces = {"application/json;charset=UTF-8"})
public class OrganizerController extends BaseController {


    @Autowired
    IOrganizerService organizerService;

    /**
     * 组织列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "组织列表(分页查询)", notes = "组织列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Response<DataGrid<OrganizerResponse>> page(@ApiIgnore OrganizerRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        Page<OrganizerResponse> page = organizerService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 菜单列表
     * @param request
     * @return
     */
    @ApiOperation(value = "组织列表", notes = "组织列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "组织名称(模糊查询)", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<OrganizerResponse>> list(@ApiIgnore OrganizerRequest request)  {

        List<OrganizerResponse> list = organizerService.getOrganizers(request);
        return Response.ok(list) ;
    }

}
