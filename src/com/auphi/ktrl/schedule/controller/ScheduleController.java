/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2017 by Auphi BI : http://www.doetl.com 

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
package com.auphi.ktrl.schedule.controller;

import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.ktrl.schedule.bean.ScheduleBean;
import com.auphi.ktrl.schedule.util.QuartzUtil;
import com.auphi.ktrl.schedule.util.ScheduleUtil;
import com.auphi.ktrl.system.repository.bean.RepositoryBean;
import com.auphi.ktrl.system.repository.util.RepositoryUtil;
import com.auphi.ktrl.system.user.bean.UserBean;
import com.auphi.ktrl.system.user.util.UMStatus;
import com.auphi.ktrl.system.user.util.UserUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 调度管理
 *
 * @auther Tony
 * @create 2017-05-02 20:35
 */
@RestController
@RequestMapping(value = "/schedule", produces = {"application/json;charset=UTF-8"})
public class ScheduleController extends BaseMultiActionController {

    private final static String INDEX = "admin/schedule/list";

    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){
        return new ModelAndView(INDEX);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jobName =  ServletRequestUtils.getStringParameter(request,"jobName",null);
        if(!StringUtils.isEmpty(jobName)){
            UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
            try {
                ScheduleBean scheduleBean = ScheduleUtil.getScheduleBeanByJobName(jobName, String.valueOf(userBean.getOrgId()));
                this.setOkTipMsg("success",scheduleBean,response);
            }catch (Exception e){
                this.setFailTipMsg(e.getMessage(),response);
            }
        }else{
            this.setFailTipMsg("jobname 不能为空！",response);
        }

    }

    /**
     * 获取出库批次号
     * @return
     */
    @ApiOperation(value = "新增普通调度", notes = "创建一个普通周期调度", httpMethod = "POST", response = Integer.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobname", value = "调度名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "描述", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "repository", value = "资源库名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "作业/转换名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filepath", value = "文件路径", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "execType", value = "执行方式", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "固定值：V3.0", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startdate", value = "开始日期(日期格式：yyyy-MM-dd)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "starttime", value = "开始时间(时间格式：HH:mm:SS)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "enddate", value = "结束日期(日期格式：yyyy-MM-dd)如果结束时间为空表示用不结束", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cycle", value = "周期(1=执行一次;2=秒;3=分钟;4=小时;5=天;6=周;7=月;8=年)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "cyclenum", value = "周期模式(间隔时间 )" +
                    "<br>cycle=6(周期为周)是可用 1=周日 2=周一。。。7=周六 多个日期用','如：1,2,3 " +
                    "<br>cycle=7 1-31表示每月的几号 L表示每月的最后一天" +
                    "<br>cycle=8 每年的执行日期(格式：MM-dd)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "daytype", value = "cycle=5(周期为天)是可用 0:取cyclenum 1:每个工作日", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "monthtype", value = "cycle=7(周期为月)是可用 0:每月的几号 1:每月的第几星期的星期几", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "weeknum", value = "cycle=7/8(周期为月/年)是可用 1-4表示第几个星期 L表示最后一个星期 ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "daynum", value = "cycle=7/8(周期为月/年)是可用 1-7 表示周日-周六   ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "yeartype", value = "cycle=8(周期为年)是可用 0:每月的几号 1:每年几月的第几星期的星期几", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "monthnum", value = "cycle=8(周期为年)是可用 每年的几月1-12表示", paramType = "query", dataType = "String"),

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
        try {
            ScheduleBean scheduleBean = ScheduleUtil.createScheduleBeanFromRequest(request, userBean);
            QuartzUtil.create(scheduleBean);
            this.setOkTipMsg("success",response);
        }catch (Exception e){
            this.setFailTipMsg(e.getMessage(),response);
        }

    }


    /**
     * 获取出库批次号
     * @return
     */
    @ApiOperation(value = "新增普通调度", notes = "创建一个普通周期调度", httpMethod = "POST", response = Integer.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobname", value = "调度名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "描述", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "repository", value = "资源库名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "作业/转换名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filepath", value = "文件路径", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "execType", value = "执行方式", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "固定值：V3.0", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startdate", value = "开始日期(日期格式：yyyy-MM-dd)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "starttime", value = "开始时间(时间格式：HH:mm:SS)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "enddate", value = "结束日期(日期格式：yyyy-MM-dd)如果结束时间为空表示用不结束", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cycle", value = "周期(1=执行一次;2=秒;3=分钟;4=小时;5=天;6=周;7=月;8=年)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "cyclenum", value = "周期模式(间隔时间 )" +
                    "<br>cycle=6(周期为周)是可用 1=周日 2=周一。。。7=周六 多个日期用','如：1,2,3 " +
                    "<br>cycle=7 1-31表示每月的几号 L表示每月的最后一天" +
                    "<br>cycle=8 每年的执行日期(格式：MM-dd)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "daytype", value = "cycle=5(周期为天)是可用 0:取cyclenum 1:每个工作日", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "monthtype", value = "cycle=7(周期为月)是可用 0:每月的几号 1:每月的第几星期的星期几", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "weeknum", value = "cycle=7/8(周期为月/年)是可用 1-4表示第几个星期 L表示最后一个星期 ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "daynum", value = "cycle=7/8(周期为月/年)是可用 1-7 表示周日-周六   ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "yeartype", value = "cycle=8(周期为年)是可用 0:每月的几号 1:每年几月的第几星期的星期几", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "monthnum", value = "cycle=8(周期为年)是可用 每年的几月1-12表示", paramType = "query", dataType = "String"),

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oriJobName =  ServletRequestUtils.getStringParameter(request,"oriJobName",null);
        if(StringUtils.isEmpty(oriJobName)){
            this.setFailTipMsg("原调度名称不能为空！",response);
        }
        UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
        try {
            ScheduleBean scheduleBean = ScheduleUtil.createScheduleBeanFromRequest(request, userBean);
            QuartzUtil.update(scheduleBean, oriJobName, userBean);
            this.setOkTipMsg("success",response);
        }catch (Exception e){
            this.setFailTipMsg(e.getMessage(),response);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jobNames =  ServletRequestUtils.getStringParameter(request,"jobNames",null);
        if(!StringUtils.isEmpty(jobNames)){
            UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
            try {
                QuartzUtil.delete(jobNames.split(","), userBean);
                this.setOkTipMsg("success",response);
            }catch (Exception e){
                this.setFailTipMsg(e.getMessage(),response);
            }
        }else{
            this.setFailTipMsg("jobname 不能为空！",response);
        }
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/run" , method = RequestMethod.POST)
    public void run(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jobNames =  ServletRequestUtils.getStringParameter(request,"jobNames",null);
        if(!StringUtils.isEmpty(jobNames)){
            UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
            try {
                QuartzUtil.execute(jobNames.split(","), userBean);
                this.setOkTipMsg("success",response);
            }catch (Exception e){
                this.setFailTipMsg(e.getMessage(),response);
            }
        }else{
            this.setFailTipMsg("jobNames 不能为空！",response);
        }
    }

    /**
     * 暂停
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/pause" , method = RequestMethod.POST)
    public void pause(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jobNames =  ServletRequestUtils.getStringParameter(request,"jobNames",null);
        if(!StringUtils.isEmpty(jobNames)){
            UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
            try {
                QuartzUtil.pause(jobNames.split(","), userBean);
                this.setOkTipMsg("success",response);
            }catch (Exception e){
                this.setFailTipMsg(e.getMessage(),response);
            }
        }else{
            this.setFailTipMsg("jobNames 不能为空！",response);
        }
    }

    /**
     * 恢复
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/resume" , method = RequestMethod.POST)
    public void resume(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jobNames =  ServletRequestUtils.getStringParameter(request,"jobNames",null);
        if(!StringUtils.isEmpty(jobNames)){
            UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
            try {
                QuartzUtil.resume(jobNames.split(","), userBean);
                this.setOkTipMsg("success",response);
            }catch (Exception e){
                this.setFailTipMsg(e.getMessage(),response);
            }
        }else{
            this.setFailTipMsg("jobNames 不能为空！",response);
        }
    }
}
