package org.firzjb.translog.model.response;


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
public class LogTransResponse  {

    private static final long serialVersionUID = 1L;

    private Long logTransId;
    /**
     * 对应转换表 R_TRANSFORMATION 中的ID_TRANSFORMATION 字段
     */
    private Long transConfigId;
    private String channelId;
    private String transname;
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
    private String loginfo;
    private String transCnName;

    private String qrtzJobName;
    private String qrtzJobGroup;

}
