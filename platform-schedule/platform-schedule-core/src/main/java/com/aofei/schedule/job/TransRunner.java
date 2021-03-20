package com.aofei.schedule.job;


import com.aofei.kettle.TransExecutor;
import com.aofei.schedule.util.RunnerUtil;
import com.aofei.translog.entity.LogTrans;
import com.aofei.translog.task.TransLogTimerTask;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Timer;

@DisallowConcurrentExecution
public class TransRunner extends QuartzJobBean {



	private static Logger logger = LoggerFactory.getLogger(TransRunner.class);
	private final Timer logTimer = new Timer();


	public TransExecutor  executeTrans(JobDetail jobDetail , Date fireTime) throws JobExecutionException {
		try {


			TransMeta transMeta = RunnerUtil.getTransMeta(jobDetail);

			TransExecutionConfiguration executionConfiguration = RunnerUtil.getTransExecutionConfiguration(transMeta);
			TransExecutor transExecutor = TransExecutor.initExecutor(executionConfiguration, transMeta);
            transExecutor.setStartDate(new Date());
			Thread tr = new Thread(transExecutor, "TransExecutor_" + transExecutor.getExecutionId());
			tr.start();

			LogTrans logTrans  = new LogTrans();
			logTrans.setStartdate(transExecutor.getStartDate());
			logTrans.setStatus("start");
			logTrans.setFireTime(fireTime);
			logTrans.setQrtzJobGroup(jobDetail.getKey().getGroup());
			logTrans.setQrtzJobName(jobDetail.getKey().getName());
			logTrans.setTransname(transExecutor.getTransMeta().getName());
			logTrans.setChannelId(transExecutor.getExecutionId());
			logTrans.setTransCnName(transExecutor.getTransMeta().getName());

			TransLogTimerTask transTimerTask = new TransLogTimerTask(transExecutor,logTrans);
			logTimer.schedule(transTimerTask, 0,5000);


			return transExecutor;

		} catch(Exception e) {
			throw new JobExecutionException(e);
		}
	}

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();

		executeTrans(jobDetail,context.getFireTime());

	}



}
