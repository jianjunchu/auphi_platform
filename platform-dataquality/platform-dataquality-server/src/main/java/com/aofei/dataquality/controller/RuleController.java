package com.aofei.dataquality.controller;


import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.dataquality.model.request.RuleRequest;
import com.aofei.dataquality.model.response.RuleResponse;
import com.aofei.dataquality.service.IRuleService;
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
 * 数据质量管理-规则管理 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2020-11-02
 */
@Api(tags = { "数据质量管理-数据质量规则" })
@Authorization
@RestController
@RequestMapping("/dataQuality/rule")
public class RuleController extends BaseController {

    @Autowired
    IRuleService ruleService;

    /**
     * 数据质量规则(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "数据质量规则(分页查询)", notes = "数据质量规则(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "description", value = "描述(模糊查询)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ruleType", value = "规则类型", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<RuleResponse>> page(
            @ApiIgnore RuleRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<RuleResponse> page = ruleService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "所有数据质量规则", notes = "所有数据质量规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "description", value = "描述(模糊查询)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ruleType", value = "规则类型", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<RuleResponse>> list(
            @ApiIgnore RuleRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        List<RuleResponse> list = ruleService.getRules(request);
        return Response.ok(list) ;
    }


    /**
     * 新建数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "新建数据质量规则", notes = "新建数据质量规则")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<RuleResponse> add(
            @RequestBody RuleRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        return Response.ok(ruleService.save(request)) ;
    }

    /**
     * 编辑数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "单独编辑数据质量规则是否启用状态", notes = "单独编辑数据质量规则是否启用状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleId", value = "规则id", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "isEnable", value = "是否启用", paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/edit/enable", method = RequestMethod.POST)
    public Response<RuleResponse> editIsEnable(
            @ApiIgnore @RequestBody RuleRequest request)  {
        return Response.ok(ruleService.updateIsEnable(request)) ;
    }

    /**
     * 编辑数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑数据质量规则", notes = "编辑数据质量规则")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<RuleResponse> edit(
            @RequestBody RuleRequest request)  {
        return Response.ok(ruleService.update(request)) ;
    }

    /**
     * 删除数据质量规则
     * @param id
     * @return
     */
    @ApiOperation(value = "删除数据质量规则", notes = "删除数据质量规则", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(ruleService.del(id)) ;
    }

    /**
     * 根据Id查询数据质量规则
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询数据质量规则", notes = "根据Id查询数据质量规则")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<RuleResponse> get(
            @PathVariable Long id)  {

        return Response.ok(ruleService.get(id)) ;
    }


}

