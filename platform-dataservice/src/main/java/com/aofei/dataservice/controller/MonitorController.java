package com.aofei.dataservice.controller;


import com.aofei.base.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  服务接口监控控制器
 * </p>
 *
 * @author Tony
 * @since 2018-11-10
 */
@Log4j
@Controller("serviceMonitorController")
@RequestMapping("/dataservice/monitor")
@Api(tags = { "数据服务-数据发布访问监控管理" })
public class MonitorController extends BaseController {

}

