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
package com.auphi.data.hub.controller;


import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.core.util.CloudConstants;
import com.auphi.data.hub.core.util.CloudUtils;
import com.auphi.data.hub.core.util.JsonHelper;
import com.auphi.data.hub.domain.UserInfo;
import com.auphi.data.hub.service.OrganizationService;
import com.auphi.data.hub.service.ResourceService;
import com.auphi.data.hub.service.UserService;
import com.auphi.ktrl.system.user.bean.LoginResponse;
import com.auphi.ktrl.system.user.bean.UserBean;
import com.auphi.ktrl.system.user.util.UserUtil;
import com.auphi.ktrl.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApiIgnore
@Controller("index")
public class IndexController extends BaseMultiActionController {

	private final static String INDEX = "index";
	
	private final static String WELCOME = "admin/welcome";
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private UserService userService;
	
	
	
	public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Dto dto = new BaseDto();
		dto.put("sysTitle", getParamValue("SYS_TITLE", req));
		dto.put("westTitle", getParamValue("WEST_NAVIGATE_TITLE", req));
		return new ModelAndView(INDEX,dto);
	}
	
	/**
	 * 欢迎页面
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView welcome(HttpServletRequest req, HttpServletResponse resp){
		return new ModelAndView(WELCOME);
	}
	
	/**
	 * 解锁系统
	 * 
	 * @param
	 * @return
	 */
	public ModelAndView unlockSystem(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		UserInfo userInfo = getSessionContainer(req).getUserInfo();
		String inputPw = req.getParameter("password");
		String password = CloudUtils.encryptBasedMd5(inputPw);
		Dto outDto = new BaseDto(CloudConstants.TRUE);
		if (password.equals(userInfo.getPassword())) {
			outDto.put("flag", CloudConstants.SUCCESS);
		}else {
			outDto.put("flag", CloudConstants.FAILURE);
		}
		write(outDto.toJson(), resp);
		return null;
	}	
	
	
	
	/**
	 * 加载当前登录用户信息
	 * 
	 * @param
	 * @return
	 */
	public ModelAndView loadUserInfo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		UserInfo userInfoVo = getSessionContainer(req).getUserInfo();
		Dto inDto = new BaseDto();
		CloudUtils.copyPropFromBean2Dto(userInfoVo, inDto);
		Dto outDto = (BaseDto)userService.saveUserItem(userInfoVo);
		outDto.remove("password");
		String jsonString = JsonHelper.encodeDto2FormLoadJson(outDto, null);
		write(jsonString, resp);
		return null;
	}
	
	/**
	 * 修改当前登录用户信息
	 * 
	 * @param
	 * @return
	 */
	public ModelAndView updateUserInfo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = super.getSessionContainer(req).getUserInfo().getUserid();
        String oldPassword = req.getParameter("password2");
        String newPassword = req.getParameter("password");
        UserInfo userInfo = userService.getUserByUserIdAndPassword(userId, oldPassword);
        try {
            if (null == userInfo) {
                setOkTipMsg("原密码错误", resp);
            } else {
                userInfo.setPassword(newPassword);
                userService.updateUserItem(userInfo);
                setOkTipMsg("修改密码成功", resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setFailTipMsg("修改密码失败", resp);
        }
        return null;
	}
	
	/**
	 * 保存样式
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView saveUserTheme(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Dto dto = new BaseDto();
		String theme = req.getParameter("theme");
		dto.put("userid", super.getSessionContainer(req).getUserInfo().getUserid());
		dto.put("theme", theme);
		Dto outDto = organizationService.saveUserTheme(dto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, resp);
		return null;
	}

	/**
	 * 保存样式
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String username = ServletRequestUtils.getStringParameter(req,"username",null);
		String passwd = ServletRequestUtils.getStringParameter(req,"passwd",null);
		if(StringUtils.isEmpty(username)){
			this.setFailTipMsg("用户名不能为空", resp);
		}
		if(StringUtils.isEmpty(passwd)){
			this.setFailTipMsg("密码不能为空", resp);
		}

		LoginResponse lrp = new LoginResponse() ;
		UserUtil.login(username,passwd,lrp) ;
		req.getSession().setAttribute("user_id", lrp.getUser_id());
		UserBean userBean = UserUtil.getUserById(lrp.getUser_id());
		req.getSession().setAttribute("userBean", userBean);
		req.getSession().setMaxInactiveInterval(3600*2);

		this.setOkTipMsg("SUCCESS",req.getSession().getId(), resp);

		return null;
	}
}
