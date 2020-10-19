package com.auphi.ktrl.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    /**利用MD5进行加密*/
    public static String encoderByMd5(String str) {
        try {
            //确定计算方法
            MessageDigest md5=MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));

            return newstr;
        }catch (Exception e){
            return str;
        }




    }

    /**判断用户密码是否正确
     *newpasswd 用户输入的密码
     *oldpasswd 正确密码*/
    public static boolean checkpassword(String newpasswd,String oldpasswd) {


        if(encoderByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        System.out.println(encoderByMd5("admin"));
        System.out.println(checkpassword("admin","ISMvKXpXpadDiUoOSoAfww=="));
        System.out.println(System.currentTimeMillis()+(3600*24*30*1000L));
    }
}
