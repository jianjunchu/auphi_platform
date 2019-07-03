package com.aofei.dataservice.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;
import java.util.*;

/**
 * @auther Tony
 * @create 2018-11-11 23:19
 */
public class RestServiceController {
//    /**
//     * POST请求传递的参数类型是JSON
//     * {"identify":"",//服务标识必须
//     * "userName":"", //用户名必须
//     * "password":"", //密码必须
//     * "systemName":"", //系统名称必须
//     * "params":[{"ftpUser":""},{"ftpPath":""}] //这里面的参数根据实际的接口的需求传递，参数名称要和接口定义的参数名称一致
//     * }
//     * @param body
//     * @param req
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "service.shtm", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
//    public String service(@RequestBody String body, HttpServletRequest req) {
//        System.out.println("====== 1.start to run services");
//        TaskStatus status = new TaskStatus();
//        String jsonString = "";
//        Map<String,String> parameters = new HashMap<String,String>();
//        //JSONObject.parse(body);
//        Dto inDto = JsonHelper.parseSingleJson2Dto(body);
//        String identify = inDto.getAsString("identify");
//        String userName = inDto.getAsString("userName");
//        String password = inDto.getAsString("password");
//        String systemName = inDto.getAsString("systemName");
//        String requestIP  =req.getRemoteAddr();
//
//        String parameterValueIdentify = null;
//        try{
//            Object obj = inDto.get("parameter");
//            net.sf.ezmorph.bean.MorphDynaBean bean = (net.sf.ezmorph.bean.MorphDynaBean)obj;
//            if(bean!=null)
//            {
//                for(int i=0;i<bean.getDynaClass().getDynaProperties().length;i++)
//                {
//                    String name = bean.getDynaClass().getDynaProperties()[i].getName();
//                    String value = bean.get(name).toString();
//                    parameters.put(name, value);
//                    System.out.println("name="+name+", value="+value);
//                    parameterValueIdentify +=value+",";
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//
//        //判断用户请求的参数是够为空，如果为空给客户端返回参数错误消息
//        if(!isNotEmpty(identify)){
//            status.setStatus(TaskStatus.Status.NULL);
//            status.setStatusCode(406);
//            status.setMessage("Parameter error,identify not null!");
//            jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//        if(!isNotEmpty(userName)){
//            status.setStatus(TaskStatus.Status.NULL);
//            status.setStatusCode(406);
//            status.setMessage("Parameter error,userName not null!");
//            jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//        if(!isNotEmpty(password)){
//            status.setStatus(TaskStatus.Status.NULL);
//            status.setStatusCode(406);
//            status.setMessage("Parameter error,password not null!");
//            jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//        if(!isNotEmpty(systemName)){
//            status.setStatus(TaskStatus.Status.NULL);
//            status.setStatusCode(406);
//            status.setMessage("Parameter error,systemName not null!");
//            jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//
//        //String key = userName + "@" + identify;
//        String key = RestServiceController.generateIdentifier(userName, identify, parameterValueIdentify);
//        String message = queryServiceStatus(key);
//        if(message != null){
//            return message;
//        } else {
//            //首先判断用户名密码是否正确
//            if(!authentication(userName,password,systemName)){
//                status.setStatus(TaskStatus.Status.Unauthorized);
//                status.setStatusCode(401);
//                status.setFtpPassword(password);
//                status.setFtpUserName(userName);
//                status.setMessage("ServiceUser name,password or systemName is error!");
//                jsonString = JsonHelper.encodeObject2Json(status);
//                return jsonString;
//            }
//            //根据服务标识获取服务对象
//            Service service = interfaceService.getServiceByIndetify(identify);
//            if(service == null){
//                status.setStatus(TaskStatus.Status.NotFound);
//                status.setStatusCode(404);
//                status.setFtpUserName(userName);
//                status.setMessage("Service not exist!");
//                jsonString = JsonHelper.encodeObject2Json(status);
//                return jsonString;
//            }
//            //判断用户是否有服务权限
//            if(!userAuthority(userName,identify,requestIP)){
//                status.setStatus(TaskStatus.Status.MethodNotAllowed);
//                status.setStatusCode(405);
//                status.setFtpUserName(userName);
//                status.setMessage("User is not authority!");
//                jsonString = JsonHelper.encodeObject2Json(status);
//                return jsonString;
//            }
//            //判断数据返回的方式，是ftp还是webservice，如果是ftp，则返回客户端数据生成的状态及FTP路径等信息，
//            //如果Webservice直接将结果写入到Webservice消息体中
//            if(service.getReturnType().equals("1")){
//                String result="";
//                try {
//                    result =  ftpReturnData(inDto,service,parameters,parameterValueIdentify);
//                    return result;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    status.setStatus(TaskStatus.Status.ParameterNotFound);
//                    status.setStatusCode(407);
//                    status.setMessage("Parameter is not found!"+e.getMessage());
//                    jsonString = JsonHelper.encodeObject2Json(status);
//                    return jsonString;
//                }
//            } else {//如果是Webservice直接返回结果数据
//                String result="";
//                try {
//                    result =  webserviceReturnData(inDto, service,parameters);
//                    return result;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    status.setStatus(TaskStatus.Status.ParameterNotFound);
//                    status.setStatusCode(407);
//                    status.setMessage("Parameter is not found!"+e.getMessage());
//                    jsonString = JsonHelper.encodeObject2Json(status);
//                    return jsonString;
//                }
//            }
//        }
//    }
//
//    private String getSQL(Service service, Map<String,String> parameters) throws Exception
//    {
//        String tableNameWithVariable = service.getTableName();
//        String tableName = replaceVariable(tableNameWithVariable,parameters);
//
//        String conditionsWithVariable = service.getConditions();
//        String conditions = replaceVariable(conditionsWithVariable,parameters);
//
//        String fields = service.getFields();
//        String SQL = "SELECT "+ fields + " FROM "+tableName +" where "+conditions;
//        return SQL;
//    }
//
//    /**
//     * execute job and save to monitor table.
//     * @param inDto
//     * @param service
//     * @param parameters
//     * @return
//     * @throws Exception
//     */
//    private String webserviceReturnData(Dto inDto, Service service, Map<String,String> parameters) throws Exception{
//        String identify = inDto.getAsString("identify");
//        String userName = inDto.getAsString("userName");
//        String password = inDto.getAsString("password");
//        String systemName = inDto.getAsString("systemName");
//        //保存服务接口监控信息表
//        Dto dto = new BaseDto();
//        dto.put("serviceId",service.getServiceId());
//        dto.put("startTime",format.format(new Date()));
//
//        ResultData data = new  ResultData();
//        WebserviceTaskRun task = new WebserviceTaskRun();
//        String SQL = this.getSQL(service, parameters);
//        int databaseId = service.getIdDatabase();
//        Map<String,Object> records = null;
//        records = task.runTransTask(databaseId,SQL, null, null,"admin","admin");
//        data.setStatuscode(200);
//        data.setMsg("OK");
//        List results = (List)records.get("result");
//        data.setRecordCount(results.size());
//        data.setRecords(results);
//        data.setTableFields(records.get("field").toString());
//
//        dto.put("status", "running");
//        dto.put("userName",userName);
//        dto.put("systemName",systemName);
//        dto.put("MONITOR_ID",null);
//        dto.put("endTime",format.format(new Date()));
//        dto.put("status", "completed");
//        dto.put("monitorId",dto.get("MONITOR_ID"));
//        this.interfaceService.saveServiceMonitor(dto);
//        return JsonHelper.encodeObject2Json(data);
//    }
//
//    /**
//     * replace all named paramters in a string.
//     * paramers starts with "${", end with "}",
//     * @param string
//     * @param parameters
//     * @return
//     * @throws Exception
//     */
//    private String replaceVariable(String string, Map<String,String> parameterMap) throws Exception{
//        StringBuffer resultBuffer = new StringBuffer();
//        StringBuffer parameterBuffer = new StringBuffer();
//        int flag=0;
//        for (int i=0;i<string.length();i++)
//        {
//            char c = string.charAt(i);
//            if(c=='$')
//            {
//                if(flag==0)
//                    flag=1;
//                else if(flag==1)
//                {
//                    resultBuffer.append('$');
//                    flag=1;
//                }else if(flag==2)
//                {
//                    parameterBuffer.append('$');
//                }
//            }
//            else if(c=='{')
//            {
//                if(flag==0)
//                    resultBuffer.append('{');
//                else if(flag==1)
//                {
//                    flag=2;
//                }else if(flag==2)
//                {
//                    parameterBuffer.append('{');
//                }
//            }
//            else if(c=='}')
//            {
//                if(flag==0)
//                    resultBuffer.append('}');
//                else if(flag==1)
//                {
//                    resultBuffer.append('$');
//                    resultBuffer.append('}');
//                    flag=0;
//                }
//                else if(flag==2)
//                {
//                    String parameterStr = parameterBuffer.toString();
//                    String value = parameterMap.get(parameterStr);
//                    if(value == null)
//                    {
//                        String keys = parameterMap.entrySet().toString();
//                        throw new Exception("parameter: "+ parameterStr+" is needed, but not found in the provided parameters：" +keys);
//                    }
//                    resultBuffer.append(value);
//                    flag=0;
//                    parameterBuffer.delete(0, parameterBuffer.length());
//                }
//
//            }else
//            {
//                if(flag==0)
//                    resultBuffer.append(c);
//                else if(flag==1)
//                {
//                    resultBuffer.append('$');
//                    resultBuffer.append(c);
//                    flag=0;
//                }
//                else if (flag==2)
//                {
//                    parameterBuffer.append(c);
//                }
//            }
//        }
//        return resultBuffer.toString();
//    }
//
//    /**
//     * 使用FTP方式返回数据，
//     * @param inDto
//     * @param parameters
//     * @return
//     * @throws Exception
//     */
//    private String ftpReturnData(Dto inDto,Service service, Map parameters,String parameterValueIdentify) throws Exception{
//        String identify = inDto.getAsString("identify");
//        String userName = inDto.getAsString("userName");
//        String password = inDto.getAsString("password");
//        String systemName = inDto.getAsString("systemName");
//        //String key = userName + "@" + identify;
//        String key = generateIdentifier(userName,identify,parameterValueIdentify);
//        TaskStatus status = new TaskStatus();
//        //生成ftp路径，已提供kettle将数据写入到指定目录
//        PropertiesHelper helper = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
//        String rootPath = helper.getValue("ftp.root.path");// 获取属性值
//        //生成ftp目录上数据存放的目录
//        String path = UUID.randomUUID().toString();
//        String ftpIp = helper.getValue("ftp.ip");
//        String ftpPath = rootPath + "/" +path;
//        //获取FTPClient对象
//        FTPClient ftpClient = getFtpClient();
//        try {
//            //创建FTP目录
//            ftpClient.makeDirectory(ftpPath);
//        } catch (IOException e) {
//            status.setStatus(TaskStatus.Status.Error);
//            status.setStatusCode(500);
//            status.setMessage("FTP Server has not started!");
//            e.printStackTrace();
//            String jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//        String ftpPassword = helper.getValue("ftp.password");
//        String ftpUserName = helper.getValue("ftp.username");
//        //给客户端返回消息
//        String returnFtpPath = "ftp://" + ftpIp + ftpPath+"/"+"data.zip";
//        status.setStatus(TaskStatus.Status.Running);
//        status.setStatusCode(201);
//        status.setMessage("runing!");
//        status.setFtpPath(returnFtpPath);
//        status.setFtpPassword(ftpPassword);
//        status.setFtpUserName(ftpUserName);
//        status.setZipPassword(password);
//        String SQL = this.getSQL(service, parameters);
//        Thread thread = new Thread(new RestTaskExector(service,userName,password,ftpIp,ftpPath,ftpUserName,ftpPassword,systemName,SQL,parameterValueIdentify));
//        thread.start();
//        RestTaskStatusManager.getInstance().setTaskStatus(key, status);
//        String jsonString = JsonHelper.encodeObject2Json(status);
//        return jsonString;
//    }
//
//
//    /**
//     * 查询用户请求的状态
//     * @param key
//     * @return
//     */
//    private String queryServiceStatus(String key) {
//        TaskStatus status = RestTaskStatusManager.getInstance().getTaskStatus(key);
//        if(status != null){
//            String jsonString = JsonHelper.encodeObject2Json(status);
//            return jsonString;
//        }
//        return null;
//    }
//
//
//    public FTPClient getFtpClient(){
//        FTPClient ftpClient = new FTPClient();
//        PropertiesHelper helper = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
//        String ip = helper.getValue("ftp.ip");// 获取属性值
//        String userName = helper.getValue("ftp.username");
//        String password = helper.getValue("ftp.password");
//        try {
//            ftpClient.connect(ip);
//            ftpClient.login(userName, password);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ftpClient;
//    }
//
//    /**
//     * 验证调用接口的用户名、密码、系统名称是否正确
//     * @param userName
//     * @param password
//     * @param systemName
//     * @return
//     */
//    private boolean authentication(String userName,String password,String systemName){
//        Dto dto = new BaseDto();
//        dto.put("userName",userName);
//        dto.put("password",password);
//        dto.put("systemName",systemName);
//        Dto dtoTemp = interfaceServiceUser.queryServiceUser(dto);
//        if(dtoTemp!=null&&!dtoTemp.equals("")){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /**
//     * 验证接口凭证是否存在
//     * @param identify
//     * @return
//     */
//    private boolean authIdentify(String identify){
//        Service service = interfaceService.getServiceByIndetify(identify);
//        if(service != null ){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /**
//     * 验证调用接口的用户是否具有操作接口的权限
//     * @param userName
//     * @param password
//     * @param systemName
//     * @return
//     */
//    private boolean userAuthority(String userName,String identify,String IP){
//        Dto dto = new BaseDto();
//        dto.put("userName",userName);
//        dto.put("identify",identify);
//        dto.put("IP","%"+IP+"%");
//        Dto dtoTemp = interfaceServiceAuth.getServiceAuth(dto);
//        if(dtoTemp!=null&&!dtoTemp.equals("")){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    private boolean isNotEmpty(String str){
//        if(str!=null&&!str.equals("")){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    public static void main(String[] args)
//    {
//        String parameter = "{\"parameter1\": \"parameter1\",\"parameter2\": \"parameter2\",\"parameter3\": \"parameter3\"}";
//        JSONObject jsonObject = (JSONObject)JSONObject.parse(parameter);
//        Set entry = jsonObject.entrySet();
//        Iterator it = entry.iterator();
//        Map parameters = new HashMap();
//        while (it.hasNext())
//        {
//            String string = it.next().toString();
//            String key = string.substring(0,string.indexOf("="));
//            String value = string.substring(string.indexOf("=")+1,string.length());
//            parameters.put(key,value);
//        }
//        Map map = new HashMap();
//        map.put("var1", "abc");
//        map.put("var2", "de");
//        map.put("var3", "50");
//        map.put("var4", "150");
//
//        String test1="test${var1}_${var2}";
//        String test2="a<${var1} and a>${var2}";
//        String test3="a=${var1} or a=${var4}";
//        String test4="test$${var1}_${var2}";
//        String test5="$test${var1}_${var2}";
//        String test6="$test${var6}_${var2}";
//
//        RestServiceController control = new RestServiceController ();
//        try {
//            String a = control.replaceVariable(test1,map);
//            String b = control.replaceVariable(test2,map);
//            String c = control.replaceVariable(test3,map);
//            String d = control.replaceVariable(test4,map);
//            String e = control.replaceVariable(test5,map);
//            String f = control.replaceVariable(test6,map);
//
//            System.out.println(a);
//            System.out.println(b);
//            System.out.println(c);
//            System.out.println(d);
//            System.out.println(e);
//            System.out.println(f);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    public static String generateIdentifier(String userName,
//                                            String serviceIdentify, String parameterValueIdentify) {
//        return userName + "@"+ serviceIdentify+":"+parameterValueIdentify;
//    }
}
