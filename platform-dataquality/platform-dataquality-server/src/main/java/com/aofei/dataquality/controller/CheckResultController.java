package com.aofei.dataquality.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.dataquality.model.request.CheckResultErrRequest;
import com.aofei.dataquality.model.request.CheckResultRequest;
import com.aofei.dataquality.model.request.RuleRequest;
import com.aofei.dataquality.model.response.CheckResultErrResponse;
import com.aofei.dataquality.model.response.CheckResultResponse;
import com.aofei.dataquality.model.response.RuleGroupResponse;
import com.aofei.dataquality.model.response.RuleResponse;
import com.aofei.dataquality.service.ICheckResultErrService;
import com.aofei.dataquality.service.ICheckResultService;
import com.aofei.dataquality.service.IRuleGroupService;
import com.aofei.dataquality.service.IRuleService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 数据质量管理-规则检查结果 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Api(tags = { "数据质量管理-数据质量规则" })
@Authorization
@RestController
@RequestMapping("/dataQuality/checkResult")
public class CheckResultController extends BaseController {

    @Autowired
    private ICheckResultService checkResultService;

    @Autowired
    private ICheckResultErrService checkResultErrService;

    @Autowired
    private IRuleService ruleService;

    @Autowired
    private IRuleGroupService ruleGroupService;
    /**
     * 编辑数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "刷新规则检查结果", notes = "刷新规则检查结果")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public Response<RuleResponse> refresh(
            @ApiIgnore RuleRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {


        request.setOrganizerId(user.getOrganizerId());

        return Response.ok(ruleService.refresh(request,user)) ;
    }


    /**
     * 不符合规则数据(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "不符合规则数据(分页查询)", notes = "不符合规则数据(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/err/listPage", method = RequestMethod.GET)
    public Response<DataGrid<CheckResultErrResponse>> page(
            @ApiIgnore CheckResultErrRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<CheckResultErrResponse> page = checkResultErrService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 编辑数据质量规则
     * @return
     */
    @ApiOperation(value = "获取稽核状态", notes = "获取稽核状态")
    @RequestMapping(value = "/refresh/status", method = RequestMethod.GET)
    public Response<Integer> getRefreshStatus(
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {


        return Response.ok(checkResultService.getRefreshStatus(user.getUserId())) ;
    }

    /**
     * 编辑数据质量规则
     * @param request
     * @return
     */
    @ApiOperation(value = "获取规则集", notes = "获取规则集")
    @RequestMapping(value = "/getRuleGroupResultList", method = RequestMethod.GET)
    public Response<List<CheckResultResponse>> getRuleGroupResultList(
            @ApiIgnore CheckResultRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {


        request.setOrganizerId(user.getOrganizerId());

        List<CheckResultResponse> checkResultResponses =  checkResultService.getRuleGroupResultList(request);

        return Response.ok(checkResultResponses) ;
    }

    /**
     * 获获取规则集取图表数据
     * @return
     */
    @ApiOperation(value = "获取规则集取图表数据", notes = "获取规则集图表数据")
    @RequestMapping(value = "/ruleGroup/{ruleGroupId}/getChartData", method = RequestMethod.GET)
    public Response<JSONObject> getChartDataByRuleGroupId(@PathVariable Long ruleGroupId)  {
        JSONObject jsonObject = new JSONObject();

        RuleGroupResponse ruleGroup =  ruleGroupService.get(ruleGroupId);

        JSONArray ruleGroupCount = checkResultService.getBreakRuleChartDataByGroupId(ruleGroupId);

        JSONArray  checkResults =  checkResultService.getCheckResultChartDataByGroupId(ruleGroupId);



        jsonObject.put("ruleGroup",ruleGroup);
        jsonObject.put("ruleGroupCount",ruleGroupCount);
        jsonObject.put("checkResults",checkResults);


        return Response.ok(jsonObject) ;
    }

}
