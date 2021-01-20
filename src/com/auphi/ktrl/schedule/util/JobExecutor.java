package com.auphi.ktrl.schedule.util;

import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.ha.bean.SlaveServerBean;
import com.auphi.ktrl.ha.util.SlaveServerUtil;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.schedule.task.JobLogTimerTask;
import com.auphi.ktrl.system.mail.util.MailUtil;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.ClassLoaderUtil;
import com.auphi.ktrl.util.StringUtil;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.logging.CentralLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.www.SlaveServerJobStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;

public class JobExecutor implements Runnable{

    private int execType;
    private MonitorScheduleBean monitorSchedule;
    private JobExecutionConfiguration executionConfiguration;
    private JobMeta jobMeta = null;
    private Job job = null;

    private java.util.Map<String,String> arguments,  params,  variables;

    private Repository repository;

    private static ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();

    private boolean finished = false;
    private int errCount;

    public long getErrCount() {
        return errCount;
    }

    private int startLineNr = 0;

    private String carteObjectId = null;

    private String jobMetaName;

    private SlaveServer slaveServer;

    public JobExecutor(MonitorScheduleBean monitorSchedule, Repository repository,Job job, int execType,Map<String,String> arguments, Map<String,String> params, Map<String,String> variables) throws Exception {
        this.monitorSchedule = monitorSchedule;
        this.job = job;
        this.execType = execType;
        this.repository = repository;
        try {
            CentralLogStore.init(100000, Const.MAX_NR_LOG_LINES);



            this.jobMeta = job.getJobMeta();

            jobMetaName = this.jobMeta.getName() ;

            executionConfiguration = new JobExecutionConfiguration();
            executionConfiguration.setRepository(repository);

            if(arguments !=null){
                executionConfiguration.setArguments(arguments);
                this.jobMeta.setArguments(executionConfiguration.getArgumentStrings());
            }
            if(params!=null){
                executionConfiguration.setParams(params);
            }
            if(variables!=null){
                executionConfiguration.setVariables(variables);
            }

            if(KettleEngine.EXECTYPE_LOCAL == execType){
                executionConfiguration.setExecutingLocally(true);
                executionConfiguration.setExecutingRemotely(false);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType || KettleEngine.EXECTYPE_CLUSTER == execType || KettleEngine.EXECTYPE_HA == execType ){

                executionConfiguration.setExecutingLocally(false);
                executionConfiguration.setExecutingRemotely(true);
            }

            this.job.setRepository(repository);

        }catch (Exception e){

            throw e;
        }

    }

    private static Hashtable<Integer, JobExecutor> executors = new Hashtable<Integer, JobExecutor>();

    public static synchronized JobExecutor initExecutor(MonitorScheduleBean monitorSchedule, Repository repository, Job job, int execType, Map<String,String> arguments, Map<String,String> params, Map<String,String> variables) throws Exception {
        JobExecutor jobExecutor = new JobExecutor(monitorSchedule,repository,job,execType,arguments,params,variables);
        executors.put(monitorSchedule.getId(), jobExecutor);
        return jobExecutor;
    }

    public static JobExecutor getExecutor(Integer executionId) {
        return executors.get(executionId);
    }

    public MonitorScheduleBean getMonitorSchedule() {
        return monitorSchedule;
    }

    public static void remove(String executionId) {
        executors.remove(executionId);
    }


