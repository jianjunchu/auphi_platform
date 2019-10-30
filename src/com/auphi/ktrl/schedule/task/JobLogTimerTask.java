package com.auphi.ktrl.schedule.task;

import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.util.MonitorUtil;
import com.auphi.ktrl.schedule.util.JobExecutor;

import java.util.Date;
import java.util.TimerTask;

public class JobLogTimerTask extends TimerTask {

    private JobExecutor jobExecutor;

    private boolean first = true;

    public JobLogTimerTask(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    @Override
    public void run() {
        try {


            if(jobExecutor!=null){
                MonitorUtil.updateMonitorExecutionLog(jobExecutor.getMonitorSchedule().getId(),jobExecutor.getExecutionLog());

                if(jobExecutor.isFinished() || jobExecutor.isStopped()){
                    MonitorScheduleBean monitorScheduleBean = jobExecutor.getMonitorSchedule();
                    monitorScheduleBean.setLogMsg(jobExecutor.getExecutionLog());
                    monitorScheduleBean.setEndTime(new Date() );
                    if(jobExecutor.getResult()!=null && jobExecutor.getErrors()!=0){
                        monitorScheduleBean.setJobStatus(MonitorUtil.STATUS_ERROR);
                    }else{
                        monitorScheduleBean.setJobStatus(MonitorUtil.STATUS_FINISHED);
                    }
                    monitorScheduleBean.setLines_input(0L);
                    monitorScheduleBean.setLines_deleted(0L);
                    monitorScheduleBean.setLines_read(0L);
                    monitorScheduleBean.setLines_output(0L);
                    monitorScheduleBean.setLines_written(0L);
                    monitorScheduleBean.setLines_updated(0L);

                    monitorScheduleBean.setLines_error(jobExecutor.getErrors());
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
