package org.firzjb.schedule.job;

import org.quartz.JobKey;

import java.util.ArrayList;
import java.util.concurrent.Future;

enum TaskStatus{
    ready,
    running,
    suspend,
    finished,
    failed
}

final class Task implements Comparable<Object>{
    private final JobKey name ;	// unique name
    private final ArrayList<Task> dependentTasks ;
    private long starttime ;
    private long endtime ;
    private Integer monitorId  ;
    private Future<?> ft ;

    private TaskStatus status ;


    Task(JobKey jobname){
        this.name = jobname ;
        this.status = TaskStatus.ready ;
        this.dependentTasks = new ArrayList<Task>() ;
    }


    public ArrayList<Task> getDependentTasks() {
        return dependentTasks;
    }

//	public String getJobname() {
//		return name;
//	}

    // 任务是否运行超时
    public boolean isTimeout(){
        return false ;
    }


    public Integer getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Integer monitorId) {
        this.monitorId = monitorId;
    }

    public JobKey getName() {
        return name;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public ArrayList<Task> getDependentJobs() {
        return dependentTasks;
    }

//	public void setDependentJobs(ArrayList<Job> dependentTasks) {
//		this.dependentJobs = dependentTasks;
//	}

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Object o) {
        Task job = (Task)o ;
        return (this.name).compareTo(job.name) ;
    }

    public String toString(){
        return name.toString() ;
    }


    public void setFuture(Future<?> ft) {
        // TODO Auto-generated method stub
        this.ft = ft ;
    }
}
