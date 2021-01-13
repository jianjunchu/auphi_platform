package com.aofei.admin.controller;

import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.schedule.model.response.DashboardResponse;
import com.aofei.schedule.service.IMonitorService;
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
