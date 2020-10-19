/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2017 by Auphi BI : http://www.doetl.com

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package com.auphi.ktrl.system;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.auphi.ktrl.system.user.bean.LoginResponse;
import com.auphi.ktrl.system.user.bean.UserBean;
import com.auphi.ktrl.system.user.util.UMStatus;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.Constants;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static String parameter_action = "action" ;
    public static String parameter_user_id = "user_id" ;
    public static String parameter_user_name = "user_name" ;
    public static String parameter_password = "password" ;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String action = request.getParameter(parameter_action) ;
		if ("login".equals(action))
        {
            String user_name = request.getParameter(parameter_user_name) ;
            String password = request.getParameter(parameter_password) ;
            LoginResponse lrp = new LoginResponse() ;
            UserUtil.login(user_name,password,lrp) ;

            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(lrp.toJSONString());
            response.getWriter().close();
        }
		else if ("platformLogin".equals(action))
        {
        	String user_name = request.getParameter(parameter_user_name) ;
            String password = request.getParameter(parameter_password) ;

          	LoginResponse lrp = new LoginResponse() ;
           	UserUtil.login(user_name,password,lrp) ;
           	request.getSession().setAttribute("user_id", lrp.getUser_id());
           	UserBean userBean = UserUtil.getUserById(lrp.getUser_id());
           	request.getSession().setAttribute("userBean", userBean);
           	int sessionTimeOut = Integer.parseInt(Constants.get("SessionTimeOut"));
           	request.getSession().setMaxInactiveInterval(sessionTimeOut);

            response.sendRedirect("index.jsp");
        }
		else if("checkLogin".equals(action))
        {
            JSONObject jsonObject = new JSONObject();

        	String user_name = request.getParameter(parameter_user_name) ;
            String password = request.getParameter(parameter_password) ;
            UserBean userBean = new UserBean() ;
           	UMStatus umStatus = UserUtil.login(user_name, password, userBean) ;
            jsonObject.put("userId",userBean.getUser_id());
           	if(umStatus.equals(UMStatus.USER_NOT_EXIST)  || umStatus.equals(UMStatus.PASSWORD_EXPIRY) || umStatus.equals(UMStatus.SUCCESS)){

                jsonObject.put("statusCode",umStatus.getStatusCode());
                jsonObject.put("statusMessage",umStatus.getStatusMessage());

            }else if(umStatus.equals(UMStatus.WRONG_PASSWORD)){
                Object count_s = request.getSession().getAttribute("WRONG_PASSWORD_COUNT");
                int count = 1;
                if(count_s == null){
                    request.getSession().setAttribute("WRONG_PASSWORD_COUNT", count);
                }else{
                    count = Integer.parseInt(count_s.toString())+1;
                    request.getSession().setAttribute("WRONG_PASSWORD_COUNT", count);
                }
                if(count > 4){
                    UserUtil.lockUser(userBean);
                }

                jsonObject.put("statusCode",umStatus.getStatusCode());
                jsonObject.put("statusMessage","密码错误,连续错误5次账户将锁定一小时,您还可以尝试"+(5-count)+"次");
            }else if (umStatus.equals(UMStatus.LOCK_LOGIN)){
                jsonObject.put("statusCode",umStatus.getStatusCode());
                long t = userBean.getLoginLockTime() - System.currentTimeMillis();
                long f = t/1000/60;
                jsonObject.put("statusMessage","密码错误次数过多,请"+f+"分钟后再次尝试!");
            }


           	response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(jsonObject.toJSONString());
            response.getWriter().close();
        }
        else if("logOut".equals(action)){
        	request.getSession().setAttribute("user_id", "");
        	request.getSession().setAttribute("userBean", null);
        	String errMsg = request.getParameter("errMsg")==null?"":new String(request.getParameter("errMsg").getBytes("ISO8859-1"), "UTF-8");
        	request.setAttribute("errMsg", errMsg);
        	RequestDispatcher dispatcher = request.getRequestDispatcher("");
    		dispatcher.forward(request, response);
        }else if("changePassword".equals(action)){
            String userId = request.getParameter(parameter_user_id) ;
            String password = request.getParameter(parameter_password) ;

            UserBean userBean =  UserUtil.getUserById(userId);

            JSONObject res =  UserUtil.changePassword(userBean,password) ;


            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(res.toJSONString());
            response.getWriter().close();

        }
	}

}
