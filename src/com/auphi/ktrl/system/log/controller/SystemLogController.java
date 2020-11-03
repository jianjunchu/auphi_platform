package com.auphi.ktrl.system.log.controller;

import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.core.util.JsonHelper;
import com.auphi.ktrl.system.log.domain.SystemLog;
import com.auphi.ktrl.system.log.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApiIgnore
@Controller
@RequestMapping(value = "/sys_log")
public class SystemLogController extends BaseMultiActionController {


    private final static String INDEX = "admin/sys/sys_log";

    @Autowired
    private SystemLogService systemLogService;

    @RequestMapping(value = "/index")
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){

        return new ModelAndView(INDEX);
    }

    @RequestMapping(value = "/query")
    public ModelAndView query(HttpServletRequest req, HttpServletResponse resp, SystemLog systemLog) throws IOException {

        try {
            PaginationSupport<SystemLog> page = systemLogService.getPage(systemLog);
            String jsonString = JsonHelper.encodeObject2Json(page);
            write(jsonString, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try{
            String ids = ServletRequestUtils.getStringParameter(req,"ids");
            Dto dto = new BaseDto();
            dto.put("ids",ids);
            this.systemLogService.delete(dto);
            this.setOkTipMsg("删除成功", resp);
        }catch(Exception e){
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }


}
