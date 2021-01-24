package com.aofei.schedule.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  首页统计响应类
 *
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Data
public class JobPlanResponse implements Comparable<JobPlanResponse>{


    /**
     * 调度名称
     */
    private String qrtzJobName;

    /**
     * 调度分组
     */
    private String qrtzJobGroup;

    /**
     * 计划执行时间
     */
    private Date planRunTime;

    /**
     * 计划执行时间
     */
    private String planRunTimeStr;


    /**
     * 状态
     */
    private String status;

    /**
     * 开始时间
     */
    private Date  startTime;

    /**
     * 结束时间
     */
    private Date  endTime;

    /**
     * 错误数量
     */
    private Long errors;

    @ApiModelProperty(value = "TRANSFORMATION or JOB")
    private String fileType;

    private Long logId;

    @Override
    public int compareTo(JobPlanResponse o) {
        return Long.valueOf(this.planRunTime.getTime() - o.getPlanRunTime().getTime()).intValue();
    }


}
