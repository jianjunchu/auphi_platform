package com.auphi.ktrl.schedule.util;

import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.ha.bean.SlaveServerBean;
import com.auphi.ktrl.ha.util.SlaveServerUtil;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.util.ClassLoaderUtil;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.CentralLogStore;
import org.pentaho.di.trans.Trans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;

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

    public JobExecutor(MonitorScheduleBean monitorSchedule, Object job, int execType) {
        this.monitorSchedule = monitorSchedule;
        this.job = job;
        this.execType = execType;

        try {


            kettleLogStoreClass = Class.forName("org.pentaho.di.core.logging.CentralLogStore");

            CentralLogStore.init(100000, Const.MAX_NR_LOG_LINES);

            this.jobMetaClass = Class.forName("org.pentaho.di.job.JobMeta", true, classLoaderUtil);
            this.jobClass= Class.forName("org.pentaho.di.job.Job", true, classLoaderUtil);

            Method getJobMeta = this.jobClass.getDeclaredMethod("getJobMeta");

            this.jobMeta = getJobMeta.invoke(job);



            executionConfigurationClass = Class.forName("org.pentaho.di.job.JobExecutionConfiguration", true, classLoaderUtil);

            Constructor<?> consJobExcutionConfig = executionConfigurationClass.getConstructor();
            executionConfiguration = consJobExcutionConfig.newInstance();

            Method setExcutingLocally = executionConfigurationClass.getDeclaredMethod("setExecutingLocally", boolean.class);
            Method setExecutingRemotely = executionConfigurationClass.getDeclaredMethod("setExecutingRemotely", boolean.class);

            if(KettleEngine.EXECTYPE_LOCAL == execType){
                setExcutingLocally.invoke(executionConfiguration, true);
                setExecutingRemotely.invoke(executionConfiguration, false);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType || KettleEngine.EXECTYPE_CLUSTER == execType || KettleEngine.EXECTYPE_HA == execType ){

                setExcutingLocally.invoke(executionConfiguration, true);
                setExecutingRemotely.invoke(executionConfiguration, false);

            }



        }catch (Exception e){
            e.printStackTrace();

        }


    }

    private static Hashtable<Integer, JobExecutor> executors = new Hashtable<Integer, JobExecutor>();

    public static synchronized JobExecutor initExecutor(MonitorScheduleBean monitorSchedule, Object job, int execType) {
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


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){


            }else if(KettleEngine.EXECTYPE_HA == execType){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            finished = true;
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

        Method getLogChannel = jobClass.getDeclaredMethod("getLogChannel");
        Object logChannel = getLogChannel.invoke(job);

        Method  getLogChannelId  =  logChannel.getClass().getDeclaredMethod("getLogChannelId");
        String logChannelId =   (String) getLogChannelId.invoke(logChannel);

        String loggingText = CentralLogStore.getAppender().getBuffer(
                logChannelId, false, startLineNr, CentralLogStore.getLastBufferLineNr()).toString();
        return loggingText;
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
