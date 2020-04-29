package com.auphi.ktrl.system.repository.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.ktrl.engine.KettleEngine;
import com.auphi.ktrl.engine.impl.KettleEngineImpl2_3;
import com.auphi.ktrl.engine.impl.KettleEngineImpl4_3;
import com.auphi.ktrl.system.repository.bean.RepositoryBean;
import com.auphi.ktrl.system.repository.util.RepositoryUtil;
import com.auphi.ktrl.system.user.bean.UserBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/reps", produces = {"application/json;charset=UTF-8"})
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

    /**
     * 获取资源库列表
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/getRepTree" , method = RequestMethod.GET)
    public void getRepTree(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String repository = ServletRequestUtils.getStringParameter(request,"repository","");
        String version = ServletRequestUtils.getStringParameter(request,"version","") ;
        UserBean userBean = request.getSession().getAttribute("userBean")==null?null:(UserBean)request.getSession().getAttribute("userBean");
        String user_id = request.getSession().getAttribute("user_id")==null?"":request.getSession().getAttribute("user_id").toString();
        try {

            KettleEngine kettleEngine = null;

            if(KettleEngine.VERSION_2_3.equals(version)){
                kettleEngine = new KettleEngineImpl2_3();
            }else if(KettleEngine.VERSION_4_3.equals(version)){
                kettleEngine = new KettleEngineImpl4_3();
            }

            String repTreeJSON = kettleEngine.getRepTreeJSON(repository, user_id);

            JSONArray jsonArray = JSONArray.parseArray(repTreeJSON);

            this.setOkTipMsg("success",jsonArray,response);
        }catch (Exception e){
            this.setFailTipMsg(e.getMessage(),response);
        }
    }
}
