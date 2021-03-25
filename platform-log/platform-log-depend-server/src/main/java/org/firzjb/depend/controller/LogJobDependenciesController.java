package org.firzjb.depend.controller;


import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.base.model.vo.DataGrid;
import org.firzjb.depend.model.request.LogJobDependenciesRequest;
import org.firzjb.depend.model.response.LogJobDependenciesResponse;
import org.firzjb.depend.service.ILogJobDependenciesService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 事件调度日志
 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2021-02-03
 */

@Api(tags = { "调度管理-监控(事件调度执行日志)" })
@Authorization
@RestController
@RequestMapping(value = "/log/depend", produces = {"application/json;charset=UTF-8"})
public class LogJobDependenciesController extends BaseController {


    @Autowired
   private ILogJobDependenciesService logJobDependenciesService;

    /**
     * 资源库列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "调度执行日志列表(分页查询)", notes = "调度执行日志列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "jobGroup", value = "分组(模糊查询)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobName", value = "调度名称(模糊查询)", paramType = "query", dataType = "String")

    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<LogJobDependenciesResponse>> page(
            @ApiIgnore LogJobDependenciesRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        Page<LogJobDependenciesResponse> page = logJobDependenciesService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }



}

