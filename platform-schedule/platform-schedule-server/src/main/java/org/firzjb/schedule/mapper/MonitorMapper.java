package org.firzjb.schedule.mapper;


import org.apache.ibatis.annotations.Param;
import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.schedule.entity.Monitor;
import org.firzjb.schedule.model.request.MonitorRequest;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

@MyBatisMapper
public interface MonitorMapper extends BaseMapper<Monitor> {
    /**
     * 作业耗时前五
     *
     * @param page
     * @param organizerId
     * @return
     */
    List<Map<String, Object>> getTimeConsumingTop5(Page page, @Param("organizerId")Long organizerId);

    /**
     * 最近七天的错误数和完成数
     * @param organizerId
     * @return
     */
    List<Map<String, Object>> get7DayErrorsAndFinishs(@Param("organizerId")Long organizerId);

    /**
     * 正在运行的作业数量
     * @param organizerId
     * @return
     */
    int countRuning(@Param("organizerId")long organizerId);

    /**
     * 运行完成作业
     * @param organizerId
     * @return
     */
    int countFinish(@Param("organizerId")long organizerId);
    /**
     * 错误总数
     * @param organizerId
     * @return
     */
    int countError(@Param("organizerId")long organizerId);

    /**
     * 查询是否有计划时间日志
     * @param request
     * @return
     */
    List<Monitor> findPlanMonitor(MonitorRequest request);

    /**
     * 总作业数量
     * @param organizerId
     * @return
     */
    int countAll(@Param("organizerId")long organizerId);

    /**
     * 获取正在执行调度数量
     * @param jobName
     * @param jobGroup
     * @return
     */
    int countScheduleRuning(@Param("jobName")String jobName, @Param("jobGroup")String jobGroup);
}
