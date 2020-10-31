package com.aofei.dataquality.controller;


import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;

import com.aofei.base.model.vo.DataGrid;
import com.aofei.dataquality.model.request.RuleGroupRequest;
import com.aofei.dataquality.model.response.RuleGroupResponse;
import com.aofei.dataquality.service.IRuleGroupService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aofei.base.controller.BaseController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * <p>
 * 数据质量规则分组 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
@Api(tags = { "数据质量管理-数据质量规则分组" })
@Authorization
@RestController
@RequestMapping("/dataQuality/ruleGroup")
public class RuleGroupController extends BaseController {

    @Autowired
    IRuleGroupService ruleGroupService;

    /**
     * 数据质量规则分组(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "数据质量规则分组(分页查询)", notes = "数据质量规则分组(分页查询)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "groupName", value = "名称(模糊查询)", paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<RuleGroupResponse>> page(
            @ApiIgnore RuleGroupRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<RuleGroupResponse> page = ruleGroupService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 数据质量规则分组
     * @param request
     * @return
     */
    @ApiOperation(value = "所有数据质量规则分组", notes = "所有数据质量规则分组", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "名称(模糊查询)", paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<RuleGroupResponse>> list(
            @ApiIgnore RuleGroupRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        List<RuleGroupResponse> list = ruleGroupService.getRuleGroups(request);
        return Response.ok(list) ;
    }


    /**
     * 新建数据质量规则分组
     * @param request
     * @return
     */
    @ApiOperation(value = "新建数据质量规则分组", notes = "新建数据质量规则分组", httpMethod = "POST")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<RuleGroupResponse> add(
            @RequestBody RuleGroupRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        return Response.ok(ruleGroupService.save(request)) ;
    }

    /**
     * 编辑数据质量规则分组
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑数据质量规则分组", notes = "编辑数据质量规则分组", httpMethod = "POST")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<RuleGroupResponse> edit(
            @RequestBody RuleGroupRequest request)  {
        return Response.ok(ruleGroupService.update(request)) ;
    }

    /**
     * 删除数据质量规则分组
     * @param id
     * @return
     */
    @ApiOperation(value = "删除数据质量规则分组", notes = "删除数据质量规则分组", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(ruleGroupService.del(id)) ;
    }

    /**
     * 根据Id查询数据质量规则分组
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询数据质量规则分组", notes = "根据Id查询数据质量规则分组", httpMethod = "GET")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<RuleGroupResponse> get(
            @PathVariable Long id)  {

        return Response.ok(ruleGroupService.get(id)) ;
    }
}

