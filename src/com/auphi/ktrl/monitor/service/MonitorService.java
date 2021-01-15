package com.auphi.ktrl.monitor.service;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;

import java.sql.SQLException;

public interface MonitorService {

    PaginationSupport<MonitorScheduleBean> getPage(MonitorScheduleBean monitor) throws SQLException;

    MonitorScheduleBean selectById(Long id);

    int deleteById(Long id);

    int deleteBySartDate(String clearDate);

    void updateStatus2Stop(Integer id);
}
