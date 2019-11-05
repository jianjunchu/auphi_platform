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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
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

    private Class<?> repositoryClass;
    private Object repository;

    private static ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();

    private boolean finished = false;
    private long errCount;

    public long getErrCount() {
        return errCount;
    }

    private int startLineNr = 0;

    private String carteObjectId = null;

    public JobExecutor(MonitorScheduleBean monitorSchedule, Object job, int execType) throws Exception {
        this.monitorSchedule = monitorSchedule;
        this.job = job;
        this.execType = execType;

        try {
            CentralLogStore.init(100000, Const.MAX_NR_LOG_LINES);

            kettleLogStoreClass = Class.forName("org.pentaho.di.core.logging.CentralLogStore");

            this.jobMetaClass = Class.forName("org.pentaho.di.job.JobMeta", true, classLoaderUtil);
            this.jobClass= Class.forName("org.pentaho.di.job.Job", true, classLoaderUtil);

            Method getJobMeta = this.jobClass.getDeclaredMethod("getJobMeta");

            this.jobMeta = getJobMeta.invoke(this.job);

            repositoryClass = Class.forName("org.pentaho.di.repository.Repository", true, classLoaderUtil);
            Method getRep = this.jobClass.getDeclaredMethod("getRep");
            repository = getRep.invoke(this.job);

            executionConfigurationClass = Class.forName("org.pentaho.di.job.JobExecutionConfiguration", true, classLoaderUtil);

            Constructor<?> consJobExcutionConfig = executionConfigurationClass.getConstructor();
            executionConfiguration = consJobExcutionConfig.newInstance();

            Method setExcutingLocally = executionConfigurationClass.getDeclaredMethod("setExecutingLocally", boolean.class);
            Method setExecutingRemotely = executionConfigurationClass.getDeclaredMethod("setExecutingRemotely", boolean.class);

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

    public static synchronized JobExecutor initExecutor(MonitorScheduleBean monitorSchedule, Object job, int execType) throws Exception {
        JobExecutor transExecutor = new JobExecutor(monitorSchedule,job,execType);
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


            if(KettleEngine.EXECTYPE_LOCAL == execType){

                //job execute
                Method beginProcessing = jobClass.getDeclaredMethod("beginProcessing");
                beginProcessing.invoke(job);


                Class<?> threadClass = Class.forName("java.lang.Thread", true, classLoaderUtil);
                Method start_job = threadClass.getDeclaredMethod("start");
                start_job.invoke(job);
                Method waitUntilFinished = jobClass.getDeclaredMethod("waitUntilFinished");
                waitUntilFinished.invoke(job);


            }else if(KettleEngine.EXECTYPE_REMOTE == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                Object slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(executionConfiguration, slaveServer);

                Method sendToSlaveServer = job.getClass().getDeclaredMethod("sendToSlaveServer", jobMeta.getClass(), executionConfigurationClass, repositoryClass);
                carteObjectId = (String) sendToSlaveServer.invoke(job, jobMeta, executionConfiguration, repository);

            }else if(KettleEngine.EXECTYPE_HA == execType){

                SlaveServerBean slaveServerBean = getSlaveServerBean("", monitorSchedule.getHaName());
                Object slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(executionConfiguration, slaveServer);


                Method sendToSlaveServer = job.getClass().getDeclaredMethod("sendToSlaveServer", jobMeta.getClass(), executionConfigurationClass, repositoryClass);
                carteObjectId = (String) sendToSlaveServer.invoke(job, jobMeta, executionConfiguration, repository);
            }
            Timer logTimer = new Timer();
            JobLogTimerTask transTimerTask = new JobLogTimerTask (this);
            logTimer.schedule(transTimerTask, 0,1000);
            finished = true;
        } catch (Exception e) {
            String errMsg = null;
            try {
                errMsg = getExecutionLog();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            MonitorUtil.updateMonitorAfterError(monitorSchedule.getId(), errMsg);

            String title = "[ScheduleError][" + StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "][" + monitorSchedule.getJobName() + "]";
            String errorNoticeUserId = monitorSchedule.getErrorNoticeUserId();
            String[] user_mails = UserUtil.getUserEmails(errorNoticeUserId);
            MailUtil.sendMail(user_mails, title, errMsg);

            finished = false;
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

            Method  getRemoteServer = executionConfigurationClass.getDeclaredMethod("getRemoteServer");
            Object remoteSlaveServer = getRemoteServer.invoke(executionConfiguration);
            Method getJobStatus = remoteSlaveServer.getClass().getDeclaredMethod("getJobStatus",String.class,String.class,int.class);
            Object jobStatus = getJobStatus.invoke(remoteSlaveServer);
            Method getLoggingString = jobStatus.getClass().getDeclaredMethod("getLoggingString");
            return  (String) getLoggingString.invoke(jobStatus);
        }
    }

    public boolean isFinished() throws Exception {
        Method  isFinished  =  jobClass.getDeclaredMethod("isFinished");

        return (boolean) isFinished.invoke(job);

    }

    public Object getResult() throws Exception {
        Method  getResult  =  jobClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(job);
        return result;
    }

    public int getErrors() throws Exception {
        Method  isFinished  =  jobClass.getDeclaredMethod("getErrors");

        return (Integer) isFinished.invoke(job);

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

}
