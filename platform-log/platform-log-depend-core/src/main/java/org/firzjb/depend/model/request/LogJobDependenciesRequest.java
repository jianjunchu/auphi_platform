package org.firzjb.depend.model.request;

import org.firzjb.base.model.request.BaseRequest;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 事件调度日志

 * </p>
 *
 * @author Tony
 * @since 2021-02-03
 */
@Data
public class LogJobDependenciesRequest extends BaseRequest<LogJobDependenciesRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long logId;

    private String schedName;
    /**
     * 事件调度名称
     */
    private String jobName;
    /**
     * 事件调度分组
     */
    private String jobGroup;

    private String jobGroupName;
    /**
     * 执行的调度名称
     */
    private String executeJobName;
    /**
     * 执行的调度分组
     */
    private String executeJobGroup;

    private String executeJobGroupName;

    private Date startdate;

    private Date enddate;
    /**
     * TRANSFORMATION/JOB
     */
    private String jobType;
    /**
     * LOG_JOB/LOG_TRANS ExecutionId
     */
    private String channelId;

    private String  searchtime;
    private String  startTime;
    private String  endTime;



}
