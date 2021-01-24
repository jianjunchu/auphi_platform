package com.aofei.schedule.service;

import com.aofei.schedule.entity.JobDetails;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import com.aofei.schedule.model.request.JobDetailsRequest;
import com.aofei.schedule.model.response.JobDetailsResponse;
import com.aofei.schedule.model.response.JobPlanResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2018-11-18
 */
public interface IJobDetailsService extends IService<JobDetails> {

    /**
     * 周期调度
     * @param page
     * @param request
     * @return
     */
    Page<JobDetailsResponse> getPage(Page<JobDetails> page, JobDetailsRequest request);

    /**
     * 新建调度
     * @param request
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    boolean save(GeneralScheduleRequest request) throws ParseException, SchedulerException;

    /**
     * 修改调度信息
     * @param request
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    boolean update(GeneralScheduleRequest request) throws ParseException, SchedulerException;

    /**
     * 删除调度信息
     * @param request
     * @return
     * @throws SchedulerException
     */
    boolean del(GeneralScheduleRequest request) throws SchedulerException;

    /**
     * 暂停调度
     * @param request
     * @return
     * @throws SchedulerException
     */
    boolean pause(GeneralScheduleRequest request) throws SchedulerException;

    /**
     *还原调度
     * @param request
     * @return
     */
    boolean resume(GeneralScheduleRequest request) throws SchedulerException;

    /**
     * 手动执行调度
     * @param request
     * @return
     */
    boolean execute(GeneralScheduleRequest request) throws SchedulerException;

    /**
     * 获取调度详情
     * @param request
     * @return
     */
    JobDetail findByName(GeneralScheduleRequest request);

    /**
     * 获取调度列表
     * @param pagination
     * @param request
     * @return
     */
    List<JobDetailsResponse> listAll(JobDetailsRequest request);

    /**
     *  获取当天调度计划
     * @param request
     * @return
     */
    List<JobPlanResponse> listJobPlan(JobDetailsRequest request) throws ParseException;
}
