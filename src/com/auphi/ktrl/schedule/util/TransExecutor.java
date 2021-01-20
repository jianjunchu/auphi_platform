package com.auphi.ktrl.schedule.util;

import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.ha.bean.SlaveServerBean;
import com.auphi.ktrl.ha.util.SlaveServerUtil;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.system.mail.util.MailUtil;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.ClassLoaderUtil;
import com.auphi.ktrl.util.StringUtil;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.logging.CentralLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.www.SlaveServerTransStatus;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

public class TransExecutor implements Runnable{

    private int execType;
    private MonitorScheduleBean monitorSchedule;
    private TransExecutionConfiguration executionConfiguration;
    private TransMeta transMeta = null;
    private Trans trans = null;


    private Repository repository;

    private Object transSplitter;

    private static ClassLoaderUtil classLoaderUtil = new ClassLoaderUtil();

    private boolean finished = false;
    private int errCount;

    public long getErrCount() {
        return errCount;
    }

    private int startLineNr = 0;

    private String carteObjectId = null;

    private  String transMetaName;


    public TransExecutor(MonitorScheduleBean monitorSchedule, Trans trans, int execType) throws Exception {
        this.monitorSchedule = monitorSchedule;
        this.trans = trans;
        this.execType = execType;

        try {
            CentralLogStore.init(100000, Const.MAX_NR_LOG_LINES);

            this.transMeta = trans.getTransMeta();

            this.transMetaName = transMeta.getName();

            repository = trans.getRepository();


            this.executionConfiguration = new TransExecutionConfiguration();

            this.executionConfiguration.setRepository(repository);;


            if(KettleEngine.EXECTYPE_LOCAL == execType){

                this.executionConfiguration.setExecutingLocally(true);
                this.executionConfiguration.setExecutingRemotely(false);
                this.executionConfiguration.setExecutingClustered(false);


            }else if(KettleEngine.EXECTYPE_REMOTE == execType){

                this.executionConfiguration.setExecutingLocally(false);
                this.executionConfiguration.setExecutingRemotely(true);
                this.executionConfiguration.setExecutingClustered(false);

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                SlaveServer slaveServer = createSlaveServer(slaveServerBean);

                executionConfiguration.setRemoteServer(slaveServer);


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){

                this.executionConfiguration.setExecutingLocally(false);
                this.executionConfiguration.setExecutingRemotely(false);
                this.executionConfiguration.setExecutingClustered(true);

                executionConfiguration.setClusterPosting(true);
                executionConfiguration.setClusterPreparing(true);
                executionConfiguration.setClusterStarting(true);
                executionConfiguration.setClusterShowingTransformation(true);



            }else if(KettleEngine.EXECTYPE_HA == execType){
                this.executionConfiguration.setExecutingLocally(false);
                this.executionConfiguration.setExecutingRemotely(true);
                this.executionConfiguration.setExecutingClustered(false);

                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                SlaveServer slaveServer = createSlaveServer(slaveServerBean);
                executionConfiguration.setRemoteServer(slaveServer);
            }

        }catch (Exception e){
            throw e;
        }


    }

    private static Hashtable<Integer, TransExecutor> executors = new Hashtable<Integer, TransExecutor>();

    public static synchronized TransExecutor initExecutor(MonitorScheduleBean monitorSchedule, Trans trans, int execType) throws Exception {
        TransExecutor transExecutor = new TransExecutor(monitorSchedule,trans,execType);
        executors.put(monitorSchedule.getId(), transExecutor);
        return transExecutor;
    }

