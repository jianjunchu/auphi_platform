package org.firzjb.schedule.service;

import org.firzjb.schedule.entity.JobDetails;
import org.firzjb.schedule.model.request.JobDetailsRequest;
import org.firzjb.schedule.model.response.JobDetailsResponse;
import org.firzjb.schedule.model.response.JobPlanResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.schedule.entity.JobDetails;
import org.firzjb.schedule.model.request.JobDetailsRequest;
import org.firzjb.schedule.model.response.JobDetailsResponse;
import org.firzjb.schedule.model.response.JobPlanResponse;

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
     *  获取当天调度计划
     * @param request
     * @return
     */
    List<JobPlanResponse> listJobPlan(JobDetailsRequest request) throws ParseException;

    /**
     * 列表查询
     * @param request
     * @return
     */
    List<JobDetailsResponse> getJobDetails(JobDetailsRequest request);

    JobDetailsResponse getScheduleByName(String jobName);
}
