package com.aofei.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.Response;
import com.aofei.sys.entity.Config;
import com.aofei.sys.service.IConfigService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口
 */
@Api(tags = { "通用接口" })
@RestController
@RequestMapping(value = "/index", produces = {"application/json;charset=UTF-8"})
public class IndexController extends BaseController {


    @Autowired
    private IConfigService configService;

    /**
     * 获取手机注册验证码
     * 系统配置文件
     * @return
     */
    @ApiOperation(value = "系统配置文件", notes = "系统配置文件")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/getSysConfigInfo", method = RequestMethod.GET)
    public Response<JSONObject> getSysConfigInfo()  {
        JSONObject jsonObject = new JSONObject();
        List<Config> list =  configService.selectList(new EntityWrapper<Config>().eq("DEL_FLAG",0));

        for(Config config:list){
            jsonObject.put(config.getName(),config.getValue());
        }

        return Response.ok(jsonObject);

    }
}
