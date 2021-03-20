package com.aofei.schedule.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 依赖调度关系表
 * </p>
 *
 * @author Tony
 * @since 2021-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("QRTZ_JOB_DEPENDENCIES")
public class JobDependencies extends Model<JobDependencies> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "DEPEND_ID", type = IdType.ID_WORKER)
    private Long dependId;
    @TableField("SCHED_NAME")
    private String schedName;
    /**
     * 调度名称
     */
    @TableField("JOB_NAME")
    private String jobName;
    /**
     * 调度分组
     */
    @TableField("JOB_GROUP")
    private String jobGroup;

    @TableField(exist = false)
    private String jobGroupName;

    /**
     * 开始的调度分组
     */
    @TableField("START_JOB_GROUP")
    private String startJobGroup;
    /**
     * 开始的调度名称
     */
    @TableField("START_JOB_NAME")
    private String startJobName;
    /**
     * 下一个调度分组
     */
    @TableField("NEXT_JOB_GROUP")
    private String nextJobGroup;
    /**
     * 下一个调度名称
     */
    @TableField("NEXT_JOB_NAME")
    private String nextJobName;


    @Override
    protected Serializable pkVal() {
        return this.dependId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDependencies that = (JobDependencies) o;
        return Objects.equals(schedName, that.schedName) &&
                Objects.equals(jobName, that.jobName) &&
                Objects.equals(jobGroup, that.jobGroup) &&
                Objects.equals(startJobGroup, that.startJobGroup) &&
                Objects.equals(startJobName, that.startJobName) &&
                Objects.equals(nextJobGroup, that.nextJobGroup) &&
                Objects.equals(nextJobName, that.nextJobName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, jobName, jobGroup, startJobGroup, startJobName, nextJobGroup, nextJobName);
    }
}
