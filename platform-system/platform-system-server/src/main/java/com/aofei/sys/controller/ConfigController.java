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
import com.aofei.utils.DesCipherUtil;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);
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
        List<String[]> list = CsvUtils.read(filePath,true);
        for(String[] cls : list){
            String keyValue = cls[0];
            String[] kv = keyValue.split("=");

            if(kv.length ==2 && jsonObject.containsKey(kv[0])){
                JSONObject object = jsonObject.getJSONObject(kv[0]);
                object.put("value",decrypt(kv[0],kv[1]));
            }
        }

        return Response.ok(jsonObject) ;
    }

    /**
     * 解密 返回客户端
     * @param value
     * @return
     */
    private String decrypt(String key,String value){
        if("HisUserName".equals(key)
                || "HisUserPwd".equals(key)
                || "HisYdUserName".equals(key)
                || "HisYdUserPwd".equals(key)
                || "HisGatUserName".equals(key)
                || "HisGatUserPwd".equals(key)
                || "PhisUserName".equals(key)
                || "PhisUserPwd".equals(key)){

            value = Encr.decryptPassword(value);
            value = DesCipherUtil.encryptPasswordIfNotUsingVariablesInternal(value);
        }



        return value;
    }

    /**
     * 客户端密码加密后保存
     * @param value
     * @return
     */
    private String encrypt(String key,String value){
        if("HisUserName".equals(key)
                || "HisUserPwd".equals(key)
                || "HisYdUserName".equals(key)
                || "HisYdUserPwd".equals(key)
                || "HisGatUserName".equals(key)
                || "HisGatUserPwd".equals(key)
                || "PhisUserName".equals(key)
                || "PhisUserPwd".equals(key)){
            value = DesCipherUtil.decryptPasswordOptionallyEncryptedInternal(value);
            value = Encr.encryptPassword(value);
        }

        return value;
    }


    @ApiOperation(value = "系统配置", notes = "修改csv文件")
    @RequestMapping(value = "/updateCsvAll", method = RequestMethod.POST)
    public Response<JSONObject> updateCsvAll(
            @RequestBody JSONObject jsonObject) throws IOException {

        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+ File.separator+"config.csv";
        List<String[]> list = CsvUtils.read(filePath,true);

        for(String[] cls : list){
            if(cls.length>0 && !StringUtils.isEmpty(cls[0])){
                logger.info(cls[0]);
                String[] kv = cls[0].split("=");
                if(kv.length>1 && jsonObject.containsKey(kv[0])){
                    JSONObject object = jsonObject.getJSONObject(kv[0]);
                    logger.info(object.toJSONString());
                    cls[0] = object.getString("key")+"="+ encrypt(object.getString("key"),object.getString("value"));
                }
            }
        }

        CsvUtils.write(filePath,null,list);
        return Response.ok(jsonObject) ;
    }


    @ApiOperation(value = "系统Flag配置", notes = "获取csv文件")
    @RequestMapping(value = "/getFlag", method = RequestMethod.GET)
    public Response<List<String>> getFlag() throws IOException {

        List<String> res = new ArrayList<>();


        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+ File.separator+"config.csv";
        List<String[]> list = CsvUtils.read(filePath,true);
        for(String[] cls : list){
            String keyValue = cls[0];
            if(keyValue!=null && !keyValue.startsWith("#") && keyValue.indexOf("=") > 0){
                String[] kv = keyValue.split("=");
                if(kv.length ==2){
                    if("LoaclFlag".equalsIgnoreCase(kv[0]) && "Y".equalsIgnoreCase(kv[1]) ){
                        res.add("本地库");
                    }
                    if("YDFlag".equalsIgnoreCase(kv[0]) && "Y".equalsIgnoreCase(kv[1]) ){
                        res.add("异地库");
                    }
                    if("GATFlag".equalsIgnoreCase(kv[0]) && "Y".equalsIgnoreCase(kv[1]) ){
                        res.add("港澳台库");
                    }
                }
            }
        }

        return Response.ok(res) ;
    }



}
