package com.auphi.ktrl.schedule.util;

import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.ha.bean.SlaveServerBean;
import com.auphi.ktrl.ha.util.SlaveServerUtil;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.util.ClassLoaderUtil;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.CentralLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

public class TransExecutor implements Runnable{

    private int execType;
    private MonitorScheduleBean monitorSchedule;
    private Object executionConfiguration;
    private Class<?> executionConfigurationClass;
    private Object transMeta = null;
    private Object trans = null;
    private Class<?> transMetaClass;
    private Class<?> transClass;

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

    public TransExecutor(MonitorScheduleBean monitorSchedule, Object trans, int execType) {
        this.monitorSchedule = monitorSchedule;
        this.trans = trans;
        this.execType = execType;

        try {
            CentralLogStore.init(50000, Const.MAX_NR_LOG_LINES);
            this.transMetaClass = Class.forName("org.pentaho.di.trans.TransMeta", true, classLoaderUtil);
            this.transClass= Class.forName("org.pentaho.di.trans.Trans", true, classLoaderUtil);
            Method getJobMeta = this.transClass.getDeclaredMethod("getTransMeta");
            this.transMeta = getJobMeta.invoke(trans);

            repositoryClass = Class.forName("org.pentaho.di.repository.Repository", true, classLoaderUtil);
            Method getRepository = this.transClass.getDeclaredMethod("getRepository");
            repository = getRepository.invoke(trans);

            executionConfigurationClass = Class.forName("org.pentaho.di.trans.TransExecutionConfiguration", true, classLoaderUtil);

            Constructor<?> consTransExcutionConfig = executionConfigurationClass.getConstructor();
            this.executionConfiguration = consTransExcutionConfig.newInstance();

            Method setRepository = executionConfigurationClass.getDeclaredMethod("setRepository", repositoryClass);
            setRepository.invoke(this.executionConfiguration, repository);

            Method setExcutingLocally = executionConfigurationClass.getDeclaredMethod("setExecutingLocally", boolean.class);
            Method setExecutingRemotely = executionConfigurationClass.getDeclaredMethod("setExecutingRemotely", boolean.class);
            Method setExecutingClustered = executionConfigurationClass.getDeclaredMethod("setExecutingClustered", boolean.class);
            if(KettleEngine.EXECTYPE_LOCAL == execType){

                setExcutingLocally.invoke(this.executionConfiguration, true);
                setExecutingRemotely.invoke(this.executionConfiguration, false);
                setExecutingClustered.invoke(this.executionConfiguration, false);

                kettleLogStoreClass = Class.forName("org.pentaho.di.core.logging.CentralLogStore");

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){

                setExcutingLocally.invoke(this.executionConfiguration, false);
                setExecutingRemotely.invoke(this.executionConfiguration, true);
                setExecutingClustered.invoke(this.executionConfiguration, false);

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                Object slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(this.executionConfiguration, slaveServer);

            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){

                setExcutingLocally.invoke(this.executionConfiguration, false);
                setExecutingRemotely.invoke(this.executionConfiguration, false);
                setExecutingClustered.invoke(this.executionConfiguration, true);
                Method setClusterPosting = executionConfigurationClass.getDeclaredMethod("setClusterPosting", boolean.class);
                setClusterPosting.invoke(this.executionConfiguration, true);
                Method setClusterPreparing = executionConfigurationClass.getDeclaredMethod("setClusterPreparing", boolean.class);
                setClusterPreparing.invoke(this.executionConfiguration, true);
                Method setClusterStarting = executionConfigurationClass.getDeclaredMethod("setClusterStarting", boolean.class);
                setClusterStarting.invoke(this.executionConfiguration, true);
                Method setClusterShowingTransformation = executionConfigurationClass.getDeclaredMethod("setClusterShowingTransformation", boolean.class);
                setClusterShowingTransformation.invoke(this.executionConfiguration, true);

            }else if(KettleEngine.EXECTYPE_HA == execType){
                setExcutingLocally.invoke(this.executionConfiguration, false);
                setExecutingRemotely.invoke(this.executionConfiguration, true);
                setExecutingClustered.invoke(this.executionConfiguration, false);

                SlaveServerBean slaveServerBean = getSlaveServerBean("", monitorSchedule.getHaName());
                Object slaveServer = createSlaveServer(slaveServerBean);
                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(this.executionConfiguration, slaveServer);
            }


        }catch (Exception e){
            e.printStackTrace();

        }


    }

    private static Hashtable<Integer, TransExecutor> executors = new Hashtable<Integer, TransExecutor>();

    public static synchronized TransExecutor initExecutor(MonitorScheduleBean monitorSchedule, Object transMeta, int execType) {
        TransExecutor transExecutor = new TransExecutor(monitorSchedule,transMeta,execType);
        executors.put(transExecutor.getMonitorSchedule().getId(), transExecutor);
        return transExecutor;
    }

