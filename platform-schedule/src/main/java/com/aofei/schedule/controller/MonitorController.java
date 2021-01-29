package com.aofei.schedule.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.joblog.entity.LogJob;
import com.aofei.joblog.service.ILogJobService;
import com.aofei.kettle.JobExecutor;
import com.aofei.kettle.TransExecutor;
import com.aofei.schedule.model.request.MonitorRequest;
import com.aofei.schedule.model.response.MonitorResponse;
import com.aofei.schedule.service.IMonitorService;
import com.aofei.translog.entity.LogTrans;
import com.aofei.translog.entity.LogTransStep;
import com.aofei.translog.service.ILogTransService;
import com.aofei.translog.service.ILogTransStepService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = { "调度管理-监控(调度执行日志)" })
@Authorization
@RestController
@RequestMapping(value = "/schedule/monitor", produces = {"application/json;charset=UTF-8"})
public class MonitorController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CycleScheduleController.class);

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private ILogJobService logJobService;

    @Autowired
    private ILogTransService logTransService;

    @Autowired
    private ILogTransStepService logTransStepService;
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
    public Response<DataGrid<MonitorResponse>> page(
            @ApiIgnore MonitorRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<MonitorResponse> page = monitorService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 调度执行日志日志
     * @param id
     * @param type
     * @return
     */
    @ApiOperation(value = "", notes = "调度执行日志日志")
    @RequestMapping(value = "/info/{id}/type/{type}", method = RequestMethod.GET)
    public Response<JSONObject> info(@PathVariable Long id, @PathVariable String type)  {
        JSONObject jsonObject = new JSONObject();

        if("JOB".equalsIgnoreCase(type)){
            LogJob logJob = logJobService.selectById(id);
            jsonObject.put("msg",logJob.getJobLog());
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("list",jsonArray);

        }else if("TRANSFORMATION".equalsIgnoreCase(type)){
            LogTrans logTrans = logTransService.selectById(id);
            jsonObject.put("msg",logTrans.getLoginfo());
            List<LogTransStep> list =  logTransStepService.selectList(new EntityWrapper<LogTransStep>().eq("LOG_TRANS_ID",logTrans.getLogTransId()));
            JSONArray jsonArray = new JSONArray();
            for (LogTransStep logTransStep : list){
                JSONObject item = new JSONObject();
                item.put("name",logTransStep.getStepname()+"("+logTransStep.getStepCopy()+")");

                item.put("step_copy",logTransStep.getStepCopy());
                item.put("lines_read",logTransStep.getLinesRead());
                item.put("lines_written",logTransStep.getLinesWritten());
                item.put("lines_updated",logTransStep.getLinesUpdated());
                item.put("lines_input",logTransStep.getLinesInput());
                item.put("lines_output",logTransStep.getLinesOutput());
                item.put("lines_rejected",logTransStep.getLinesRejected());
                item.put("errors",logTransStep.getErrors());
                item.put("status",logTransStep.getStatus());
                item.put("costtime",logTransStep.getCosttime());
                item.put("speed",logTransStep.getSpeed());
                jsonArray.add(item);
            }

            jsonObject.put("list",jsonArray);


        }



        return Response.ok(jsonObject);
    }

    /**
     * 调度执行日志日志
     * @param id
     * @param type
     * @return
     */
    @ApiOperation(value = "", notes = "停止调度")
    @RequestMapping(value = "/stop/{id}/type/{type}", method = RequestMethod.GET)
    public Response<Boolean> stop(@PathVariable Long id, @PathVariable String type)  {

        if("JOB".equalsIgnoreCase(type)){
            LogJob logJob = logJobService.selectById(id);
            JobExecutor.stop(logJob.getChannelId());
        }else if("TRANSFORMATION".equalsIgnoreCase(type)){
            LogTrans logTrans = logTransService.selectById(id);
            TransExecutor.stop(logTrans.getChannelId());
        }

        return Response.ok(true);
    }


}
