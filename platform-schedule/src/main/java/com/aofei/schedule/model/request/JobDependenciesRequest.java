package com.aofei.schedule.model.request;

import com.aofei.base.model.request.BaseRequest;
import lombok.Data;

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

public class JobDependenciesRequest extends BaseRequest<JobDependenciesRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long dependId;

    private String schedName;

    /**
     * 调度名称
     */
    private String jobName;
    /**
     * 调度分组
     */
    private String jobGroup;
    /**
     * 开始的调度分组
     */
    private String startJobGroup;
    /**
     * 开始的调度名称
     */
    private String startJobName;
    /**
     * 下一个调度分组
     */
    private String nextJobGroup;
    /**
     * 下一个调度名称
     */
    private String nextJobName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDependenciesRequest that = (JobDependenciesRequest) o;
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
