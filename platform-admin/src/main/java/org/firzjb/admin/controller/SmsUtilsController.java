package org.firzjb.admin.controller;

import org.firzjb.admin.model.request.SmsCaptchaRequest;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.model.response.Response;
import org.firzjb.sys.entity.User;
import org.firzjb.sys.exception.SystemError;
import org.firzjb.sys.model.response.SmsCountryResponse;
import org.firzjb.sys.service.ISmsCountryService;
import org.firzjb.sys.service.IUserService;
import org.firzjb.utils.TencentSmsSingleSender;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统管理
 * 短信接口
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 15:45
 */
@Api(tags = { "系统管理-短信接口" })
@RestController
@RequestMapping(value = "/sms", produces = {"application/json;charset=UTF-8"})
public class SmsUtilsController extends BaseController {


    @Autowired
    private IUserService userService;

    @Autowired
    private ISmsCountryService smsCountryService;


    @Autowired
    private TencentSmsSingleSender tencentSmsSingleSender;

    /**
     * 国家代码列表
     * @return
     *  countryCode:国家代码
     *  countryName: 国家名称
     */
    @ApiOperation(value = "", notes = "国家代码列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/country/listAll", method = RequestMethod.GET)
    public Response<List<SmsCountryResponse>> list()  {
        List<SmsCountryResponse> list = smsCountryService.getSmsCountrys();
        return Response.ok(list) ;
    }


    /**
     * 获取注册手机注册验证码
     * @param request
     *      mobilephone:手机号
     *      countryCode: 国家代码
     * @return
     *     0:发送短信成功
     * @throws Exception
     */
    @ApiOperation(value = "", notes = "获取手机注册验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 200013, message = "the phone number is exist"),
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/register/getSmsCaptcha", method = RequestMethod.POST)
    public Response<Integer> getRegisterSmsCaptcha(@RequestBody SmsCaptchaRequest request) throws Exception {

        int count = userService.selectCount(new EntityWrapper<User>()
                .eq("C_MOBILEPHONE",request.getMobilephone())
                .eq("C_COUNTRY_CODE",request.getCountryCode())
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(count>0){
            throw new ApplicationException(SystemError.PHONE_NUMBER_EXIST.getCode(),"the phone number is exist");
        }
        tencentSmsSingleSender.sendSms(request.getCountryCode(),request.getMobilephone());
        return Response.ok(0);
    }

    /**
     * 获取登录手机注册验证码
     * @param request
     *      mobilephone:手机号
     *      countryCode: 国家代码
     * @return
     *     0:发送短信成功
     * @throws Exception
     */
    @ApiOperation(value = "获取登录手机注册验证码", notes = "获取手机注册验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "the phone number is not exist"),
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/login/getSmsCaptcha", method = RequestMethod.POST)
    public Response<Integer> getLoginSmsCaptcha(@RequestBody SmsCaptchaRequest request) throws Exception {

        int count = userService.selectCount(new EntityWrapper<User>()
                .eq("C_MOBILEPHONE",request.getMobilephone())
                .eq("C_COUNTRY_CODE",request.getCountryCode())
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(count == 0){
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(),"the phone number is not exist");
        }
        tencentSmsSingleSender.sendSms(request.getCountryCode(),request.getMobilephone());
        return Response.ok(0);
    }



}
