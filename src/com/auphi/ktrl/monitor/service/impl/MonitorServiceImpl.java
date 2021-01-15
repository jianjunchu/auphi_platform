package com.auphi.ktrl.monitor.service.impl;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.util.DateUtil;
import com.auphi.data.hub.dao.SystemDao;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service("MonitorService")
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    SystemDao systemDao;

    @Override
    public PaginationSupport<MonitorScheduleBean> getPage(MonitorScheduleBean monitorScheduleBean) throws SQLException {
        List<MonitorScheduleBean> items = systemDao.queryForPage("monitorSchedule.selectList", monitorScheduleBean, monitorScheduleBean.getStart(), monitorScheduleBean.getLimit());
        Integer total = (Integer)systemDao.queryForObject("monitorSchedule.selectCount",monitorScheduleBean);
        PaginationSupport<MonitorScheduleBean> page = new PaginationSupport<MonitorScheduleBean>(items, total);
        return page;
    }

    @Override
    public MonitorScheduleBean selectById(Long id) {
        return (MonitorScheduleBean)systemDao.queryForObject("monitorSchedule.selectById",id);
    }

    @Override
    public int deleteById(Long id) {

        return systemDao.delete("monitorSchedule.deleteById",id);
    }

    @Override
    public int deleteBySartDate(String clearDate) {



        MonitorScheduleBean monitorScheduleBean = new MonitorScheduleBean();
        monitorScheduleBean.setStartTime(DateUtil.format(clearDate,"yyyy-MM-dd"));
        return systemDao.delete("monitorSchedule.deleteBySartDate",monitorScheduleBean);
    }

    @Override
    public void updateStatus2Stop(Integer id) {
        systemDao.update("monitorSchedule.updateStatus2Stop",id);
    }
}
