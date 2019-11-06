package com.auphi.ktrl.schedule.task;

import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.schedule.util.TransExecutor;
import com.auphi.ktrl.system.mail.util.MailUtil;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.StringUtil;
import org.pentaho.reporting.libraries.repository.Repository;

import java.util.Date;
import java.util.TimerTask;

public class TransLogTimerTask extends TimerTask {

    private TransExecutor transExecutor;

    private boolean first = true;

    public TransLogTimerTask(TransExecutor transExecutor) {
        this.transExecutor = transExecutor;
    }

    @Override
    public void run() {
        try {


            if(transExecutor!=null){
                MonitorUtil.updateMonitorExecutionLog(transExecutor.getMonitorSchedule().getId(),transExecutor.getExecutionLog());

                if(transExecutor.isFinished() || transExecutor.isStopped()){

                    MonitorScheduleBean monitorScheduleBean = transExecutor.getMonitorSchedule();
                    monitorScheduleBean.setLogMsg(transExecutor.getExecutionLog());
                    monitorScheduleBean.setEndTime(new Date() );
                    if(transExecutor.isStopped() ||  transExecutor.getErrors()!=0){
                        monitorScheduleBean.setJobStatus(MonitorUtil.STATUS_ERROR);

                        String title = "[ScheduleError][" + StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "][" + monitorScheduleBean.getJobName() + "]";
                        String errorNoticeUserId = monitorScheduleBean.getErrorNoticeUserId();
                        String[] user_mails = UserUtil.getUserEmails(errorNoticeUserId);
                        MailUtil.sendMail(user_mails, title, monitorScheduleBean.getLogMsg());
                    }else{
                        monitorScheduleBean.setJobStatus(MonitorUtil.STATUS_FINISHED);
                    }
                    monitorScheduleBean.setLines_input(transExecutor.getNrLinesInput());
                    monitorScheduleBean.setLines_deleted(transExecutor.getNrLinesDeleted());
                    monitorScheduleBean.setLines_read(transExecutor.getNrLinesRead());
                    monitorScheduleBean.setLines_output(transExecutor.getNrLinesOutput());
                    monitorScheduleBean.setLines_written(transExecutor.getNrLinesWritten());
                    monitorScheduleBean.setLines_updated(transExecutor.getNrLinesUpdated());

                    monitorScheduleBean.setLines_error(transExecutor.getErrors());
                    MonitorUtil.updateMonitorAfter(monitorScheduleBean);
                    cancel();
                }

            }else{
                cancel();
            }

        } catch (Exception e) {
            cancel();
            e.printStackTrace();
        }
    }
}
