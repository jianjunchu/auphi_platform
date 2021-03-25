/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2018 by Auphi BI : http://www.doetl.com

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.firzjb.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.firzjb.base.common.Const;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.log.annotation.Log;
import org.firzjb.schedule.entity.JobDependencies;
import org.firzjb.schedule.mapper.JobDependenciesMapper;
import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.firzjb.schedule.model.request.JobDependenciesRequest;
import org.firzjb.schedule.model.request.ParamRequest;
import org.firzjb.schedule.model.response.GeneralScheduleResponse;
import org.firzjb.schedule.model.response.JobDependenciesResponse;
import org.firzjb.schedule.service.IDependScheduleService;
import org.firzjb.schedule.service.IGroupService;
import org.firzjb.schedule.util.QuartzUtil;
import org.firzjb.utils.BeanCopier;
import org.firzjb.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-10-02 19:45
 */
@Service
public class DependScheduleService implements IDependScheduleService {

    private  Logger logger = LoggerFactory.getLogger(DependScheduleService.class);

    @Autowired
    private Scheduler dependScheduler;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private JobDependenciesMapper jobDependenciesMapper;


    /**
     * 创建事件调度
     * @param request
     * @param jobExecClass
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "创建事件调度信息")
    @Override
    public  void create(GeneralScheduleRequest request,  Class<? extends Job> jobExecClass) throws SchedulerException {
        String jobName = request.getJobName();
        String group = request.getJobGroup();

        try {

            if(!checkJobExist(jobName,group)){
                // 获取事件调度器
                Scheduler sched = dependScheduler;
                // 创建一项作业
                JobDetail jobDetail = JobBuilder.newJob(jobExecClass)
                        .withIdentity(jobName, group).storeDurably()

                        .withDescription(request.getDescription()).build();
                JobDataMap data = jobDetail.getJobDataMap();
                data.put(Const.GENERAL_SCHEDULE_KEY, JSONObject.toJSONString(request));

                Trigger trigger = QuartzUtil.getTrigger(request,group);

                // 告诉事件调度器使用该触发器来安排作业
                sched.scheduleJob(jobDetail, trigger);


                if(request.getDependencies()!=null && request.getDependencies().size()>0){
                    for(JobDependenciesRequest jobDependenciesRequest : request.getDependencies()){
                        if(StringUtils.isEmpty(jobDependenciesRequest.getStartJobGroup())){
                            jobDependenciesRequest.setStartJobGroup(group);
                        }
                        if(StringUtils.isEmpty(jobDependenciesRequest.getNextJobGroup())){
                            jobDependenciesRequest.setNextJobGroup(group);
                        }
                        JobDependencies dependencies =   BeanCopier.copy(jobDependenciesRequest, JobDependencies.class);
                        dependencies.setJobGroup(trigger.getJobKey().getGroup());
                        dependencies.setJobName(trigger.getJobKey().getName());
                        dependencies.setSchedName("dependScheduler");

                        int count =  jobDependenciesMapper.selectCount(new EntityWrapper<JobDependencies>()
                                .eq("SCHED_NAME","dependScheduler")
                                .eq("JOB_NAME",dependencies.getJobName())
                                .eq("JOB_GROUP",dependencies.getJobGroup())
                                .eq("START_JOB_GROUP",dependencies.getStartJobGroup())
                                .eq("START_JOB_NAME",dependencies.getStartJobName())
                                .eq("NEXT_JOB_GROUP",dependencies.getNextJobGroup())
                                .eq("NEXT_JOB_NAME",dependencies.getNextJobGroup()));

                        if(count==0){
                            dependencies.insert();
                        }
                    }

                }



            }else{
                throw new ApplicationException(StatusCode.CONFLICT.getCode(), "保存失败!调度已存在!");
            }

        }catch (ApplicationException ex){
            throw ex;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ApplicationException(StatusCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getCode(), "保存失败!调度周期没有合法的的触发时间");
        }




    }



    /**
     * 根据事件调度名称获取事件调度详细信息
     * @param jobName 事件调度名称
     * @return JobDetail 事件调度详细信息
     */
    @Override
    public GeneralScheduleResponse findByName(String jobName, String groupName){
        try {
            JobKey tk = JobKey.jobKey(jobName, groupName);


            JobDetail jobDetail =  dependScheduler.getJobDetail(tk);

            String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);

            GeneralScheduleResponse response = JSON.parseObject(json,GeneralScheduleResponse.class);

            List<JobDependencies> list =  jobDependenciesMapper.selectList(new EntityWrapper<JobDependencies>()
                    .eq("SCHED_NAME","dependScheduler")
                    .eq("JOB_NAME",tk.getName())
                    .eq("JOB_GROUP",tk.getGroup()));

            response.setDependencies(BeanCopier.copy(list, JobDependenciesResponse.class));

            return response;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 根据作业名删除作业
     * @param jobName
     * @param group
     * @param organizerId
     * @return
     */
    @Log(module = "调度管理",description = "删除事件调度信息")
    @Override
    public boolean removeJob(String jobName, String group, Long organizerId) throws SchedulerException {

        TriggerKey tk = TriggerKey.triggerKey(jobName, group);
        dependScheduler.pauseTrigger(tk);//停止触发器  
        dependScheduler.unscheduleJob(tk);//移除触发器
        JobKey jobKey = JobKey.jobKey(jobName, group);
        dependScheduler.deleteJob(jobKey);//删除作业

        jobDependenciesMapper.delete(new EntityWrapper<JobDependencies>()
                .eq("SCHED_NAME","dependScheduler")
                .eq("JOB_NAME",jobKey.getName())
                .eq("JOB_GROUP",jobKey.getGroup()));

        logger.info("删除事件调度=> [作业名称：" + jobName + " 作业组：" + group + "] ");
        return true;
    }




    /**
     * 执行事件调度
     * @param jobName 事件调度名称
     * @param jobGroup
     * @param params
     * @return
     */
    @Log(module = "调度管理",description = "手动执行事件调度")
    @Override
    public  boolean execute(String jobName, String jobGroup, ParamRequest[] params) throws SchedulerException {

        logger.info("执行作业=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        dependScheduler.triggerJob(jk) ;


        logger.info("执行事件调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        return true;

    }

    /**
     * 暂停事件调度
     * @param jobName
     * @param jobGroup
     * @return
     */
    @Log(module = "调度管理",description = "暂停事件调度")
    @Override
    public  boolean pause(String jobName, String jobGroup) throws SchedulerException {
        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        dependScheduler.pauseJob(jk);
        logger.info("暂停事件调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        return true;
    }

    /**
     * 还原事件调度
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "还原事件调度")
    @Override
    public  boolean resume(String jobName, String jobGroup) throws SchedulerException {
        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        dependScheduler.resumeJob(jk);
        logger.info("还原事件调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        return true;
    }

    /**
     * 检查事件调度是否存在
     * @param jobName 事件调度名称
     * @return boolean 事件调度是否存在
     */
    @Override
    public  boolean checkJobExist(String jobName, String jobGroup){
        try{
            JobKey jk = JobKey.jobKey(jobName,jobGroup);
            JobDetail job = dependScheduler.getJobDetail(jk);
            return job!=null;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 更新事件调度信息
     * @param request
     * @param jobExecClass
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "更新事件调度信息")
    @Override
    public void update(GeneralScheduleRequest request, Class<? extends Job> jobExecClass) throws SchedulerException {

        JobDetail oJobDetail = null;
        Trigger oTrigger = null;

        try {
            if(checkJobExist(request.getOriginalJobName(),request.getOriginalJobGroup())){
                oJobDetail = findDetailByName(request.getOriginalJobName(),request.getOriginalJobGroup());
                TriggerKey tk = TriggerKey.triggerKey(request.getOriginalJobName(), request.getOriginalJobGroup());
                oTrigger =  dependScheduler.getTrigger(tk);
                removeJob(request.getOriginalJobName(),request.getOriginalJobGroup(), request.getOrganizerId());
            }
            create(request,jobExecClass);


        }catch (ApplicationException ex){
            if(oJobDetail!=null && oTrigger!=null){
                dependScheduler.scheduleJob(oJobDetail, oTrigger);
            }
            throw ex;
        }catch (Exception e){
            if(oJobDetail!=null && oTrigger!=null){
                dependScheduler.scheduleJob(oJobDetail, oTrigger);
            }
            throw new ApplicationException(StatusCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getCode(),"保存失败!调度周期没有合法的的触发时间");
        }
    }

    @Override
    public JobDetail findDetailByName(String jobName, String groupName) {

        try {
            JobKey tk = JobKey.jobKey(jobName, groupName);
            return dependScheduler.getJobDetail(tk);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 获取事件调度
     * @param group
     * @param name
     * @return
     */
    @Override
    public Map<JobKey,ArrayList<JobKey>> getDependJobKeyTree(String group, String name) {

        Map<JobKey,ArrayList<JobKey>> dependency = new HashMap<JobKey,ArrayList<JobKey>>() ;
        List<JobDependencies> list =  jobDependenciesMapper.selectList(new EntityWrapper<JobDependencies>()
                .eq("SCHED_NAME","dependScheduler")
                .eq("JOB_NAME",name)
                .eq("JOB_GROUP",group));
        for(JobDependencies jobDependencies : list){

            JobKey startKey = JobKey.jobKey(jobDependencies.getStartJobName(),jobDependencies.getStartJobGroup());
            JobKey nextKey = JobKey.jobKey(jobDependencies.getNextJobName(),jobDependencies.getNextJobGroup());

            ArrayList<JobKey> dp = dependency.get(startKey) ;
            if(dp == null){
                dp = new ArrayList<>() ;
                dependency.put(startKey, dp) ;
            }

            if(nextKey != null && nextKey.toString()!=null && !StringUtils.isEmpty(nextKey.toString())
                    && !dp.contains(nextKey))
                dp.add(nextKey) ;
        }


        return dependency;
    }

    /**
     *
     * @param jobName
     * @param jobGrou
     * @return
     */
    @Override
    public Boolean isDependSchedule(String jobName, String jobGrou) {
        return checkJobExist(jobName,jobGrou);
    }


}
