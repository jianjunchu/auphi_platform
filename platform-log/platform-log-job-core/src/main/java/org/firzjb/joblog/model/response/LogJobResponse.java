package org.firzjb.joblog.model.response;


import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-17
 */
@Data
public class LogJobResponse  {

    private static final long serialVersionUID = 1L;

    private Long logJobId;
    /**
     * 对应r_job表中的ID_JOB主键
     */
    private Long jobConfigId;
    /**
     * 唯一，通UUID表示

     */
    private String channelId;

    private String jobName;

    private String jobCnName;

    private String status;

    private Long linesRead;

    private Long linesWritten;

    private Long linesUpdated;

    private Long linesInput;

    private Long linesOutput;

    private Long linesRejected;

    private Long errors;

    private Date startdate;

    private Date enddate;

    private Date logdate;
    private Date depdate;
    private Date replaydate;
    private String logField;
    private String executingServer;
    private String executingUser;
    /**
     * 1:表示本地，2表示远程，3表示集群
     */
    private Integer excutorType;
    private String jobLog;


    private String qrtzJobName;
    private String qrtzJobGroup;

}