    public static TransExecutor getExecutor(Integer executionId) {
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
                startLineNr = 0;

                this.trans.setSafeModeEnabled(executionConfiguration.isSafeModeEnabled());
                this.trans.setLogLevel(executionConfiguration.getLogLevel());
                this.trans.setReplayDate(executionConfiguration.getReplayDate());
                this.trans.setRepository(executionConfiguration.getRepository());

                //trans execute
                this.trans.execute(transMeta.getArguments());

                while (running){
                    Thread.sleep(500);
                    running = ! this.trans.isFinished();
                }
                Date stop=new Date();

                //logs
                LogChannelInterface logChannel =  this.trans.getLogChannel();

                logChannel.logMinimal("ETL--TRANS Finished", new Object[0]);
                logChannel.logMinimal("ETL--TRANS Start="+StringUtil.DateToString(monitorSchedule.getStartTime(), "yyyy/MM/dd HH:mm:ss")+", Stop="+StringUtil.DateToString(stop, "yyyy/MM/dd HH:mm:ss"), new Object[0]);
                long millis=stop.getTime()-monitorSchedule.getStartTime().getTime();
                logChannel.logMinimal("ETL--TRANS Processing ended after "+(millis/1000)+" seconds.", new Object[0]);

                errCount = (int)trans.getClass().getDeclaredMethod("getErrors").invoke(trans);

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){
                SlaveServerBean slaveServerBean = getSlaveServerBean(monitorSchedule.getServerName(), "");
                SlaveServer slaveServer = createSlaveServer(slaveServerBean);
                executionConfiguration.setRemoteServer(slaveServer);
                String carteObjectId =  trans.sendToSlaveServer(transMeta, this.executionConfiguration, repository);

                Method getTransStatus = slaveServer.getClass().getDeclaredMethod("getTransStatus",String.class,String.class,int.class);

                while(running) {
                    SlaveServerTransStatus transStatus = slaveServer.getTransStatus(transMetaName,carteObjectId,0);
                    running = transStatus.isRunning();
                    Result result = transStatus.getResult();
                    if(!running && result != null) {
                        errCount = Long.valueOf(result.getNrErrors()).intValue();
                    }
                    Thread.sleep(500);
                }

            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){



            }else if(KettleEngine.EXECTYPE_HA == execType){
                SlaveServerBean slaveServerBean = getSlaveServerBean("", monitorSchedule.getHaName());
                SlaveServer slaveServer = createSlaveServer(slaveServerBean);
                executionConfiguration.setRemoteServer(slaveServer);
                String carteObjectId =  trans.sendToSlaveServer(transMeta, this.executionConfiguration, repository);

                Method getTransStatus = slaveServer.getClass().getDeclaredMethod("getTransStatus",String.class,String.class,int.class);

                while(running) {
                    SlaveServerTransStatus transStatus = slaveServer.getTransStatus(transMetaName,carteObjectId,0);
                    running = transStatus.isRunning();
                    Result result = transStatus.getResult();
                    if(!running && result != null) {
                        errCount = Long.valueOf(result.getNrErrors()).intValue();
                    }
                    Thread.sleep(500);
                }
            }


        } catch (Exception  e) {

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

        }finally {
            finished = true;
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


            try {
                if(executionConfiguration.isExecutingLocally()) {

                    //logs
                    LogChannelInterface logChannel =  this.trans.getLogChannel();

                    String logChannelId =   logChannel.getLogChannelId();


                    String loggingText = CentralLogStore.getAppender().getBuffer(
                            logChannelId, false, startLineNr, CentralLogStore.getLastBufferLineNr()).toString();
                    return loggingText;

                } else if(executionConfiguration.isExecutingRemotely()) {
                    SlaveServer remoteSlaveServer = executionConfiguration.getRemoteServer();
                    SlaveServerTransStatus transStatus = remoteSlaveServer.getTransStatus(transMetaName,carteObjectId,0);
                    return  transStatus.getLoggingString();
                }
            }catch (Exception e){
                return "";
            }


            return "";
    }

    public boolean isFinished() throws Exception {
        return finished;

    }
    public int getErrors() throws Exception {


        return errCount;

    }
    public boolean isStopped() throws Exception {

        return this.trans.isStopped();

    }


    public Long getNrLinesInput() throws Exception {

        return trans.getResult().getNrLinesInput();
    }

    public Long getNrLinesOutput() throws Exception {

        return trans.getResult().getNrLinesOutput();
    }

    public Long getNrLinesRead() throws Exception {

        return trans.getResult().getNrLinesRead();
    }

    public Result getResult() throws Exception {

        return trans.getResult();
    }

    public Long getNrLinesUpdated() throws Exception {
        return trans.getResult().getNrLinesUpdated();
    }

    public Long getNrLinesDeleted() throws Exception {
        return trans.getResult().getNrLinesDeleted();
    }

    public Long getNrLinesWritten() throws Exception {
        return trans.getResult().getNrLinesWritten();
    }

    public void stopAllForcely() throws Exception{

        this.trans.stopAllForcely();
    }

    public void stop() throws Exception {

        try {
            if(KettleEngine.EXECTYPE_LOCAL == execType){
                trans.stopAll();

            }else if(KettleEngine.EXECTYPE_REMOTE == execType){


            }else if(KettleEngine.EXECTYPE_CLUSTER == execType){


            }else if(KettleEngine.EXECTYPE_HA == execType){

            }
        }catch (Exception e){
            throw e;
        }


    }



    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }



    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
