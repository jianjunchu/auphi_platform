package com.aofei.sys.controller;

/**
 * @auther Tony
 * @create 2018-09-13 13:38
 */

import com.aofei.base.annotation.Authorization;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.exception.StatusCode;
import com.aofei.base.model.request.PageRequest;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.log.annotation.Log;
import com.aofei.sys.exception.SystemError;
import com.aofei.utils.MD5Utils;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @auther Tony
 * @create 2018-09-12 20:07
 */
@Log4j
@Api(tags = { "系统管理-用户管理模块接口" })
@RestController
@Authorization
@RequestMapping(value = "/sys/user", produces = {"application/json;charset=UTF-8"})
public class UserController extends BaseController {

    @Autowired
    IUserService userService;

    @Autowired
    IUserRoleService userRoleService;


    /**
     * 用户列表(分页查询)
     * @return
     */
    @ApiOperation(value = "当前登录用户信息", notes = "当前登录用户信息", httpMethod = "GET")
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public Response<UserResponse> my(
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        return Response.ok(userService.get(user.getUserId()));
    }

    /**
     * 用户列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "用户列表(分页查询)", notes = "用户列表(分页查询)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Response<DataGrid<UserResponse>> page(@ApiIgnore UserRequest request)  {
        Page<UserResponse> page = userService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 新建用户
     * @param request
     * @return
     */
    @Log(module = "用户管理",description = "新建用户")
    @ApiOperation(value = "新建用户", notes = "新建用户", httpMethod = "POST")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<UserResponse> add(
            @RequestBody UserRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user)  {

        int usernameCount = userService.selectCount(new EntityWrapper<User>()
                .eq("C_USER_NAME",request.getMobilephone())
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(usernameCount>0){
            throw new ApplicationException(SystemError.USERNAME_EXIST.getCode(),"用户名已存在");
        }
        int mobilephoneCount = userService.selectCount(new EntityWrapper<User>()
                .eq("C_MOBILEPHONE",request.getMobilephone())
                .eq("C_COUNTRY_CODE","86")
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(mobilephoneCount>0){
            throw new ApplicationException(SystemError.PHONE_NUMBER_EXIST.getCode(),"手机号已存在");
        }
        request.setOrganizerId(user.getOrganizerId());
        userService.save(request);

        return Response.ok() ;
    }

    /**
     * 编辑用户
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "编辑用户", httpMethod = "POST")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<UserResponse> edit(
            @RequestBody UserRequest request)  {

        userService.update(request);

        return Response.ok() ;
    }

    /**
     * 编辑用户
     * @param request
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码", httpMethod = "POST")
    @RequestMapping(value = "/edit_password", method = RequestMethod.POST)
    public Response<UserResponse> edit_password(
            @RequestBody EditUserPasswordRequest request)  {


        if(StringUtils.isEmpty(request.getNewPassword())){
            throw new ApplicationException(StatusCode.BAD_REQUEST.getCode(),"密码不能为空");
        }

        if(!request.getNewPassword().equals(request.getRenewPassword())){
            throw new ApplicationException(StatusCode.BAD_REQUEST.getCode(),"两次输入密码不一致");
        }

        User existing = userService.selectById(request.getUserId());
        if(existing!=null){
            existing.setPassword(MD5Utils.getStringMD5(request.getNewPassword()));//密码进行MD5加密
            existing.preUpdate();
            userService.updateById(existing);
        }else{
            //用户不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), "更新失败.用户不存在");
        }

        return Response.ok() ;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "删除用户", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {

        return Response.ok(userService.del(id)) ;
    }

    /**
     * 根据Id查询用户
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户", httpMethod = "GET")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<UserResponse> get(
            @PathVariable Long id)  {

        return Response.ok(userService.get(id)) ;
    }

    /**
     * 修改用户拥有的角色
     *
     * @return
     */
    @ApiOperation(value = "修改用户拥有的角色", notes = "修改用户拥有的角色", httpMethod = "POST")
    @RequestMapping(value = "{id}/role/modify", method = RequestMethod.POST)
    public Response<Integer> changeUserRole(
            @ApiParam(value = "用户Id", required = true) @PathVariable("id") Long userId,
            @ApiParam(value = "角色ID数组json对象", required = true)  @RequestBody List<Long> roles) {
        return Response.ok(userRoleService.changeUserRole(userId, roles)) ;
    }

    /**
     * 查询角色下的用户列表
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询角色下的用户列表", notes = "查询角色下的用户列表", httpMethod = "GET")
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    public Response<DataGrid<UserResponse>> getUserByRoleCode(@PathVariable("roleId") Long roleId, PageRequest pageRequest) {
        Page<UserResponse> page = userService.getUsers(getPagination(pageRequest), roleId);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 修改用户密码
     * @param id
     * @param originalPassword
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码", httpMethod = "POST")
    @RequestMapping(value = "{id}/modifypwd", method = RequestMethod.POST)
    public Integer modifyPassword(
            @ApiParam(value = "用户Id", required = true)  @PathVariable("id") Long id,
            @ApiParam(value = "原密码", required = true)  @RequestParam("originalPassword") String originalPassword,
            @ApiParam(value = "新密码", required = true)  @RequestParam("newPassword") String newPassword) {
        return userService.modifyPassword(id, originalPassword, newPassword);
    }

}
