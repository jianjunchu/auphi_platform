package com.auphi.ktrl.system.log.service;


import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.ktrl.system.log.domain.SystemLog;

import java.sql.SQLException;
import java.util.List;

public interface SystemLogService {

    PaginationSupport<SystemLog> getPage(SystemLog systemLog) throws SQLException;

    List<SystemLog> findList(SystemLog dto);


    void save(SystemLog SystemLog);

    SystemLog getSystemLog(SystemLog SystemLog);

    int delete(Dto dto);
}
