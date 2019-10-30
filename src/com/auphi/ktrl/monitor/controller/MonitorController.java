package com.auphi.ktrl.monitor.controller;

import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.util.JsonHelper;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.monitor.service.MonitorService;
import com.auphi.ktrl.schedule.util.JobExecutor;
import com.auphi.ktrl.schedule.util.QuartzUtil;
import com.auphi.ktrl.schedule.util.TransExecutor;
import com.auphi.ktrl.system.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApiIgnore
@Controller("monitor")
public class MonitorController extends BaseMultiActionController {

    private final static String INDEX = "admin/monitor/index";

    @Autowired
    private MonitorService monitorService;


    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){

        String jobName = ServletRequestUtils.getStringParameter(req,"jobName","");

        ModelMap map=new ModelMap();
        map.put("jobName",jobName);
        return new ModelAndView(INDEX,map);
    }



    public ModelAndView query(HttpServletRequest req, HttpServletResponse resp, MonitorScheduleBean monitor) throws IOException {

        try {


            PaginationSupport<MonitorScheduleBean> page = monitorService.getPage(monitor);
            String jsonString = JsonHelper.encodeObject2Json(page);
            write(jsonString, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView get(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Long id = ServletRequestUtils.getLongParameter(req,"id");
            MonitorScheduleBean monitorSchedule  = monitorService.selectById(id);
            this.setOkTipMsg("SUCCESS",monitorSchedule, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView stop(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String ids = ServletRequestUtils.getStringParameter(req,"ids");
            String[] idArray = ids.split(",");
            for(String idStr : idArray){
                Long id = Long.parseLong(idStr);

                TransExecutor  transExecutor =  TransExecutor.getExecutor(id);
                if(transExecutor!=null){
                    transExecutor.stop();
                }

                JobExecutor jobExecutor =  JobExecutor.getExecutor(id);
                if(jobExecutor!=null){
                    jobExecutor.stop();
                }
            }


            this.setOkTipMsg("SUCCESS",resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String ids = ServletRequestUtils.getStringParameter(req,"ids");
            String[] idArray = ids.split(",");
            for(String idStr : idArray){
                Long id = Long.parseLong(idStr);
                monitorService.deleteById(id);
            }


            this.setOkTipMsg("SUCCESS",resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView clear(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String clearDate = ServletRequestUtils.getStringParameter(req,"clearDate");
            monitorService.deleteBySartDate(clearDate);

            this.setOkTipMsg("SUCCESS",resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView resume(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            UserBean user = (UserBean)req.getSession().getAttribute("userBean");
            Long id = ServletRequestUtils.getLongParameter(req,"id");
            MonitorScheduleBean monitorSchedule  = monitorService.selectById(id);
            QuartzUtil.execute(new String[]{monitorSchedule.getJobName()}, user);
            this.setOkTipMsg("SUCCESS",resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }


}
