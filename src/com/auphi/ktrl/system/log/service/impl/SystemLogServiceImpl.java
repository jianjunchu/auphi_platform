package com.auphi.ktrl.system.log.service.impl;


import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.dao.SystemDao;
import com.auphi.ktrl.system.log.domain.SystemLog;
import com.auphi.ktrl.system.log.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service("SystemLogService")
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    SystemDao systemDao;

    @Override
    public PaginationSupport<SystemLog> getPage(SystemLog dto) throws SQLException {
        List<SystemLog> items = systemDao.queryForPage("systemLog.selectList", dto, dto.getStart(), dto.getLimit());
        Integer total = (Integer)systemDao.queryForObject("systemLog.selectCount",dto);
        PaginationSupport<SystemLog> page = new PaginationSupport<SystemLog>(items, total);
        return page;
    }

    @Override
    public List<SystemLog> findList(SystemLog dto) {
        List<SystemLog> items = systemDao.queryForList("systemLog.selectList", dto);
        return items;
    }

    @Override
    public void save(SystemLog SystemLog) {

    }

    @Override
    public SystemLog getSystemLog(SystemLog SystemLog) {
        return null;
    }

    @Override
    public int delete(Dto dto) {
        return this.systemDao.delete("systemLog.deleteByIds", dto);
    }


}
