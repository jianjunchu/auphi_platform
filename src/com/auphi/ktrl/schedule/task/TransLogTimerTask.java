package com.auphi.ktrl.schedule.task;

import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.schedule.util.TransExecutor;

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

                if(transExecutor.isFinishedOrStopped()){
                    MonitorScheduleBean monitorScheduleBean = transExecutor.getMonitorSchedule();
                    monitorScheduleBean.setLogMsg(transExecutor.getExecutionLog());
                    monitorScheduleBean.setEndTime(new Date() );
                    if(transExecutor.getResult()!=null && transExecutor.getErrors()!=0){
                        monitorScheduleBean.setJobStatus(MonitorUtil.STATUS_ERROR);
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