    public static TransExecutor getExecutor(Long executionId) {
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
                Method getLastBufferLineNr = kettleLogStoreClass.getMethod("getLastBufferLineNr");
                startLineNr = (Integer) getLastBufferLineNr.invoke(null);

                Method setSafeModeEnabled =  this.transClass.getDeclaredMethod("setSafeModeEnabled",boolean.class);
                Method isSafeModeEnabled =  this.executionConfigurationClass.getDeclaredMethod("isSafeModeEnabled");
                setSafeModeEnabled.invoke(this.trans,isSafeModeEnabled.invoke(this.executionConfiguration));

                Method setGatheringMetrics =  this.transClass.getDeclaredMethod("setGatheringMetrics",boolean.class);
                Method isGatheringMetrics =  this.executionConfigurationClass.getDeclaredMethod("isGatheringMetrics");
                setGatheringMetrics.invoke(this.trans,isGatheringMetrics.invoke(this.executionConfiguration));

                Method setLogLevel =  this.transClass.getDeclaredMethod("setLogLevel", LogLevel.class);
                Method getLogLevel =  this.executionConfigurationClass.getDeclaredMethod("getLogLevel");
                setLogLevel.invoke(this.trans,getLogLevel.invoke(this.executionConfiguration));

                Method setReplayDate =  this.transClass.getDeclaredMethod("setReplayDate", Date.class);
                Method getReplayDate =  this.executionConfigurationClass.getDeclaredMethod("getReplayDate");
                setReplayDate.invoke(this.trans,getReplayDate.invoke(this.executionConfiguration));

                Method setRepository =  this.transClass.getDeclaredMethod("setRepository", Repository.class);
                Method getRepository =  this.executionConfigurationClass.getDeclaredMethod("getRepository");
                setRepository.invoke(this.trans,getRepository.invoke(this.executionConfiguration));

                //trans execute
                Method getArguments = transMetaClass.getDeclaredMethod("getArguments");
                Method execute =  this.transClass.getDeclaredMethod("execute",String[].class);
                execute.invoke(this.trans,getArguments.invoke(transMeta));

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){
                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                Object slaveServer = createSlaveServer(slaveServerBean);

                Method setRemoteServer = this.executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(this.executionConfiguration, slaveServer);


                Method sendToSlaveServer = trans.getClass().getDeclaredMethod("sendToSlaveServer", transMeta.getClass(), executionConfigurationClass, repositoryClass);
                sendToSlaveServer.invoke(trans, transMeta, this.executionConfiguration, repository);

            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){

                Method executeClustered = trans.getClass().getDeclaredMethod("executeClustered", transMeta.getClass(), executionConfigurationClass);
                executeClustered.invoke(trans, transMeta, executionConfiguration);
            }else if(KettleEngine.EXECTYPE_HA == execType){
                SlaveServerBean slaveServerBean = getSlaveServerBean("", monitorSchedule.getHaName());
                Object slaveServer = createSlaveServer(slaveServerBean);
                Method setRemoteServer = executionConfigurationClass.getDeclaredMethod("setRemoteServer", slaveServer.getClass());
                setRemoteServer.invoke(executionConfiguration, slaveServer);

                Method sendToSlaveServer = trans.getClass().getDeclaredMethod("sendToSlaveServer", transMeta.getClass(), executionConfigurationClass, repositoryClass);
                sendToSlaveServer.invoke(trans, transMeta, executionConfiguration, repository);
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

        Method getLogChannel = transClass.getDeclaredMethod("getLogChannel");
        Object logChannel = getLogChannel.invoke(trans);

        Method  getLogChannelId  =  logChannel.getClass().getDeclaredMethod("getLogChannelId");
        String logChannelId =   (String) getLogChannelId.invoke(logChannel);


        String loggingText = CentralLogStore.getAppender().getBuffer(
                logChannelId, false, startLineNr, CentralLogStore.getLastBufferLineNr()).toString();
        return loggingText;
    }

    public boolean isFinished() throws Exception {
        Method  isFinished  =  transClass.getDeclaredMethod("isFinished");

        return (boolean) isFinished.invoke(trans);

    }
    public int getErrors() throws Exception {
        Method  isFinished  =  transClass.getDeclaredMethod("getErrors");

        return (Integer) isFinished.invoke(trans);

    }
    public boolean isStopped() throws Exception {
        Method  isFinishedOrStopped  =  transClass.getDeclaredMethod("isStopped");

        return (boolean) isFinishedOrStopped.invoke(trans);

    }

    public boolean isFinishedOrStopped() throws Exception {
        Method  isFinishedOrStopped  =  transClass.getDeclaredMethod("isFinishedOrStopped");
        return (boolean) isFinishedOrStopped.invoke(trans);
    }

    public Long getNrLinesInput() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesInput").invoke(result);
    }

    public Long getNrLinesOutput() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesOutput").invoke(result);
    }

    public Long getNrLinesRead() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesRead").invoke(result);
    }

    public Object getResult() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return result;
    }

    public Long getNrLinesUpdated() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesUpdated").invoke(result);
    }

    public Long getNrLinesDeleted() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesDeleted").invoke(result);
    }

    public Long getNrLinesWritten() throws Exception {
        Method  getResult  =  transClass.getDeclaredMethod("getResult");
        Object result = getResult.invoke(trans);
        return (Long) result.getClass().getDeclaredMethod("getNrLinesWritten").invoke(result);
    }

    public void stop() throws Exception {

        try {
            if(KettleEngine.EXECTYPE_LOCAL == execType){
                Method  stopAll  =  transClass.getDeclaredMethod("stopAll");
                stopAll.invoke(trans);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){


            }else if(KettleEngine.EXECTYPE_HA == execType){

            }
        }catch (Exception e){
            throw e;
        }


    }
}
