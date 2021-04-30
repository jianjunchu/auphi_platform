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

import com.alibaba.fastjson.JSONObject;
import org.firzjb.base.common.Const;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.log.annotation.Log;
import org.firzjb.schedule.entity.JobDependencies;
import org.firzjb.schedule.mapper.JobDependenciesMapper;
import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.firzjb.schedule.model.request.ParamRequest;
import org.firzjb.schedule.service.ICycleScheduleService;
import org.firzjb.schedule.util.QuartzUtil;
import org.firzjb.utils.DateUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-10-02 19:45
 */
@Service
public class CycleScheduleService implements ICycleScheduleService {

    private  Logger logger = LoggerFactory.getLogger(CycleScheduleService.class);

    @Autowired
    private Scheduler quartzScheduler;

    @Autowired
    private JobDependenciesMapper jobDependenciesMapper;




    /**
     * 创建周期调度
     * @param request
     * @param jobExecClass
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "创建周期调度信息")
    @Override
    public  void create(GeneralScheduleRequest request,  Class<? extends Job> jobExecClass) throws SchedulerException {
        try {
            String jobName = request.getJobName();
            String group = request.getJobGroup();
            if(!request.getFilePath().contains(request.getOrganizerId().toString())){
                request.setFilePath(Const.getUserPath(request.getOrganizerId(),request.getFilePath()) );
            }
            if(!checkJobExist(jobName,group)){
                // 获取周期调度器
                Scheduler sched = quartzScheduler;

                // 创建一项作业
                JobDetail jobDetail = JobBuilder.newJob(jobExecClass)
                        .withIdentity(jobName, group).storeDurably()

                        .withDescription(request.getDescription()).build();

                JobDataMap data = jobDetail.getJobDataMap();

                data.put(Const.GENERAL_SCHEDULE_KEY, JSONObject.toJSONString(request));

                data.put("isFastConfig", false);


                data.put("background_action_name", "");
                data.put("processId", QuartzUtil.class.getName()); //$NON-NLS-1$
                data.put("background_user_name", "");
                //data.put("background_output_location", "background/" + StringUtil.createNumberString(16)); //$NON-NLS-1$
                data.put("background_submit_time", DateUtils.toYmd(DateTime.now().toDate()));

                // This tells our execution component (QuartzExecute) that we're running
                // a background job instead of
                // a standard quartz execution.
                data.put("backgroundExecution", "true"); //$NON-NLS-1$

                Trigger trigger = QuartzUtil.getTrigger(request,group);
                // 告诉周期调度器使用该触发器来安排作业
                sched.scheduleJob(jobDetail, trigger);

            }else{
                throw new ApplicationException(StatusCode.CONFLICT.getCode(), "保存失败!调度已存在!");
            }

        } catch (ApplicationException ex){
            throw ex;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ApplicationException(StatusCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getCode(), "保存失败!调度周期没有合法的的触发时间");
        }

    }



    /**
     * 根据周期调度名称获取周期调度详细信息
     * @param jobName 周期调度名称
     * @return JobDetail 周期调度详细信息
     */
    @Override
    public JobDetail findByName(String jobName, String groupName){
        try {
            JobKey tk = JobKey.jobKey(jobName, groupName);
            return quartzScheduler.getJobDetail(tk);
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
    @Log(module = "调度管理",description = "删除周期调度信息")
    @Override
    public boolean removeJob(String jobName, String group, Long organizerId) throws SchedulerException {

        TriggerKey tk = TriggerKey.triggerKey(jobName, group);
        quartzScheduler.pauseTrigger(tk);//停止触发器  
        quartzScheduler.unscheduleJob(tk);//移除触发器
        JobKey jobKey = JobKey.jobKey(jobName, group);
        quartzScheduler.deleteJob(jobKey);//删除作业

        logger.info("删除周期调度=> [作业名称：" + jobName + " 作业组：" + group + "] ");
        return true;
    }




    /**
     * 执行周期调度
     * @param jobName 周期调度名称
     * @param jobGroup
     * @param params
     * @return
     */
    @Log(module = "调度管理",description = "手动执行周期调度")
    @Override
    public  boolean execute(String jobName, String jobGroup, ParamRequest[] params) throws SchedulerException {

        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        try {
            quartzScheduler.triggerJob(jk) ;
            logger.info("执行周期调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
            return true;
        }catch (Exception e){
            throw new ApplicationException(e.getMessage());
        }

    }

    /**
     * 暂停周期调度
     * @param jobName
     * @param jobGroup
     * @return
     */
    @Log(module = "调度管理",description = "暂停周期调度")
    @Override
    public  boolean pause(String jobName, String jobGroup) throws SchedulerException {
        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        quartzScheduler.pauseJob(jk);
        logger.info("暂停周期调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        return true;
    }

    /**
     * 还原周期调度
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "还原周期调度")
    @Override
    public  boolean resume(String jobName, String jobGroup) throws SchedulerException {
        JobKey jk = JobKey.jobKey(jobName,jobGroup);
        quartzScheduler.resumeJob(jk);
        logger.info("还原周期调度=> [作业名称：" + jobName + " 作业组：" + jobGroup + "] ");
        return true;
    }

    /**
     * 检查周期调度是否存在
     * @param jobName 周期调度名称
     * @return boolean 周期调度是否存在
     */
    @Override
    public  boolean checkJobExist(String jobName, String jobGroup){
        try{
            JobKey jk = JobKey.jobKey(jobName,jobGroup);
            JobDetail job = quartzScheduler.getJobDetail(jk);
            return job!=null;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 更新周期调度信息
     * @param request
     * @param quartzExecuteClass
     * @throws SchedulerException
     */
    @Log(module = "调度管理",description = "更新周期调度信息")
    @Override
    public void update(GeneralScheduleRequest request, Class<Job> quartzExecuteClass) throws SchedulerException {

        JobDetail oJobDetail = null;
        Trigger oTrigger = null;

        try {
            if(checkJobExist(request.getOriginalJobName(),request.getOriginalJobGroup())){
                oJobDetail = findByName(request.getOriginalJobName(),request.getOriginalJobGroup());
                TriggerKey tk = TriggerKey.triggerKey(request.getOriginalJobName(), request.getOriginalJobGroup());
                oTrigger =  quartzScheduler.getTrigger(tk);
                removeJob(request.getOriginalJobName(),request.getOriginalJobGroup(), request.getOrganizerId());
            }
                create(request,quartzExecuteClass);

                List<JobDependencies> list = jobDependenciesMapper.selectList(new EntityWrapper<JobDependencies>()
                        .eq("START_JOB_GROUP",request.getOriginalJobGroup()).eq("START_JOB_NAME",request.getOriginalJobName()));
                for(JobDependencies dependencies:list){
                    dependencies.setStartJobGroup(request.getJobGroup());
                    dependencies.setStartJobName(request.getJobName());
                    dependencies.updateById();
                }

                list = jobDependenciesMapper.selectList(new EntityWrapper<JobDependencies>()
                        .eq("NEXT_JOB_GROUP",request.getOriginalJobGroup()).eq("NEXT_JOB_NAME",request.getOriginalJobName()));
                for(JobDependencies dependencies:list){
                    dependencies.setNextJobGroup(request.getJobGroup());
                    dependencies.setNextJobName(request.getJobName());
                    dependencies.updateById();
                }
        }catch (ApplicationException ex){
            if(oJobDetail!=null && oTrigger!=null){
                quartzScheduler.scheduleJob(oJobDetail, oTrigger);
            }
            throw ex;
        }catch (Exception e){
            if(oJobDetail!=null && oTrigger!=null){
                quartzScheduler.scheduleJob(oJobDetail, oTrigger);
            }
            throw new ApplicationException(StatusCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getCode(),"保存失败!调度周期没有合法的的触发时间");
        }


    }

    @Override
    public JobDetail getScheduleByName(String jobName) {

        return null;
    }

    @Override
    public boolean isRun(String jobName, String jobGroup) throws SchedulerException {
        List<JobExecutionContext> jobContexts = quartzScheduler.getCurrentlyExecutingJobs();
        for(JobExecutionContext context : jobContexts) {
            //请求停止的job服务存在的场合。
            JobKey jobKey = context.getJobDetail().getKey();
            if (jobName.equals(jobKey.getName()) && jobGroup.equals(jobKey.getGroup())) {
                return true;
            }
        }
        return false;
    }


}
