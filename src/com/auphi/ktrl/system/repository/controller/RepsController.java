package com.auphi.ktrl.system.repository.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.ktrl.system.repository.bean.RepositoryBean;
import com.auphi.ktrl.system.repository.util.RepositoryUtil;
import com.auphi.ktrl.system.user.bean.UserBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = {"application/json;charset=UTF-8"})
public class RepsController extends BaseMultiActionController {

    /**
     * 获取资源库列表
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/getReps" , method = RequestMethod.GET)
    public void getReps(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String version = request.getParameter("version")==null?"":request.getParameter("version");
        UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");

        try {
            List<RepositoryBean> listReps = RepositoryUtil.getRepByVersionAndOrg(version, userBean.getOrgId());
            JSONArray jsonArray = new JSONArray();
            for(RepositoryBean repositoryBean : listReps){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("repositoryID",repositoryBean.getRepositoryID());
                jsonObject.put("repositoryName",repositoryBean.getRepositoryName());
                jsonArray.add(jsonObject);
            }
            this.setOkTipMsg("success",jsonArray,response);
        }catch (Exception e){
            this.setFailTipMsg(e.getMessage(),response);
        }


    }
}
