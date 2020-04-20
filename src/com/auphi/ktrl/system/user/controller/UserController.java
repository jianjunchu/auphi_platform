package com.auphi.ktrl.system.user.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auphi.data.hub.core.BaseMultiActionController;

import com.auphi.ktrl.system.user.bean.UserBean;
import com.auphi.ktrl.system.user.util.UserUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = {"application/json;charset=UTF-8"})
public class UserController extends BaseMultiActionController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")})
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<UserBean> users =  UserUtil.getUsers();
            JSONArray jsonArray = new JSONArray();
            for(UserBean userBean : users){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId",userBean.getUser_id());
                jsonObject.put("nickName",userBean.getNick_name());
                jsonObject.put("email",userBean.getEmail());
                jsonObject.put("username",userBean.getUser_name());
                jsonArray.add(jsonObject);
            }
            this.setOkTipMsg("success",jsonArray,response);
        }catch (Exception e){
            this.setFailTipMsg(e.getMessage(),response);
        }

    }
}
