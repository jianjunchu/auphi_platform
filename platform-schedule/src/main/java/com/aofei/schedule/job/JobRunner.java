package com.aofei.schedule.job;


import com.alibaba.fastjson.JSON;
import com.aofei.base.common.Const;
import com.aofei.joblog.entity.LogJob;
import com.aofei.joblog.task.JobLogTimerTask;
import com.aofei.kettle.App;
import com.aofei.kettle.JobExecutor;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import org.apache.log4j.Logger;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class JobRunner extends QuartzJobBean {

	private static Logger logger = Logger.getLogger(JobRunner.class);

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {

		JobDetail jobDetail = context.getJobDetail();

		executeJob(jobDetail,context.getFireTime());

	}



	public JobExecutor executeJob(JobDetail jobDetail ,Date fireTime) throws JobExecutionException {


		Repository repository = App.getInstance().getRepository();
		try {
			String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);

			GeneralScheduleRequest request = JSON.parseObject(json,GeneralScheduleRequest.class);

			String dir = request.getFilePath();
			String name = request.getFile();


			System.out.println("Job path ==> " + dir);
			System.out.println("Job name ==> " + name);

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
			executionConfiguration.setLogLevel(LogLevel.MINIMAL );

			// Fill the parameters, maybe do this in another place?
			Map<String, String> params = executionConfiguration.getParams();
			params.clear();
			String[] paramNames = jobMeta.listParameters();
			for (String key : paramNames) {
				params.put(key, "");
			}



			JobExecutor jobExecutor = JobExecutor.initExecutor(executionConfiguration, jobMeta);
			Thread tr = new Thread(jobExecutor, "JobExecutor_" + jobExecutor.getExecutionId());
			tr.start();
            jobExecutor.setStartDate(new Date());

			LogJob logJob  = new LogJob();
			logJob.setStartdate(jobExecutor.getStartDate());
			logJob.setStatus("start");
			logJob.setFireTime(fireTime);
			logJob.setQrtzJobGroup(jobDetail.getKey().getGroup());
			logJob.setQrtzJobName(jobDetail.getKey().getName());
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


}
