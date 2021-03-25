package org.firzjb.schedule.service;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.schedule.entity.Monitor;
import org.firzjb.schedule.model.request.MonitorRequest;
import org.firzjb.schedule.model.response.DashboardResponse;
import org.firzjb.schedule.model.response.MonitorResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.schedule.entity.Monitor;
import org.firzjb.schedule.model.request.MonitorRequest;
import org.firzjb.schedule.model.response.DashboardResponse;
import org.firzjb.schedule.model.response.MonitorResponse;
import org.pentaho.di.core.exception.KettleException;

public interface IMonitorService extends IService<Monitor> {

    Page<MonitorResponse> getPage(Page<Monitor> page, MonitorRequest request);


    /**
     * 统计信息
     * @param user
     * @return
     */
    DashboardResponse getDashboardCount(CurrentUserResponse user) throws KettleException;

    /**
     * 查询计划任务日志
     * @param request
     * @return
     */
    MonitorResponse getPlanMonitor(MonitorRequest request);

}
