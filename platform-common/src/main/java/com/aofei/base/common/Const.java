package com.aofei.base.common;

import com.aofei.utils.PropertiesLoader;
import com.aofei.utils.StringUtils;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

/**
 * 常量类
 * Created by Hao on 2017-03-24.
 */
public class Const {


    public static final String GENERAL_SCHEDULE_KEY = "general_schedule_key";
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("config/application.properties","config/db.properties");

    /** session中存放的用户key*/
    public final static String SESSION_USER = "user";

    public final static String TOKEN_KEY = "token_key";

    public final static String REPOSITORY_USERNAME = "admin";

    public final static String REPOSITORY = "repository";

    public final static String REPOSITORY_PASSWORD = "admin";


    public static final  int YES = 1;

    public static final  int NO = 0;




    /**
     * 获取配置
     * @see ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    public static String getRootPath(Long organizerId){
        return "/"+ (organizerId==null ? "" : organizerId.toString());
    }

    public static String getUserPath(Long organizerId,String path){

        if(StringUtils.isEmpty(path)){
            return getRootPath(organizerId);
        }

        if(path.startsWith(organizerId.toString()) || path.startsWith(getRootPath(organizerId))){
            return  (path.startsWith("/") ? "" : "/")  + path;
        }
        return getRootPath(organizerId) +(path.startsWith("/") ? "" : "/")  + path;
    }


    public static String getUserDir(Long organizerId){
        String base = loader.getProperty("disk.root.dir");

        base = StringUtils.isEmpty(base) ? System.getProperty("etl_platform.root") : base;

        String path = base + File.separator +"disk"+ File.separator +organizerId;

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
       return  file.getAbsolutePath();

    }


    public static String getUserFilePath(Long organizerId, String path) {
        if(path!=null && organizerId!=null){
            String root = getUserDir(organizerId);
            return path.replace(root,"");
        }
        return "";
    }




}
