package org.firzjb.schedule.job;

import org.firzjb.depend.entity.LogJobDependencies;
import org.firzjb.kettle.JobExecutor;
import org.firzjb.kettle.TransExecutor;
import org.firzjb.schedule.entity.JobDependencies;
import org.firzjb.schedule.service.IDependScheduleService;
import org.firzjb.schedule.util.TSortUtils;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.pentaho.di.repository.RepositoryObjectType;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DependRunner extends QuartzJobBean {

    private static Logger logger = Logger.getLogger(DependRunner.class);

    Map<Task,ArrayList<Task>> taskTrees ;

    private ArrayList<Task> tasksReady ;
    private ArrayList<Task> tasksRunning = new ArrayList<Task>();
    private ArrayList<Task> tasksFinished = new ArrayList<Task>();
    private ArrayList<Task> tasksFailed = new ArrayList<Task>();

    final long sleepTime = 15000L ;
    final int maxCountTaskThread = 5 ;
    final String notifyFlag = "" ;
    final boolean stopIfFailed = false ;

    @Autowired
    private Scheduler dependScheduler;

    @Autowired
    private Scheduler quartzScheduler;

    private JobDetail dependJobDetail;

    // 静态共享线程池，如果改为实例独享的线程池，需要在execute中增加释放的处理代码
    static ExecutorService executor = Executors.newFixedThreadPool(10) ;
    public static List<JobDependencies> runningDSchedules = new ArrayList<JobDependencies>() ;

    @Resource
    IDependScheduleService dependScheduleService;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        dependJobDetail = context.getJobDetail() ;

        if(this.taskTrees == null){
            Map<JobKey,ArrayList<JobKey>> dtree = dependScheduleService.getDependJobKeyTree(dependJobDetail.getKey().getGroup(),dependJobDetail.getKey().getName());
            init(dtree) ;
        }
        try {
            schedule();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化
     * @param trees
     */
    private void init(Map<JobKey, ArrayList<JobKey>> trees ){
        taskTrees = new TreeMap<Task,ArrayList<Task>>() ;

        Map<JobKey,Task> tempMaps =new HashMap<JobKey,Task>() ;

        Set<JobKey> keys = trees.keySet() ;
        for(JobKey key:keys){
            Task Task = tempMaps.get(key) ;
            if(Task == null)
            {
                Task = new Task(key) ;
                tempMaps.put(key, Task) ;
            }

            ArrayList<JobKey> Tasknames = trees.get(key) ;
            for(JobKey Taskname:Tasknames){
                Task = tempMaps.get(Taskname) ;
                if(Task == null)
                {
                    Task = new Task(Taskname) ;
                    tempMaps.put(Taskname, Task) ;
                }
            }
        }

        for(JobKey key: tempMaps.keySet()){
            ArrayList<JobKey> dependentTasknames = trees.get(key) ;
            if(dependentTasknames == null)
                dependentTasknames = new ArrayList<JobKey>() ;

            Task Task = tempMaps.get(key) ;
            ArrayList<Task> dependentTasks = Task.getDependentTasks() ;
            for(JobKey Taskname:dependentTasknames){
                Task dependentTask = tempMaps.get(Taskname) ;
                dependentTasks.add(dependentTask) ;
            }

            taskTrees.put(Task, dependentTasks) ;

        }
    }

    /**
     * 调度
     *
     * **/
    private boolean schedule() throws Exception{

        tasksReady = taskSort(taskTrees) ;

        // 将今天已经执行完成的作业移动到
        removeTasksFinishedToday() ;

        boolean existsFailedTask = false ;
        while(true){

            // 等待执行的调度
            for(int i = 0 ; i < tasksReady.size() ; i++)
            {
                final Task task = tasksReady.get(i) ;
                if(tasksRunning.size() >= maxCountTaskThread)
                    break ;
                if(isTaskReady(task)){
                    taskRunning(task) ;
                    executor.execute(new TaskExec(task)) ;

                    i -- ;
                } else if (existsFailedDependentTask(task)){
                    taskFailed(task,false) ;
                    i -- ;
                    System.err.println("Failed to start "+task.getName()+" because there exists failed dependent task!");
                }

            }
            // 正在运行的调度
            boolean hasNewFinishedJob = false ;
            for(int i = 0 ; i < tasksRunning.size() ; i++){
                Task task = tasksRunning.get(i) ;
                TaskStatus status = queryTaskStatus(task) ;
                if(status == TaskStatus.finished){
                    taskFinished(task) ;
                    hasNewFinishedJob = true ;
                    i -- ;
                }
                else if(status == TaskStatus.failed)
                {
                    taskFailed(task,true) ;
                    existsFailedTask = true;
                    i -- ;
                    if(stopIfFailed)
                        throw new Exception(task.getName()+" failed!") ;
                }

                //TODO:  任务超时处理
                if(task.isTimeout())
                {
                }
            }

            if(hasNewFinishedJob)
                continue ;


            if(isAllTaskScheduled())
                break ;

            synchronized(notifyFlag){
                notifyFlag.wait(sleepTime) ;
            }

        }
        return !existsFailedTask;
    }

    /**
     * 将今天已经成功执行过的调度移动至已完成队列
     *
     * */
    private void removeTasksFinishedToday() throws Exception{

        for(int i = 0 ; i < tasksReady.size() ; i++)
        {
            Task task = tasksReady.get(i) ;

            JobKey jobKey = task.getName();


        }
    }

    /**
     * 拓扑排序
     *
     * */
    public static ArrayList<Task> taskSort(Map<Task, ArrayList<Task>> trees) throws Exception
    {
        TSortUtils.tSortFix(trees) ;
        return TSortUtils.tSort(trees) ;
    }

    public static ArrayList<String> tSort(Map<String, ArrayList<String>> trees) throws Exception
    {
        TSortUtils.tSortFix(trees) ;
        return TSortUtils.tSort(trees) ;
    }

    private TaskStatus queryTaskStatus(Task task) throws SchedulerException {

        // 其他视为异常
//		System.out.println(status) ;
        return task.getStatus();

    }

    private void taskRunning(Task task){

        synchronized(this){
            int monitorId = 1;
            task.setStatus(TaskStatus.running) ;
            task.setStarttime(System.currentTimeMillis()) ;
            task.setMonitorId(monitorId) ;
            tasksReady.remove(task) ;
            if(!tasksRunning.contains(task))
                tasksRunning.add(task) ;
            else
                System.err.println(task.getName()+" run twice!");
        }
    }

    private void taskFinished(Task task){

        synchronized(this){
            task.setStatus(TaskStatus.finished) ;
            task.setEndtime(System.currentTimeMillis()) ;
            tasksRunning.remove(task) ;
            if(!tasksFinished.contains(task))
                tasksFinished.add(task) ;
            else
                System.err.println(task.getName()+" run twice!");
        }
    }

    /**
     * 任务失败：运行失败或因为存在失败的依赖任务导致无法运行
     *
     * */
    private void taskFailed(Task task,boolean failedWhileExecute){
        synchronized(this){
            task.setStatus(TaskStatus.failed) ;
            task.setEndtime(System.currentTimeMillis()) ;
            if(failedWhileExecute)
                tasksRunning.remove(task) ;
            else
                tasksReady.remove(task) ;
            if(!tasksFailed.contains(task))
                tasksFailed.add(task) ;
        }
    }


    /**
     * 任务已经成功执行
     *
     * */
    private void taskAlreadyFinished(Task task){
        synchronized(this){
            task.setStatus(TaskStatus.finished) ;
            tasksReady.remove(task) ;
            if(!tasksFinished.contains(task))
                tasksFinished.add(task) ;
        }
    }

    /**
     * 所有依赖的任务完成后，任务即处于就绪状态
     *
     * **/
    private boolean isTaskReady(Task task){
        ArrayList<Task> dependentTasks = task.getDependentTasks() ;
        for(Task dTask: dependentTasks){
            if(!isTaskFinished(dTask))
                return false ;
        }
        return true ;
    }


    private boolean isTaskFailed(Task task){
        return tasksFailed.contains(task) ;
    }


    private boolean isTaskFinished(Task task){
        return tasksFinished.contains(task) ;
    }

    /**
     * 所有任务已经完成调度执行：成功结束或失败中止
     *
     * **/
    private boolean isAllTaskScheduled(){
        return  this.tasksRunning.size() == 0
                && this.tasksReady.size() == 0
                && (this.tasksFinished.size()+this.tasksFailed.size() == this.taskTrees.size());
    }

    /**
     * 存在失败的依赖任务
     *
     * **/
    private boolean existsFailedDependentTask(Task task){
        ArrayList<Task> dependentTasks = task.getDependentTasks() ;
        for(Task dTask: dependentTasks){
            if(isTaskFailed(dTask))
                return true ;
        }
        return false ;
    }


    class TaskExec implements Runnable{
        final Task task ;
        TaskExec(final Task Task){
            this.task = Task ;
        }

        @SneakyThrows
        public void run()
        {

            JobKey jobKey =  task.getName();

            Integer id = task.getMonitorId() ;

            LogJobDependencies log = new LogJobDependencies();
            log.setSchedName("dependScheduler");
            if(dependScheduleService.isDependSchedule(jobKey.getName(), jobKey.getGroup())){
                System.out.println("1111111111111111111111111111");

            } else{


                JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
                if((JobRunner.class.getName().equals(jobDetail.getJobClass().getName()))){
                    JobRunner jobRunner = new JobRunner();
                    JobExecutor jobExecutor =  jobRunner.executeJob(jobDetail,new Date());

                    while(!jobExecutor.isFinished()) {
                        Thread.sleep(500);
                    }
                    log.setErrors(jobExecutor.getErrCount());
                    log.setJobType(RepositoryObjectType.JOB.getTypeDescription());
                    log.setChannelId(jobExecutor.getExecutionId());
                    log.setStartdate(jobExecutor.getStartDate());
                    log.setEnddate(jobExecutor.getEndDate());

                    if(jobExecutor.getErrCount()>0){
                        task.setStatus(TaskStatus.failed);
                    }else{
                        taskFinished(task);
                    }

                }else if(TransRunner.class.getName().equals(jobDetail.getJobClass().getName())){
                    TransRunner transRunner = new TransRunner();

                    TransExecutor transExecutor =  transRunner.executeTrans(jobDetail,new Date());


                    while(!transExecutor.isFinished()) {
                        Thread.sleep(500);

                    }
                    log.setErrors(transExecutor.getErrCount());

                    log.setJobType(RepositoryObjectType.TRANSFORMATION.getTypeDescription());
                    log.setChannelId(transExecutor.getExecutionId());
                    log.setStartdate(transExecutor.getStartDate());
                    log.setEnddate(transExecutor.getEndDate());
                    if(transExecutor.getErrCount()>0){
                        task.setStatus(TaskStatus.failed);
                    }else{
                        taskFinished(task);
                    }

                }
            }

            log.setJobGroup(dependJobDetail.getKey().getGroup());
            log.setJobName(dependJobDetail.getKey().getName());
            log.setExecuteJobGroup(task.getName().getGroup());
            log.setExecuteJobName(task.getName().getName());

            log.insert();



            synchronized(notifyFlag){
                notifyFlag.notifyAll() ;
            }
        }
    }


}
