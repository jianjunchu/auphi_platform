package org.firzjb.schedule.job;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import lombok.SneakyThrows;
import org.apache.log4j.*;
import org.firzjb.base.common.Const;
import org.firzjb.joblog.entity.LogJob;
import org.firzjb.joblog.task.JobLogTimerTask;
import org.firzjb.kettle.App;
import org.firzjb.kettle.JobExecutor;
import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.firzjb.schedule.model.request.GeneralScheduleRequest;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.DefaultLogLevel;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class JobRunner implements Job {

	private static Logger logger = Logger.getLogger(JobRunner.class);


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();

		JobExecutor jobExecutor = executeJob(jobDetail,context.getFireTime());

		while (!jobExecutor.isFinished()){
			try {
				Thread.sleep(1000);
			}catch (Exception e){
			}
		}
	}



	public JobExecutor executeJob(JobDetail jobDetail ,Date fireTime) throws JobExecutionException {
		Date startDate = new Date();
		long logJobId = IdWorker.getId();


		Repository repository = App.getInstance().getRepository();
		try {
			//初始化文件日志 将作业日志保存到文件
			initFileLog(logJobId);

			//初始化日志
			LogJob logJob  = new LogJob();
			logJob.setLogJobId(logJobId);
			logJob.setStartdate(startDate);
			logJob.setStatus("start");
			logJob.setFireTime(fireTime);
			logJob.setQrtzJobGroup(jobDetail.getKey().getGroup());
			logJob.setQrtzJobName(jobDetail.getKey().getName());
			logJob.insert();

			String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);

			GeneralScheduleRequest request = JSON.parseObject(json,GeneralScheduleRequest.class);

			String dir = request.getFilePath();
			String name = request.getFile();


			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null)
				directory = repository.getUserHomeDirectory();

			JobMeta jobMeta = repository.loadJob(name, directory, null, null);
			repository.disconnect();
			JobExecutionConfiguration executionConfiguration = App.getInstance().getJobExecutionConfiguration();

			// Remember the variables set previously
			//
			RowMetaAndData variables = App.getInstance().getVariables();
			Object[] data = variables.getData();
			String[] fields = variables.getRowMeta().getFieldNames();
			Map<String, String> variableMap = new HashMap<String, String>();
			for ( int idx = 0; idx < fields.length; idx++ ) {
				variableMap.put( fields[idx], data[idx].toString() );
			}

			executionConfiguration.setVariables( variableMap );
			executionConfiguration.getUsedVariables( jobMeta );
			executionConfiguration.setReplayDate( null );
			executionConfiguration.setRepository( App.getInstance().getRepository() );
			executionConfiguration.setSafeModeEnabled( false );
			executionConfiguration.setStartCopyName( null );
			executionConfiguration.setStartCopyNr( 0 );
			executionConfiguration.setLogLevel( DefaultLogLevel.getLogLevel() );

			// Fill the parameters, maybe do this in another place?
			Map<String, String> params = executionConfiguration.getParams();
			params.clear();
			String[] paramNames = jobMeta.listParameters();
			for (String key : paramNames) {
				params.put(key, "");
			}



			JobExecutor jobExecutor = JobExecutor.initExecutor(executionConfiguration, jobMeta);
			jobExecutor.setStartDate(startDate);
			logger.info("执行作业=> [作业名称：" + jobDetail.getKey().getName() + " 作业组：" + jobDetail.getKey().getGroup() + "] ");
			Thread tr = new Thread(jobExecutor, "JobExecutor_" + jobExecutor.getExecutionId());
			tr.start();


			logJob.setJobName(jobMeta.getName());
			logJob.setChannelId(jobExecutor.getExecutionId());
			logJob.setJobCnName(jobMeta.getName());


			JobLogTimerTask jobLogTimerTask = new JobLogTimerTask(jobExecutor,logJob);
			Timer logTimer = new Timer();
			logTimer.schedule(jobLogTimerTask, 0,5000);

			return jobExecutor ;

		} catch(Exception e) {
			throw new JobExecutionException(e);
		}finally {
			repository.disconnect();
		}
	}


	private void initFileLog(long id){
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


}
