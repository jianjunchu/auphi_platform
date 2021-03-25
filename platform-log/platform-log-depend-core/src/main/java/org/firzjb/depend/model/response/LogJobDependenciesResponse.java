package org.firzjb.depend.model.response;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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

public class LogJobDependenciesResponse  {

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


}
