package com.aofei.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.aofei.base.common.Const;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.schedule.entity.JobDetails;
import com.aofei.schedule.job.JobRunner;
import com.aofei.schedule.job.TransRunner;
import com.aofei.schedule.mapper.JobDetailsMapper;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import com.aofei.schedule.model.request.JobDetailsRequest;
import com.aofei.schedule.model.request.MonitorRequest;
import com.aofei.schedule.model.response.GeneralScheduleResponse;
import com.aofei.schedule.model.response.JobDetailsResponse;
import com.aofei.schedule.model.response.JobPlanResponse;
import com.aofei.schedule.model.response.MonitorResponse;
import com.aofei.schedule.service.IJobDetailsService;
import com.aofei.schedule.service.IMonitorService;
import com.aofei.schedule.service.IQuartzService;
import com.aofei.schedule.util.QuartzUtil;
import com.aofei.utils.BeanCopier;
import com.aofei.utils.DateUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-11-18
 */
@Service
public class JobDetailsService extends BaseService<JobDetailsMapper, JobDetails> implements IJobDetailsService {

    @Autowired
    private IQuartzService quartzService;

    @Autowired
    private IMonitorService monitorService;
    /**
     * 任务调度查询
     * @param page
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "任务调度查询")
    @Override
    public Page<JobDetailsResponse> getPage(Page<JobDetails> page, JobDetailsRequest request) {
        List<JobDetails> list = baseMapper.findList(page, request);
        for(JobDetails jobDetails : list){
            if(jobDetails.getStartTime() == null || jobDetails.getStartTime()==0){
                JobDetail jobDetail =  quartzService.findByName(jobDetails.getJobName(),jobDetails.getJobGroup());
                String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);
                GeneralScheduleResponse response = JSON.parseObject(json,GeneralScheduleResponse.class);
                jobDetails.setStartTime(response.getStartTime().getTime());
                jobDetails.setEndTime(response.getEndTime().getTime());

            }
        }

        page.setRecords(list);
        return convert(page, JobDetailsResponse.class);
    }

    /**
     * 创建调度
     * @param request
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "创建调度")
    @Override
    public boolean save(GeneralScheduleRequest request) throws ParseException, SchedulerException {
        Date satrtDate = request.getStartTime();
        Date endDate = request.getEndTime();

        if(satrtDate==null){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"请选择开始时间");
        }
        if(endDate==null){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"请选择结束时间");
        }


        if(satrtDate.getTime() - new Date().getTime() < 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"开始时间必须大于当前时间");
        }
        if(satrtDate.getTime() - endDate.getTime() > 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"结束时间必须大于开始时间");
        }

        Class quartzExecuteClass = null;
        if("JOB".equalsIgnoreCase(request.getFileType())){
            quartzExecuteClass = JobRunner.class;
        }else if("TRANSFORMATION".equalsIgnoreCase(request.getFileType())){
            quartzExecuteClass = TransRunner.class;
        }


        quartzService.create(request ,quartzExecuteClass);

        return true;
    }

    /**
     * 更新调度
     * @param request
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "更新调度")
    @Override
    public boolean update(GeneralScheduleRequest request) throws ParseException, SchedulerException {

        Date satrtDate = request.getStartTime();
        Date endDate = request.getEndTime();

        if(satrtDate==null){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"请选择开始时间");
        }
        if(endDate==null){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"请选择结束时间");
        }


        if(satrtDate.getTime() - new Date().getTime() < 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"开始时间必须大于当前时间");
        }
        if(satrtDate.getTime() - endDate.getTime() > 0){
            throw  new ApplicationException(StatusCode.NOT_FOUND.getCode(),"结束时间必须大于开始时间");
        }

        Class quartzExecuteClass = null;
        if("JOB".equalsIgnoreCase(request.getFileType())){
            quartzExecuteClass = JobRunner.class;
        }else if("TRANSFORMATION".equalsIgnoreCase(request.getFileType())){
            quartzExecuteClass = TransRunner.class;
        }
        quartzService.update(request, quartzExecuteClass);

        return true;
    }

    /**
     * 删除调度
     * @param request
     * @return
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "删除调度")
    @Override
    public boolean del(GeneralScheduleRequest request) throws SchedulerException {
        return quartzService.removeJob(request.getJobName(),request.getJobGroup(),request.getOrganizerId());
    }

    /**
     * 暂停调度
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "暂停调度")
    @Override
    public boolean pause(GeneralScheduleRequest request) throws SchedulerException {
        return quartzService.pause(request.getJobName(),request.getJobGroup());
    }

    /**
     * 恢复暂停的调度
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "恢复暂停的调度")
    @Override
    public boolean resume(GeneralScheduleRequest request) throws SchedulerException {
        return quartzService.resume(request.getJobName(),request.getJobGroup());
    }

    /**
     * 手动执行调度
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "手动执行调度")
    @Override
    public boolean execute(GeneralScheduleRequest request) throws SchedulerException {
        return quartzService.execute(request.getJobName(),request.getJobGroup(),null);
    }

    /**
     * 获取调度详情
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "手动执行调度")
    @Override
    public JobDetail findByName(GeneralScheduleRequest request) {
        return quartzService.findByName(request.getJobName(),request.getJobGroup());
    }

    /**
     *
     * @param request
     * @return
     */
    @Log(module = "调度管理",description = "查询调度列表")
    @Override
    public List<JobDetailsResponse> listAll(JobDetailsRequest request) {
        List<JobDetails> list = baseMapper.findList(request);
        return BeanCopier.copy(list, JobDetailsResponse.class);
    }

