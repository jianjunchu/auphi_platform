package com.aofei.schedule.controller;

import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.schedule.job.DependRunner;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import com.aofei.schedule.model.request.JobDependenciesRequest;
import com.aofei.schedule.model.request.JobDetailsRequest;
import com.aofei.schedule.model.response.GeneralScheduleResponse;
import com.aofei.schedule.model.response.GroupResponse;
import com.aofei.schedule.model.response.JobDetailsResponse;
import com.aofei.schedule.model.response.JobPlanResponse;
import com.aofei.schedule.service.IDependScheduleService;
import com.aofei.schedule.service.IGroupService;
import com.aofei.schedule.service.IJobDetailsService;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.*;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.util.*;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-09-22 15:39
 */
@Api(tags = { "调度管理-事件调度" })
@Authorization
@RestController
@RequestMapping(value = "/schedule/depend", produces = {"application/json;charset=UTF-8"})
public class DependScheduleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DependScheduleController.class);

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IDependScheduleService dependScheduleService;


    @Autowired
    private IJobDetailsService jobDetailsService;
    /**
     * 资源库列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "周期调度列表(分页查询)", notes = "周期调度列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "jobGroup", value = "分组(模糊查询)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobName", value = "调度名称(模糊查询)", paramType = "query", dataType = "String")

    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<JobDetailsResponse>> page(
                            @ApiIgnore JobDetailsRequest request,
                            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        request.setSchedName("dependScheduler");
        Page<JobDetailsResponse> page = jobDetailsService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }





    /**
     * 新建调度
     * @param request
     * @return
     */
    @ApiOperation(value = "添加普通调度", notes = "" +
            "<strong>jobGroup</strong> (string): 分组ID ,</br>" +
            "<strong>jobName</strong> (string): 调度名称 ,</br>" +
            "<strong>description</strong> (string): 描述 ,</br>" +
            "<strong>repository</strong> (string): 资源库名称 ,</br>" +
            "<strong>startTime</strong> (string): 开始时间(格式:HH:mm:ss) </br>" +
            "<strong>startDate</strong> (string): 开始日期(格式:yyyy-MM-dd) </br>" +
            "<strong>cycle</strong> (integer): 周期(1:执行一次;2:秒;3:分钟;4:小时;5:天;6:周;7:月;8:年;) </br>" +
            "<strong>cycleNum </strong>(string): 周期模式; </br>" +
            "  1:执行一次:cycleNum为空 </br>" +
            "  2:秒:cycleNum为整数;表示几秒执行一次 </br>" +
            "  3:分钟:cycleNum为整数;表示几分钟享执行一次 </br>" +
            "  4:小时:cycleNum为整数;表示几小时享执行一次 </br>" +
            "  5:天:cycleNum为整数;表示几天执行一次;-1表示每个工作日执行 </br>" +
            "  6:周:cycleNum为整数;1-7分别表示 周日-周六(周日开始) </br>" +
            "  7:月:分两种情况,第一种:用1-31表示每月的几号执行,L表示最后一天 第二种: cycleNum为空,用weekNum,dayNum表示每月的第几个星期的星期几 </br>" +
            "  8:年:分两种情况,第一种:用格式MM-dd表示每年的几月几号执行 第二种:cycleNum为空,用monthNum,weekNum,dayNum表示每年第几个月的第几个星期的星期几 </br>" +
            "<strong>dayType</strong> (integer): 周期为天时区分类型,</br>dayType=1:表示按天周期执行 </br>dayType=2:只在工作日执行</br>" +
            "<strong>monthType</strong> (integer): 周期为月时区分类型,</br>dayType=1:用1-31表示每月的几号执行,L表示最后一天 </br>dayType=2: cycleNum为空,用weekNum,dayNum表示每月的第几个星期的星期几;</br>" +
            "<strong>yearType</strong> (integer): 周期为年时区分类型,</br>yearType=1:用格式MM-dd表示每年的几月几号执行 </br>yearType=2:cycleNum为空,用monthNum,weekNum,dayNum表示每年第几个月的第几个星期的星期几</br>" +
            "<strong>monthNum</strong> (integer): 1-12表示1-12月份;</br>" +
            "<strong>weekNum</strong> (integer): 第几个星期 1-4表示 L表示最后一个星期</br>"+
            "<strong>dayNum</strong> (integer): 1-7分别表示 周日-周六(周日开始);和weekNum同时使用表示第几个星期的星期几 </br>" +
            "<strong>endDate</strong> (string): 结束日期(格式:yyyy-MM-dd);结束日期为空,表示永不结束;周期为执行一次结束时间为空 </br>" +
            "<strong>errorNoticeUser</strong> (string): 发送错误通知用户(用户名,多用户用','分割) </br>" +
            "<strong>execType</strong> (integer): 运行方式(1:本地运行,2:远程运行,3:集群运行;4:HA集群运行) </br>" +
            "<strong>file</strong> (string): 执行的转换或者作业名 ,</br>" +
            "<strong>filePath</strong> (string): 执行的转换或者作业path </br>" +
            "<strong>fileType</strong> (string): TRANSFORMATION or JOB </br>" +
            "<strong>version</strong> (string): 版本(固定值v3.9) ,</br>" ,httpMethod = "POST")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<Boolean> add(
            @RequestBody GeneralScheduleRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException, ParseException {



        if(validate(request,user)){
            request.setUsername(user.getUsername());
            request.setOrganizerId(user.getOrganizerId());

            dependScheduleService.create(request ,DependRunner.class);
        }



        return Response.ok(true) ;
    }

    /**
     * 新建调度
     * @param request
     * @return
     */
    @ApiOperation(value = "添加普通调度", notes = "" +
            "<strong>jobGroup</strong> (string): 分组ID ,</br>" +
            "<strong>jobName</strong> (string): 调度名称 ,</br>" +
            "<strong>description</strong> (string): 备注 ,</br>" +
            "<strong>repository</strong> (string): 资源库名称 ,</br>" +
            "<strong>startTime</strong> (string): 开始时间(格式:HH:mm:ss) </br>" +
            "<strong>startDate</strong> (string): 开始日期(格式:yyyy-MM-dd) </br>" +
            "<strong>cycle</strong> (integer): 周期(1:执行一次;2:秒;3:分钟;4:小时;5:天;6:周;7:月;8:年;) </br>" +
            "<strong>cycleNum </strong>(string): 周期模式; </br>" +
            "  1:执行一次:cycleNum为空 </br>" +
            "  2:秒:cycleNum为整数;表示几秒执行一次 </br>" +
            "  3:分钟:cycleNum为整数;表示几分钟享执行一次 </br>" +
            "  4:小时:cycleNum为整数;表示几小时享执行一次 </br>" +
            "  5:天:cycleNum为整数;表示几天执行一次;-1表示每个工作日执行 </br>" +
            "  6:周:cycleNum为整数;1-7分别表示 周日-周六(周日开始) </br>" +
            "  7:月:分两种情况,第一种:用1-31表示每月的几号执行,L表示最后一天 第二种: cycleNum为空,用weekNum,dayNum表示每月的第几个星期的星期几 </br>" +
            "  8:年:分两种情况,第一种:用格式MM-dd表示每年的几月几号执行 第二种:cycleNum为空,用monthNum,weekNum,dayNum表示每年第几个月的第几个星期的星期几 </br>" +
            "<strong>dayType</strong> (integer): 周期为天时区分类型,</br>dayType=1:表示按天周期执行 </br>dayType=2:只在工作日执行</br>" +
            "<strong>monthType</strong> (integer): 周期为月时区分类型,</br>dayType=1:用1-31表示每月的几号执行,L表示最后一天 </br>dayType=2: cycleNum为空,用weekNum,dayNum表示每月的第几个星期的星期几;</br>" +
            "<strong>yearType</strong> (integer): 周期为年时区分类型,</br>yearType=1:用格式MM-dd表示每年的几月几号执行 </br>yearType=2:cycleNum为空,用monthNum,weekNum,dayNum表示每年第几个月的第几个星期的星期几</br>" +
            "<strong>monthNum</strong> (integer): 1-12表示1-12月份;</br>" +
            "<strong>weekNum</strong> (integer): 第几个星期 1-4表示 L表示最后一个星期</br>"+
            "<strong>dayNum</strong> (integer): 1-7分别表示 周日-周六(周日开始);和weekNum同时使用表示第几个星期的星期几 </br>" +
            "<strong>endDate</strong> (string): 结束日期(格式:yyyy-MM-dd);结束日期为空,表示永不结束;周期为执行一次结束时间为空 </br>" +
            "<strong>errorNoticeUser</strong> (string): 发送错误通知用户 ,</br>" +
            "<strong>execType</strong> (integer): 运行方式(1:本地运行,2:远程运行,3:集群运行;4:HA集群运行) </br>" +
            "<strong>file</strong> (string): 执行的转换或者作业名 ,</br>" +
            "<strong>filePath</strong> (string): 执行的转换或者作业path </br>" +
            "<strong>fileType</strong> (string): TRANSFORMATION or JOB </br>" +
            "<strong>version</strong> (string): 版本(固定值v3.9) ,</br>" ,httpMethod = "POST")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<Boolean> edit(
            @RequestBody GeneralScheduleRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException, ParseException {


        if(validate(request,user)){
            dependScheduleService.update(request, DependRunner.class);
        }
        return Response.ok(true) ;

    }


    private boolean validate(GeneralScheduleRequest request,CurrentUserResponse user){

        if(StringUtils.isEmpty(request.getJobGroup())){
            GroupResponse groupResponse = groupService.getDefaultGroup(user.getOrganizerId());
            request.setJobGroup(groupResponse.getGroupId());
        }

        Date satrtDate = request.getStartTime();
        Date endDate = request.getEndTime();

        if(satrtDate==null){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"请选择开始时间");
        }


        if(satrtDate.getTime() - new Date().getTime() < 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"开始时间必须大于当前时间");
        }
        if(endDate!=null && satrtDate.getTime() - endDate.getTime() > 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"结束时间必须大于开始时间");
        }

        Set<String> stringSet=new HashSet<>();

        if(request.getDependencies()!=null){
            for(JobDependenciesRequest dependenciesRequest:request.getDependencies()){
                String name = dependenciesRequest.getStartJobGroup()+"_"+dependenciesRequest.getStartJobName()
                        +"_"+dependenciesRequest.getNextJobGroup()+"_"+dependenciesRequest.getNextJobName();

                String name2 = dependenciesRequest.getNextJobGroup()+"_"+dependenciesRequest.getNextJobName()
                        +"_"+dependenciesRequest.getStartJobGroup()+"_"+dependenciesRequest.getStartJobName();

                boolean in = stringSet.add(name);
                if(!in){
                    throw  new ApplicationException(StatusCode.CONFLICT.getCode(),"保存失败!存在相同或冲突的依赖"+dependenciesRequest.getStartJobName()+"-"+dependenciesRequest.getNextJobName());
                }

                boolean in2 = stringSet.add(name2);
                if(!in2){
                    throw  new ApplicationException(StatusCode.CONFLICT.getCode(),"保存失败!存在相同或冲突的依赖"+dependenciesRequest.getStartJobName()+"-"+dependenciesRequest.getNextJobName());
                }
            }


        }



        return true;
    }

    @ApiOperation(value = "删除调度", notes = "删除调度", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{jobName}/group/{jobGroup}", method = RequestMethod.DELETE)
    public Response<Boolean> delete(
            @ApiParam(value = "调度名称", required = true)@PathVariable String jobName,
            @ApiParam(value = "调度分组ID", required = true)@PathVariable String jobGroup,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException {

        return Response.ok(dependScheduleService.removeJob(jobName,jobGroup,user.getOrganizerId())) ;
    }

    @ApiOperation(value = "暂停调度", notes = "暂停调度")
    @RequestMapping(value = "/pause/{jobName}/group/{jobGroup}", method = RequestMethod.GET)
    public Response<Boolean> pause(
            @ApiParam(value = "调度名称", required = true)@PathVariable String jobName,
            @ApiParam(value = "调度分组ID", required = true)@PathVariable String jobGroup,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException {

        return Response.ok(dependScheduleService.pause(jobName,jobGroup)) ;
    }

    @ApiOperation(value = "还原调度", notes = "还原暂停的调度")
    @RequestMapping(value = "/resume/{jobName}/group/{jobGroup}", method = RequestMethod.GET)
    public Response<Integer> resume(
            @ApiParam(value = "调度名称", required = true)@PathVariable String jobName,
            @ApiParam(value = "调度分组ID", required = true)@PathVariable String jobGroup,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException {

        return Response.ok(dependScheduleService.resume(jobName,jobGroup)) ;
    }

    @ApiOperation(value = "手动执行调度", notes = "手动执行调度")
    @RequestMapping(value = "/execute/{jobName}/group/{jobGroup}", method = RequestMethod.GET)
    public Response<Integer> execute(
            @ApiParam(value = "调度名称", required = true)@PathVariable String jobName,
            @ApiParam(value = "调度分组ID", required = true)@PathVariable String jobGroup,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException {

        return Response.ok(dependScheduleService.execute(jobName,jobGroup,null)) ;
    }


    @ApiOperation(value = "获取调度详情", notes = "获取调度详情")
    @RequestMapping(value = "/{jobName}/group/{jobGroup}", method = RequestMethod.GET)
    public Response<GeneralScheduleResponse> get(
            @ApiParam(value = "调度名称", required = true)@PathVariable String jobName,
            @ApiParam(value = "调度分组ID", required = true)@PathVariable String jobGroup,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws SchedulerException {

        GeneralScheduleResponse response = dependScheduleService.findByName(jobName,jobGroup);


        return Response.ok(response) ;
    }

    @ApiOperation(value = "调度计划", notes = "当日调度计划清单列表")
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public Response<List<JobPlanResponse>> plan(
            @ApiIgnore JobDetailsRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws ParseException {

        request.setOrganizerId(user.getOrganizerId());
        List<JobPlanResponse> page = jobDetailsService.listJobPlan(request);
        Collections.sort(page); // 按年龄排序


        return Response.ok(page) ;
    }

}
