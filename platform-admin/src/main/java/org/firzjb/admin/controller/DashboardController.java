package org.firzjb.admin.controller;

import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.schedule.model.response.DashboardResponse;
import org.firzjb.schedule.service.IMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 首页
 * 仪表盘
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 15:45
 */
@Api(tags = { "首页-仪表盘" })
@Authorization
@RestController
@RequestMapping(value = "/dashboard", produces = {"application/json;charset=UTF-8"})
public class DashboardController extends BaseController {



    @Autowired
    private IMonitorService monitorService;


    /**
     * 首页
     * 仪表盘数据
     *     int runCount: 运行中作业
     *     int finishCount:运行完成作业;
     *     int allCount:总作业数
     *     int errorCount:错误作业数
     *     RunTimesResponse runTimes:作业耗时(前五)
     *     RunCountResponse runCounts:近七天完成数和错误数量
     * @param user 当前登录的用户
     * @return
     */
    @ApiOperation(value = "首页-仪表盘", notes = "首页-仪表盘")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Response<DashboardResponse> index(@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException {
        DashboardResponse response = monitorService.getDashboardCount(user);

        return Response.ok(response);
    }




}
