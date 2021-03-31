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
package org.firzjb.schedule.service;

import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.firzjb.schedule.model.request.ParamRequest;
import org.firzjb.schedule.model.response.GeneralScheduleResponse;
import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.firzjb.schedule.model.request.ParamRequest;
import org.firzjb.schedule.model.response.GeneralScheduleResponse;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * ${DESCRIPTION}
 *
 * @auther 傲飞数据整合平台
 * @create 2018-10-02 19:44
 */
public interface IDependScheduleService {

    void create(GeneralScheduleRequest request, Class<? extends Job> jobExecClass) throws SchedulerException, ParseException;


    /**
     * 根据调度名称获取调度详细信息
     * @param jobName 调度名称
     * @param groupName 调度分组
     * @return groupName 调度详细信息
     */
    GeneralScheduleResponse findByName(String jobName, String groupName);
    /**
     * 根据作业名删除作业
     * @param name
     * @param group
     * @param organizerId
     * @return
     */
    boolean removeJob(String name, String group, Long organizerId) throws SchedulerException;


    /**
     * 执行调度
     * @param jobname 调度名称
     * @param params
     */
    boolean execute(String jobname, String jobgroup, ParamRequest[] params) throws SchedulerException;

    /**
     * 暂停调度
     * @param name
     * @param jobgrou
     * @return
     */
    boolean pause(String name, String jobgrou) throws SchedulerException;

    /**
     * 还原调度
     * @param name
     * @param jobgrou
     * @return
     */
    boolean resume(String name, String jobgrou) throws SchedulerException;

    /**
     * 检查调度是否存在
     * @param jobName 调度名称
     * @return boolean 调度是否存在
     */
    boolean checkJobExist(String jobName, String jobgrou);

    /**
     * 修改调度信息
     * @param request
     * @param jobExecClass
     * @throws SchedulerException
     */
    void update(GeneralScheduleRequest request, Class<? extends Job> jobExecClass) throws SchedulerException, ParseException;

    /**
     * 获取事件调度
     * @param group
     * @param name
     * @return
     */
    Map<JobKey,ArrayList<JobKey>> getDependJobKeyTree(String group, String name);

    /**
     * 判断一个调度是否是事件调度
     * @param jobName
     * @param jobGrou
     * @return
     */
    Boolean isDependSchedule(String jobName, String jobGrou);

    JobDetail findDetailByName(String jobName, String groupName);
}