    @Override
    public void run() {

        try {

            boolean running = true;





            if(KettleEngine.EXECTYPE_LOCAL == execType){

                this.job.start();

                while(running) {
                    Thread.sleep(500);
                    running = !job.isFinished();
                }

                errCount = this.job.getErrors();

                LogChannelInterface logChannel =  this.job.getLogChannel();
                Date stop = new Date();

                logChannel.logMinimal("ETL--JOB Finished!", new Object[0]);
                logChannel.logMinimal("ETL--JOB Start="+ StringUtil.DateToString(monitorSchedule.getStartTime(), "yyyy/MM/dd HH:mm:ss")+", Stop="+ StringUtil.DateToString(stop, "yyyy/MM/dd HH:mm:ss"), new Object[0]);
                long millis= stop.getTime()-monitorSchedule.getStartTime().getTime();
                logChannel.logMinimal("ETL--JOB Processing ended after "+(millis/1000)+" seconds.", new Object[0]);


            }else if(KettleEngine.EXECTYPE_REMOTE == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                slaveServer = createSlaveServer(slaveServerBean);

                executionConfiguration.setRemoteServer(slaveServer);

                this.job.sendToSlaveServer(jobMeta, executionConfiguration, repository);

                SlaveServer remoteSlaveServer =  executionConfiguration.getRemoteServer();

                SlaveServerJobStatus jobStatus =  remoteSlaveServer.getJobStatus(jobMetaName,carteObjectId,0);


                while(running) {
                    Thread.sleep(500);
                    running = jobStatus.isRunning();

                    Result result = jobStatus.getResult();
                    if(!running && result != null) {
                        errCount = Long.valueOf(result.getNrErrors()).intValue();
                    }


                }

            }else if(KettleEngine.EXECTYPE_HA == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                slaveServer = createSlaveServer(slaveServerBean);

                executionConfiguration.setRemoteServer(slaveServer);

                this.job.sendToSlaveServer(jobMeta, executionConfiguration, repository);

                SlaveServer remoteSlaveServer =  executionConfiguration.getRemoteServer();

                SlaveServerJobStatus jobStatus =  remoteSlaveServer.getJobStatus(jobMetaName,carteObjectId,0);


                while(running) {
                    Thread.sleep(500);
                    running = jobStatus.isRunning();

                    Result result = jobStatus.getResult();
                    if(!running && result != null) {
                        errCount = Long.valueOf(result.getNrErrors()).intValue();
                    }


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = null;
            try {
                errMsg = getExecutionLog();
            } catch (Exception ex) {
                errMsg = e.getLocalizedMessage();
            }
            MonitorUtil.updateMonitorAfterError(monitorSchedule.getId(), errMsg);

            String title = "[ScheduleError][" + StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "][" + monitorSchedule.getJobName() + "]";
            String errorNoticeUserId = monitorSchedule.getErrorNoticeUserId();
            String[] user_mails = UserUtil.getUserEmails(errorNoticeUserId);
            MailUtil.sendMail(user_mails, title, errMsg);

        }finally {

            setFinished(true);

            if(repository != null){
                repository.disconnect();
            }
        }


    }

    private SlaveServerBean getSlaveServerBean(String remoteServer, String ha) throws Exception{
        SlaveServerBean slaveServerBean = new SlaveServerBean();
        if(!"".equals(remoteServer)){
            slaveServerBean = SlaveServerUtil.getSlaveServer(remoteServer);
        }else if(!"".equals(ha)){
            slaveServerBean = SlaveServerUtil.getBestServerFromCluster(ha);
        }

        return slaveServerBean;
    }

    /**
     * create instance of SlaveServer using platform slave configuration
     * @param slaveServerBean
     * @return
     * @throws Exception
     */
    private SlaveServer createSlaveServer(SlaveServerBean slaveServerBean) throws Exception{
        boolean isMaster = false;
        if("1".equals(slaveServerBean.getMaster())){
            isMaster = true;
        }

        SlaveServer slaveServer = new SlaveServer(
                slaveServerBean.getName(),
                slaveServerBean.getHost_name(),
                slaveServerBean.getPort(),
                slaveServerBean.getUsername(),
                slaveServerBean.getPassword(),
                slaveServerBean.getProxy_host_name(),
                slaveServerBean.getProxy_port(),
                slaveServerBean.getNon_proxy_hosts(), isMaster);

        return slaveServer;
    }

    public String getExecutionLog() throws Exception {


        if(executionConfiguration.isExecutingLocally()) {

            LogChannelInterface logChannel =  this.job.getLogChannel();

            String logChannelId = logChannel.getLogChannelId();

            String loggingText = CentralLogStore.getAppender().getBuffer(
                    logChannelId, false, startLineNr, CentralLogStore.getLastBufferLineNr()).toString();

            return loggingText;
        } else {

            SlaveServerJobStatus jobStatus =  slaveServer.getJobStatus(jobMetaName,carteObjectId,0);

            return jobStatus.getLoggingString();

        }
    }

    public boolean isFinished() throws Exception {


       return finished;

    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Result getResult() throws Exception {

        return job.getResult();
    }

    public int getErrors() throws Exception {


        return errCount;
    }
    public boolean isStopped() throws Exception {
       return job.isStopped();

    }

    public void stop() throws Exception {

        try {
            if(KettleEngine.EXECTYPE_LOCAL == execType){
                job.stopAll();

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){


            }else if(KettleEngine.EXECTYPE_HA == execType){

            }
        }catch (Exception e){
            throw e;
        }


    }

    public void stopAllForcely() throws Exception{

        job.stopAllForcely();
    }


    public Object getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Object getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }



    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
