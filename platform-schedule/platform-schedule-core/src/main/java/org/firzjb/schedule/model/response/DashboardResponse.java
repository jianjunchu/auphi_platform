package org.firzjb.schedule.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  首页统计响应类
 *
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
@Data
public class DashboardResponse {


    /**
     *运行中作业
     */
    @ApiModelProperty(value = "运行中作业")
    private int runCount;

    /**
     * 运行完成作业
     */
    @ApiModelProperty(value = "运行完成作业")
    private int finishCount;

    /**
     * 总作业数
     */
    @ApiModelProperty(value = "总作业数")
    private int allCount;

    /**
     * 错误作业数
     */
    @ApiModelProperty(value = "错误作业数")
    private int errorCount;

    /**
     * 作业耗时(前五)
     */
    @ApiModelProperty(value = "作业耗时(前五)")
    private RunTimesResponse runTimes;

    /**
     * 近七天完成数和错误数量
     */
    @ApiModelProperty(value = "近七天完成数和错误数量")
    private RunCountResponse runCounts;

}
