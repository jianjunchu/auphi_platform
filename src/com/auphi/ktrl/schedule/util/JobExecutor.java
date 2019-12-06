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
import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.CentralLogStore;
import org.pentaho.di.job.Job;
import org.pentaho.di.www.SlaveServerJobStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;

public class JobExecutor implements Runnable{

    private int execType;
    private MonitorScheduleBean monitorSchedule;
    private Object executionConfiguration;
    private Class<?> executionConfigurationClass;
    private Object jobMeta = null;
    private Object job = null;
    private Class<?> jobMetaClass;
    private Class<?> jobClass;

    private Class<?> kettleLogStoreClass;

    private Object repository;

    private Class<?> repositoryClass;

    private static ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();

    private boolean finished = false;
    private int errCount;

    public long getErrCount() {
        return errCount;
    }

    private int startLineNr = 0;

    private String carteObjectId = null;

    private String jobMetaName;

    private Object slaveServer;

    public JobExecutor(MonitorScheduleBean monitorSchedule, Object repository,Object job, int execType) throws Exception {
        this.monitorSchedule = monitorSchedule;
        this.job = job;
        this.execType = execType;
        this.repository = repository;
        try {
            CentralLogStore.init(100000, Const.MAX_NR_LOG_LINES);



            kettleLogStoreClass = Class.forName("org.pentaho.di.core.logging.CentralLogStore");

            this.jobMetaClass = Class.forName("org.pentaho.di.job.JobMeta", true, classLoaderUtil);
            this.jobClass= Class.forName("org.pentaho.di.job.Job", true, classLoaderUtil);
            this.repositoryClass = Class.forName("org.pentaho.di.repository.Repository", true, classLoaderUtil);

            Method getJobMeta = this.jobClass.getDeclaredMethod("getJobMeta");

            this.jobMeta = getJobMeta.invoke(this.job);
            this.job.getClass().getDeclaredMethod("setRepository",this.repositoryClass).invoke(this.job,this.repository);

            jobMetaName = (String) jobMeta.getClass().getDeclaredMethod("getName").invoke(jobMeta);

            executionConfigurationClass = Class.forName("org.pentaho.di.job.JobExecutionConfiguration", true, classLoaderUtil);

            Constructor<?> consJobExcutionConfig = executionConfigurationClass.getConstructor();
            executionConfiguration = consJobExcutionConfig.newInstance();

            Method setExcutingLocally = executionConfigurationClass.getDeclaredMethod("setExecutingLocally", boolean.class);
            Method setExecutingRemotely = executionConfigurationClass.getDeclaredMethod("setExecutingRemotely", boolean.class);

            executionConfiguration.getClass().getDeclaredMethod("setRepository",this.repositoryClass).invoke(executionConfiguration,this.repository);

            if(KettleEngine.EXECTYPE_LOCAL == execType){
                setExcutingLocally.invoke(executionConfiguration, true);
                setExecutingRemotely.invoke(executionConfiguration, false);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType || KettleEngine.EXECTYPE_CLUSTER == execType || KettleEngine.EXECTYPE_HA == execType ){

                setExcutingLocally.invoke(executionConfiguration, false);
                setExecutingRemotely.invoke(executionConfiguration, true);

            }

        }catch (Exception e){

            throw e;
        }

    }

    private static Hashtable<Integer, JobExecutor> executors = new Hashtable<Integer, JobExecutor>();

    public static synchronized JobExecutor initExecutor(MonitorScheduleBean monitorSchedule, Object repository,Object job, int execType) throws Exception {
        JobExecutor transExecutor = new JobExecutor(monitorSchedule,repository,job,execType);
        executors.put(transExecutor.getMonitorSchedule().getId(), transExecutor);
        return transExecutor;
    }

