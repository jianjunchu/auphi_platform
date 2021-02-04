package com.aofei.schedule.model.response;

import lombok.Data;

/**
 * <p>
 * 依赖调度关系表
 * </p>
 *
 * @author Tony
 * @since 2021-02-02
 */
@Data
public class JobDependenciesResponse  {

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


}
