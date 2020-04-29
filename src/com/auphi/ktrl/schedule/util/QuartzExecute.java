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
package com.auphi.ktrl.schedule.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auphi.ktrl.conn.util.ConnectionPool;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.util.SnowflakeIdWorker;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;
import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.engine.impl.KettleEngineImpl2_3;
import com.auphi.ktrl.engine.impl.KettleEngineImpl4_3;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.schedule.bean.ScheduleBean;
import com.auphi.ktrl.schedule.template.Template;
import com.auphi.ktrl.schedule.template.TemplateFactory;
import com.auphi.ktrl.schedule.view.DispatchingModeView;
import com.auphi.ktrl.schedule.view.FastConfigView;
import com.auphi.ktrl.system.mail.util.MailUtil;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.StringUtil;

public class QuartzExecute implements Job {
	private static Logger logger = Logger.getLogger(QuartzExecute.class);

	public static final LogChannelInterface loggingObject = new LogChannel("QuartzExecute");

	private static Class<?> PKG = QuartzExecute.class;


	@Override
	public synchronized void execute(JobExecutionContext arg0) {
		// TODO Auto-generated method stub

		JobDetail jobDetail = arg0.getJobDetail();
		JobDataMap data = jobDetail.getJobDataMap();

		//执行前置脚本

		String beforeSell = data.getString("beforeSell");

		Boolean isFastConfig = data.getBoolean("isFastConfig");

		if(isFastConfig){
			executeFastConfig(data, jobDetail.getName());
		}else {
			ScheduleUtil.executeNormal(data, jobDetail.getName(), jobDetail.getGroup(),"");
		}
	}






	/**
	 * run as fastconfig
	 * @param data jobDataMap
	 */
	public void executeFastConfig(JobDataMap data, String jobDetailName){
		String fastConfigJson = data.getString("fastConfigJson");
		String fieldMappingJson = data.getString("fieldMappingJson");
		String dispatchingModeJosn = data.getString("dispatchingModeJosn");

		FastConfigView fastConfigView = JSON.parseObject(fastConfigJson, FastConfigView.class);
		DispatchingModeView dispatchingModeView= JSON.parseObject(dispatchingModeJosn, DispatchingModeView.class);

		String repName = "Default";
		//runmode:1 本地运行,2集群运行，对应到execType:1本地运行,4ha运行
		int execType = "1".equals(dispatchingModeView.getRunMode())?1:4;
		//目前还没有开放2集群运行，未设置ha
		String ha = dispatchingModeView.getRunCluster();
		String middlePath = "Template" + fastConfigView.getIdSourceType() + fastConfigView.getIdDestType() +"1";
		Integer id = Integer.parseInt(StringUtil.createNumberString(9));
		Date date =new Date();
		long time = System.currentTimeMillis()-24*60*60*1000;//yesterday
		date.setTime(time);
		try{
			Template template = TemplateFactory.createTemplate(repName, middlePath, date, false);
			if(template==null)
			{
				logger.error("template not found："+middlePath);
				return;
			} else{
				template.bind(fastConfigJson, fieldMappingJson);
			}
			MonitorUtil.addMonitorBeforeRun(id, jobDetailName, middlePath, data.getString("userId"), "", ha);

			if(ProcessUtil.runProcess(id,data.getString("beforeSell"))){
				KettleEnvironment.init();
				MonitorScheduleBean monitorScheduleBean = new MonitorScheduleBean();
				monitorScheduleBean.setId(id);
				monitorScheduleBean.setHaName(ha);

				template.execute(execType, monitorScheduleBean);
			}

		}catch (Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String errMsg = sw.toString();

			logger.error(e.getMessage(), e);
			MonitorUtil.updateMonitorAfterError(id, errMsg);

			String title = "[ScheduleError][" + StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "][" + jobDetailName + "]";
			String errorNoticeUserId = data.getString("errorNoticeUserId");
			String[] user_mails = UserUtil.getUserEmails(errorNoticeUserId);
			MailUtil.sendMail(user_mails, title, errMsg);
		}
	}
}