    public static JobExecutor getExecutor(Long executionId) {
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


                Class<?> threadClass = Class.forName("java.lang.Thread", true, classLoaderUtil);
                Method start_job = threadClass.getDeclaredMethod("start");
                start_job.invoke(job);

                Method  isFinished  =  job.getClass().getDeclaredMethod("isFinished");
                while(running) {
                    Thread.sleep(500);
                    running = !(Boolean) isFinished.invoke(job);
                }

                Method getLogChannel = jobClass.getDeclaredMethod("getLogChannel");
                Object logChannel = getLogChannel.invoke(job);

                Method logMinimal = logChannel.getClass().getDeclaredMethod("logMinimal", new Class[] {String.class, Object[].class});
                //log write
                logMinimal.invoke(logChannel, new Object[] {"ETL--JOB Finished!", new Object[0]});
                Date stop = new Date();
                logMinimal.invoke(logChannel, new Object[] {"ETL--JOB Start="+ StringUtil.DateToString(monitorSchedule.getStartTime(), "yyyy/MM/dd HH:mm:ss")+", Stop="+ StringUtil.DateToString(stop, "yyyy/MM/dd HH:mm:ss"), new Object[0]});
                long millis= stop.getTime()-monitorSchedule.getStartTime().getTime();
                logMinimal.invoke(logChannel, new Object[] {"ETL--JOB Processing ended after "+(millis/1000)+" seconds.", new Object[0]});

                errCount = (int)job.getClass().getDeclaredMethod("getErrors").invoke(job);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(executionConfiguration, slaveServer);

                Method sendToSlaveServer = job.getClass().getDeclaredMethod("sendToSlaveServer", jobMeta.getClass(), executionConfigurationClass, repositoryClass);
                carteObjectId = (String) sendToSlaveServer.invoke(job, jobMeta, executionConfiguration, repository);

                Method  getRemoteServer = executionConfigurationClass.getDeclaredMethod("getRemoteServer");
                Object remoteSlaveServer = getRemoteServer.invoke(executionConfiguration);
                Method getJobStatus = remoteSlaveServer.getClass().getDeclaredMethod("getJobStatus",String.class,String.class,int.class);
                Object jobStatus = getJobStatus.invoke(remoteSlaveServer,jobMetaName,carteObjectId,0);

                while(running) {
                    Thread.sleep(500);
                    running = (boolean)jobStatus.getClass().getDeclaredMethod("isRunning").invoke(jobStatus);

                    Object result = jobStatus.getClass().getDeclaredMethod("getResult").invoke(jobStatus);
                    if(!running && result != null) {
                        errCount = ((Long)result.getClass().getDeclaredMethod("getNrErrors").invoke(result)).intValue();
                    }


                }

            }else if(KettleEngine.EXECTYPE_HA == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean("", monitorSchedule.getHaName());
                slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(executionConfiguration, slaveServer);


                Method sendToSlaveServer = job.getClass().getDeclaredMethod("sendToSlaveServer", jobMeta.getClass(), executionConfigurationClass, repositoryClass);
                carteObjectId = (String) sendToSlaveServer.invoke(job, jobMeta, executionConfiguration, repository);

                Method getJobStatus = slaveServer.getClass().getDeclaredMethod("getJobStatus",String.class,String.class,int.class);
                while(running) {

                    Thread.sleep(500);

                    Object jobStatus = getJobStatus.invoke(slaveServer,jobMetaName,carteObjectId,0);

                    running = (boolean)jobStatus.getClass().getDeclaredMethod("isRunning").invoke(jobStatus);

                    Object result = jobStatus.getClass().getDeclaredMethod("getResult").invoke(jobStatus);
                    if(!running && result != null) {

                        errCount = ((Long)result.getClass().getDeclaredMethod("getNrErrors").invoke(result)).intValue();
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
                try {
                    this.repository.getClass().getDeclaredMethod("disconnect").invoke(this.repository);
                } catch (Exception e) {

                }
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
    private Object createSlaveServer(SlaveServerBean slaveServerBean) throws Exception{
        boolean isMaster = false;
        if("1".equals(slaveServerBean.getMaster())){
            isMaster = true;
        }
        Class<?> slaveServerClass = Class.forName("org.pentaho.di.cluster.SlaveServer", true, classLoaderUtil);
        Constructor<?> slaveServerConstructor = slaveServerClass
                .getConstructor(String.class, String.class,
                        String.class, String.class, String.class,
                        String.class, String.class, String.class,
                        boolean.class);
        Object slaveServer = slaveServerConstructor.newInstance(
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


        Method isExecutingLocally  = executionConfigurationClass.getDeclaredMethod("isExecutingLocally");

        if((Boolean) isExecutingLocally.invoke(executionConfiguration)) {

            Method getLogChannel = jobClass.getDeclaredMethod("getLogChannel");
            Object logChannel = getLogChannel.invoke(job);

            Method  getLogChannelId  =  logChannel.getClass().getDeclaredMethod("getLogChannelId");
            String logChannelId =   (String) getLogChannelId.invoke(logChannel);

            String loggingText = CentralLogStore.getAppender().getBuffer(
                    logChannelId, false, startLineNr, CentralLogStore.getLastBufferLineNr()).toString();
            return loggingText;
        } else {

            Method getJobStatus = slaveServer.getClass().getDeclaredMethod("getJobStatus",String.class,String.class,int.class);
            Object jobStatus = getJobStatus.invoke(slaveServer,jobMetaName,carteObjectId,0);
            Method getLoggingString = jobStatus.getClass().getDeclaredMethod("getLoggingString");
            return  (String) getLoggingString.invoke(jobStatus);
        }
    }

    public boolean isFinished() throws Exception {


       return finished;

    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Object getResult() throws Exception {
        Method  getResult  =  jobClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(job);
        return result;
    }

    public int getErrors() throws Exception {


        return errCount;
    }
    public boolean isStopped() throws Exception {
        Method  isFinishedOrStopped  =  jobClass.getDeclaredMethod("isStopped");

        return (boolean) isFinishedOrStopped.invoke(job);

    }

    public void stop() throws Exception {

        try {
            if(KettleEngine.EXECTYPE_LOCAL == execType){
                Method  stopAll  =  jobClass.getDeclaredMethod("stopAll");
                stopAll.invoke(job);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){


            }else if(KettleEngine.EXECTYPE_HA == execType){

            }
        }catch (Exception e){
            throw e;
        }


    }


    public Object getRepository() {
        return repository;
    }

    public void setRepository(Object repository) {
        this.repository = repository;
    }

    public Object getJob() {
        return job;
    }

    public void setJob(Object job) {
        this.job = job;
    }

    public Class<?> getJobMetaClass() {
        return jobMetaClass;
    }

    public void setJobMetaClass(Class<?> jobMetaClass) {
        this.jobMetaClass = jobMetaClass;
    }



}
