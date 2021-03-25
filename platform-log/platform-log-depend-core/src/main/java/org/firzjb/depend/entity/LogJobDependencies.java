package org.firzjb.depend.entity;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("LOG_JOB_DEPENDENCIES")
public class LogJobDependencies extends Model<LogJobDependencies> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "LOG_ID", type = IdType.ID_WORKER)
    private Long logId;
    @TableField("SCHED_NAME")
    private String schedName;
    /**
     * 事件调度名称
     */
    @TableField("JOB_NAME")
    private String jobName;
    /**
     * 事件调度分组
     */
    @TableField("JOB_GROUP")
    private String jobGroup;

    @TableField(exist = false)
    private String jobGroupName;
    /**
     * 执行的调度名称
     */
    @TableField("EXECUTE_JOB_NAME")
    private String executeJobName;
    /**
     * 执行的调度分组
     */
    @TableField("EXECUTE_JOB_GROUP")
    private String executeJobGroup;

    @TableField(exist = false)
    private String executeJobGroupName;

    @TableField("STARTDATE")
    private Date startdate;
    @TableField("ENDDATE")
    private Date enddate;
    /**
     * TRANSFORMATION/JOB
     */
    @TableField("JOB_TYPE")
    private String jobType;
    /**
     * LOG_JOB/LOG_TRANS ExecutionId
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 错误数量
     */
    @TableField("ERRORS")
    private Long errors;
    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.logId;
    }

}
