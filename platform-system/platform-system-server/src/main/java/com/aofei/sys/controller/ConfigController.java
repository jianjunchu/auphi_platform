package com.aofei.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.sys.entity.Config;
import com.aofei.sys.service.IConfigService;
import com.aofei.sys.service.IUserService;
import com.aofei.utils.CsvUtils;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-09-12 20:07
 */
@Log4j
@Api(tags = { "系统管理-系统配置" })
@RestController
@RequestMapping(value = "/sys/config", produces = {"application/json;charset=UTF-8"})
public class ConfigController extends BaseController {



    @Autowired
    IConfigService configService;

    @Autowired
    IUserService userService;

    @Authorization
    @ApiOperation(value = "系统配置", notes = "修改系统配置")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response<Boolean> update(
            @RequestBody JSONObject reques,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        if(reques!=null){
            JSONObject data = reques.getJSONObject("data");
            String groupCode = reques.getString("groupCode");

            if(data!=null){
                for (Map.Entry<String, Object> entry : data.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue()!=null ?  entry.getValue().toString() : "";



                    if("HisUserPwd".equals(entry.getKey()) && !StringUtils.isEmpty(value) && !value.startsWith(KettleTwoWayPasswordEncoder.PASSWORD_ENCRYPTED_PREFIX)){
                        value = Encr.encryptPasswordIfNotUsingVariables(value);
                    }

                    configService.delete(new EntityWrapper<Config>().eq("NAME",key));
                    Config config = new Config();
                    config.setGroupCode(groupCode);
                    config.setName(entry.getKey());
                    config.setValue(value);
                    config.preInsert();
                    config.insert();
                }
            }
        }

        return Response.ok(true) ;
    }

    @ApiOperation(value = "系统配置", notes = "获取系统配置")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Response<JSONObject> getAll(
            @ApiParam(value = "用户名")  @RequestParam(value = "groupCode") String groupCode,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        JSONObject jsonObject = new JSONObject();
        EntityWrapper wrapper =  new EntityWrapper<Config>();
        if(!StringUtils.isEmpty(groupCode)){
            wrapper.eq("GROUP_CODE",groupCode);
        }
        List<Config> list = configService.selectList(wrapper);
        for(Config config : list){
            jsonObject.put(config.getName(),config.getValue());
        }

        return Response.ok(jsonObject) ;
    }

    @ApiOperation(value = "系统配置", notes = "获取csv文件")
    @RequestMapping(value = "/getCsvAll", method = RequestMethod.POST)
    public Response<JSONObject> getCsvAll(
            @RequestBody JSONObject jsonObject) throws IOException {

        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+ File.separator+"config.csv";
        List<String[]> list = CsvUtils.read(filePath,false);
        for(String[] cls : list){
            String keyValue = cls[0];
            String[] kv = keyValue.split("=");
            if(kv.length ==2 && jsonObject.containsKey(kv[0])){
                JSONObject object = jsonObject.getJSONObject(kv[0]);
                object.put("value",kv[1]);
            }
        }

        return Response.ok(jsonObject) ;
    }


    @ApiOperation(value = "系统配置", notes = "修改csv文件")
    @RequestMapping(value = "/updateCsvAll", method = RequestMethod.POST)
    public Response<JSONObject> updateCsvAll(
            @RequestBody JSONObject jsonObject) throws IOException {

        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+ File.separator+"config.csv";
        List<String[]> list = CsvUtils.read(filePath,false);
        Map<String,String[]> maps = new HashMap<>();

        for(String[] cls : list){
            maps.put(cls[1],cls);
        }

        for(String key :jsonObject.keySet()){
            JSONObject object = jsonObject.getJSONObject(key);
            String[] cls = maps.get(key);
            if(cls==null){
                cls = new String[4];
            }
            cls[0] = object.getString("key")+"="+object.getString("value");
            cls[1] = object.getString("remark");
            maps.put(key,cls);
        }

        List<String[]> valueList = new ArrayList<String[]>(maps.values());
        String[] head = {"参数配置","备注"};
        CsvUtils.write(filePath,head,valueList);


        return Response.ok(jsonObject) ;
    }
}
