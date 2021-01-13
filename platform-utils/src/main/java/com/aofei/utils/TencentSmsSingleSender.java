package com.aofei.utils;


import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯短信发送接口
 */
@Component
public class TencentSmsSingleSender {

    private static Logger logger = LoggerFactory.getLogger(TencentSmsSingleSender.class);

    private static Map<String,VerificationCode> captchas = new HashMap<>();

    /**
     * 短信应用SDK AppID
     */
    @Value("#{propertiesReader['qcloudsms.appid']}")
    private int appid ;

    @Value("#{propertiesReader['qcloudsms.appkey']}")
    private String appkey  ; // 短信应用SDK AppKey

    @Value("#{propertiesReader['qcloudsms.captchaTemplId.zh']}")
    private int zhTemplateId  ; // 短信模板ID，需要在短信应用中申请

    @Value("#{propertiesReader['qcloudsms.smsSign.zh']}")
    private String zhSmsSign ; // 签名

    @Value("#{propertiesReader['qcloudsms.captchaTemplId.en']}")
    private int enTemplateId  ; // 短信模板ID，需要在短信应用中申请

    @Value("#{propertiesReader['qcloudsms.smsSign.en']}")
    private String enSmsSign ; // 签名

    private long valid = 1000*60*30;//验证码有效期


    /**
     * 发送短信
     * @param countryCode
     * @param phoneNumber
     */
    public void sendSms(String countryCode, String phoneNumber) {
        try {
            String[] params = {createCaptcha(countryCode,phoneNumber),"30"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);

            //判断是否是国内短信还是国际短信(国内短信和国际短信签名参数不一致)
            if("86".equalsIgnoreCase(countryCode)){
                //国内短信
                SmsSingleSenderResult result = ssender.sendWithParam(countryCode, phoneNumber,
                        zhTemplateId, params, zhSmsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            }else{
                //国际短信
                SmsSingleSenderResult result = ssender.sendWithParam(countryCode, phoneNumber,
                        enTemplateId, params, enSmsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            }

        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }

    /**
     * 创建一个短信验证码,并且保存到内存中
     * @param countryCode
     * @param phoneNumber
     * @return
     */
    private String createCaptcha(String countryCode,String phoneNumber){
        int a = (int)((Math.random()*9+1)*100000) ;//6为随机数

        phoneNumber = countryCode+phoneNumber;
        String captcha = String.valueOf(a);
        logger.info("captcha=>"+captcha);
        VerificationCode verificationCode = new VerificationCode(captcha,phoneNumber,System.currentTimeMillis());
        captchas.put(phoneNumber,verificationCode);

        return captcha;

    }

    /**
     * 验证短信验证码
     * @param phoneNumber
     * @param captcha
     * @return
     */
    public boolean validate(String countryCode,String phoneNumber,String captcha){
        phoneNumber = countryCode+phoneNumber;
        VerificationCode verificationCode = captchas.get(phoneNumber);
        long now  = System.currentTimeMillis();

        if(verificationCode!=null && now - verificationCode.getCreateTime() < valid && verificationCode.getCode().equalsIgnoreCase(captcha) ){
            return true;
        }else{
            return false;
        }
    }




    class VerificationCode{

       private String code;

       private String phoneNumber;

       private long createTime;

        VerificationCode(String code,String phoneNumber,long createTime){
            setCode(code);
            setCreateTime(createTime);
            setPhoneNumber(phoneNumber);
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getCode() {
            return code;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
