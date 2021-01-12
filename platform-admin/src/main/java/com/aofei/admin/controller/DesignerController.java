package com.aofei.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aofei.base.common.Const;
import com.aofei.base.controller.BaseController;
import com.aofei.base.exception.ApplicationException;
import com.aofei.sys.model.response.UserResponse;
import com.aofei.sys.service.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 设计器登录控制器
 *
 */
@Api(tags = { "系统管理-登录认证模块接口" })
@RestController
public class DesignerController extends BaseController {


    @Autowired
    private IUserService userService;


    /**
     * 设计器登录接口api
     * @param action action(非必须,兼容老版本)
     * @param user_name 用户名
     * @param password 密码
     * @return 返回资源库信息(数据库连接信息)
     * @throws Exception
     */
    @ApiOperation(value = "设计器登录接口(兼容老版本)", notes = "设计器登录接口(兼容老版本)")
    @ApiResponses(value = {
            @ApiResponse(code = 200001, message = "invalid username or password"),
            @ApiResponse(code = 200002, message = "captcha error"),
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject login(
            @ApiParam(value = "action(兼容老版本)", required = false)  @RequestParam(value = "action") String action,
            @ApiParam(value = "用户名", required = true)  @RequestParam(value = "user_name") String user_name,
            @ApiParam(value = "密码",   required = true)  @RequestParam(value = "password") String password) throws Exception {

        JSONObject object = new JSONObject();


        //验证码校验
        UserResponse user = null;
        try {
            //用户名密码验证
            user = userService.auth(user_name, password);
            object.put("priviledges",-1);
            object.put("user_id",user.getUserId());
            object.put("user_name",user.getNickName());
            object.put("status",0);
            object.put("rep_list",null);

            JSONArray rep_list = new JSONArray();

            JSONObject rep = new JSONObject();
            rep.put("rep_ID",1);
            rep.put("password", Const.getConfig("jdbc.password"));
            rep.put("DBAccess", "Native");
            rep.put("rep_name", "Default");
            rep.put("user_name", Const.getConfig("jdbc.username"));
            rep.put("DBHost", Const.getConfig("jdbc.host"));
            rep.put("DBName", Const.getConfig("jdbc.database"));
            rep.put("DBType", Const.getConfig("jdbc.type"));
            rep.put("version", "V3.0");
            rep.put("DBPort", Const.getConfig("jdbc.port"));

            rep_list.add(rep);
            object.put("rep_list",rep_list);


        }catch (ApplicationException e){
            object.put("priviledges",0);
            object.put("user_id",null);
            object.put("user_name",null);
            object.put("status",6);
            object.put("rep_list",null);

        }

        return object;


    }
}