    @Log(module = "调度管理",description = "查询调度计划")
    @Override
    public List<JobPlanResponse> listJobPlan(JobDetailsRequest request) throws ParseException {

        List<JobPlanResponse> responses = new ArrayList<>();

        List<JobDetails> list = baseMapper.findList(request);

       long now = System.currentTimeMillis();

        for(JobDetails jobDetails : list){

            JobDetail jobDetail =  quartzService.findByName(jobDetails.getJobName(),jobDetails.getJobGroup());
            String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);
            GeneralScheduleRequest generalScheduleRequest = JSON.parseObject(json,GeneralScheduleRequest.class);

            if( generalScheduleRequest.getStartTime().getTime() < DateUtils.endOfDay(new Date()).getTime()  ){

                String cron = QuartzUtil.getCron(generalScheduleRequest);
                List<Date> dates = QuartzUtil.getQuartzPlan(cron,generalScheduleRequest.getStartTime());
                for(Date date : dates){

                    JobPlanResponse response = new JobPlanResponse();
                    response.setPlanRunTime(date);
                    response.setPlanRunTimeStr(DateUtils.toYmdHms(date));
                    response.setQrtzJobName(jobDetails.getJobName());
                    response.setQrtzJobGroup(jobDetails.getGroupName());
                    response.setFileType(generalScheduleRequest.getFileType());
                    if(date.getTime() > now){
                        response.setStatus("waiting");
                    }else{
                        MonitorRequest monitorRequest = new MonitorRequest();
                        monitorRequest.setQrtzJobGroup(generalScheduleRequest.getJobGroup());
                        monitorRequest.setQrtzJobName(generalScheduleRequest.getJobName());
                        monitorRequest.setFireTime(date);
                        MonitorResponse monitorResponse = monitorService.getPlanMonitor(monitorRequest);
                        if(monitorResponse!=null){
                            response.setStartTime(monitorResponse.getStartdate());
                            response.setEndTime(monitorResponse.getEnddate());
                            response.setStatus(monitorResponse.getStatus());
                            response.setErrors(monitorResponse.getErrors());
                            response.setLogId(monitorResponse.getId());
                        }else{
                            response.setStatus("lose");
                        }
                    }
                    responses.add(response);

                }
            }


        }


        return responses;
    }
}
