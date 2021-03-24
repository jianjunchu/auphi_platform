package com.aofei.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.aofei.base.common.Const;
import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.schedule.entity.JobDetails;
import com.aofei.schedule.mapper.JobDetailsMapper;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import com.aofei.schedule.model.request.JobDetailsRequest;
import com.aofei.schedule.model.request.MonitorRequest;
import com.aofei.schedule.model.response.GeneralScheduleResponse;
import com.aofei.schedule.model.response.JobDetailsResponse;
import com.aofei.schedule.model.response.JobPlanResponse;
import com.aofei.schedule.model.response.MonitorResponse;
import com.aofei.schedule.service.ICycleScheduleService;
import com.aofei.schedule.service.IJobDetailsService;
import com.aofei.schedule.service.IMonitorService;
import com.aofei.schedule.util.QuartzUtil;
import com.aofei.utils.BeanCopier;
import com.aofei.utils.DateUtils;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.quartz.JobDetail;
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
    private ICycleScheduleService cycleScheduleService;

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
                JobDetail jobDetail =  cycleScheduleService.findByName(jobDetails.getJobName(),jobDetails.getJobGroup());
                String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);
                GeneralScheduleResponse response = JSON.parseObject(json,GeneralScheduleResponse.class);
                jobDetails.setStartTime(response.getStartTime().getTime());
                if(response.getEndTime()!=null){
                    jobDetails.setEndTime(response.getEndTime().getTime());
                }


            }
        }

        page.setRecords(list);
        return convert(page, JobDetailsResponse.class);
    }




    @Log(module = "调度管理",description = "查询调度计划")
    @Override
    public List<JobPlanResponse> listJobPlan(JobDetailsRequest request) throws ParseException {

        List<JobPlanResponse> responses = new ArrayList<>();

        List<JobDetails> list = baseMapper.findList(request);

       long now = System.currentTimeMillis();

        for(JobDetails jobDetails : list){

            JobDetail jobDetail =  cycleScheduleService.findByName(jobDetails.getJobName(),jobDetails.getJobGroup());

            if(jobDetail!=null && jobDetail.getJobDataMap()!=null && jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY)!=null){


                String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);
                GeneralScheduleRequest generalScheduleRequest = JSON.parseObject(json,GeneralScheduleRequest.class);

                if( generalScheduleRequest.getStartTime().getTime() < DateUtils.endOfDay(new Date()).getTime()
                        && !"PAUSED".equalsIgnoreCase(jobDetails.getTriggerState())
                        && !StringUtils.isEmpty(jobDetails.getTriggerState())){

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



        }


        return responses;
    }

    /**
     * 列查询
     * @param request
     * @return
     */
    @Override
    public List<JobDetailsResponse> getJobDetails(JobDetailsRequest request) {
        List<JobDetails> list = baseMapper.findList(request);
        return BeanCopier.copy(list, JobDetailsResponse.class);
    }

    @Override
    public JobDetailsResponse getScheduleByName(String jobName) {
        JobDetailsRequest request = new JobDetailsRequest();
        request.setJobName(jobName);
        request.setSchedName("quartzScheduler");
        List<JobDetails> list = baseMapper.findList(request);
        if(list!=null && list.size()>0){
           return BeanCopier.copy(list.get(0), JobDetailsResponse.class);
        }

        return null;
    }
}