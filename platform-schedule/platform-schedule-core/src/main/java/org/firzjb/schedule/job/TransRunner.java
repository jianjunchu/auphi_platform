package org.firzjb.schedule.job;


import com.baomidou.mybatisplus.toolkit.IdWorker;
import lombok.SneakyThrows;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;
import org.firzjb.kettle.TransExecutor;
import org.firzjb.schedule.util.RunnerUtil;
import org.firzjb.translog.entity.LogTrans;
import org.firzjb.translog.task.TransLogTimerTask;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class TransRunner implements Job {


	public static Object lock = new Object();
	private static Logger logger = LoggerFactory.getLogger(TransRunner.class);
	private final Timer logTimer = new Timer();

	public TransExecutor  executeTrans(JobDetail jobDetail , Date fireTime) throws JobExecutionException {
		try {

			long logTransId = IdWorker.getId();
			initLog(logTransId);

			TransMeta transMeta = RunnerUtil.getTransMeta(jobDetail);

			TransExecutionConfiguration executionConfiguration = RunnerUtil.getTransExecutionConfiguration(transMeta);
			TransExecutor transExecutor = TransExecutor.initExecutor(executionConfiguration, transMeta);
            transExecutor.setStartDate(new Date());
			logger.info("执行转换=> [作业名称：" + jobDetail.getKey().getName() + " 作业组：" + jobDetail.getKey().getGroup() + "] ");
			Thread tr = new Thread(transExecutor, "TransExecutor_" + transExecutor.getExecutionId());
			tr.start();

			LogTrans logTrans  = new LogTrans();
			logTrans.setLogTransId(logTransId);
			logTrans.setTransConfigId(Long.valueOf(transMeta.getObjectId().getId()));
			logTrans.setStartdate(transExecutor.getStartDate());
			logTrans.setStatus("start");
			logTrans.setFireTime(fireTime);
			logTrans.setQrtzJobGroup(jobDetail.getKey().getGroup());
			logTrans.setQrtzJobName(jobDetail.getKey().getName());
			logTrans.setTransname(transExecutor.getTransMeta().getName());
			logTrans.setChannelId(transExecutor.getExecutionId());
			logTrans.setTransCnName(transExecutor.getTransMeta().getName());
			logTrans.insert();

			TransLogTimerTask transTimerTask = new TransLogTimerTask(transExecutor,logTrans);
			logTimer.schedule(transTimerTask, 0,5000);


			return transExecutor;

		} catch(Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}



	private void initLog(long id){
		String file = "../logs/kettle/"+id+".log";

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%-d{yyyy-MM-dd HH:mm:ss} [%t] %m%n");

		RollingFileAppender appender = new RollingFileAppender();
		appender.setLayout(layout);
		appender.setFile(file);
		appender.setEncoding("UTF-8");
		appender.setAppend(true);
		appender.activateOptions();

		org.apache.log4j.Logger.getLogger("org.firzjb.schedule").addAppender(appender);
		org.apache.log4j.Logger.getLogger("org.firzjb.kettle").addAppender(appender);
		org.apache.log4j.Logger.getLogger("org.pentaho.di").addAppender(appender);

	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		TransExecutor transExecutor = executeTrans(jobDetail,context.getFireTime());

		while (!transExecutor.isFinished()){
			try {
				Thread.sleep(1000);
			}catch (Exception e){
			}
		}
	}
}
