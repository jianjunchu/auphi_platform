package com.aofei.sys.controller;

/**
 * @auther 傲飞数据整合平台
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
import com.aofei.sys.entity.User;
import com.aofei.sys.exception.SystemError;
import com.aofei.sys.model.request.EditUserPasswordRequest;
import com.aofei.sys.model.request.UserRequest;
import com.aofei.sys.model.response.MenuResponse;
import com.aofei.sys.model.response.RoleResponse;
import com.aofei.sys.model.response.UserResponse;
import com.aofei.sys.service.IMenuService;
import com.aofei.sys.service.IRoleService;
import com.aofei.sys.service.IUserRoleService;
import com.aofei.sys.service.IUserService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 傲飞数据整合平台
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
    IRoleService roleService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IUserRoleService userRoleService;


    /**
     * 用户列表(分页查询)
     * @return
     */
    @ApiOperation(value = "当前登录用户信息", notes = "当前登录用户信息")
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public Response<UserResponse> my(
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        UserResponse response = userService.get(user.getUserId());



        if(response!=null){

            List<RoleResponse> roleResponses =  roleService.getRolesByUser(response.getUserId());
            if(roleResponses.contains(new RoleResponse(1L))
                    || roleResponses.contains(new RoleResponse(2L))
                    || roleResponses.contains(new RoleResponse(3L))
                    || roleResponses.contains(new RoleResponse(4L))){
                response.setHomePage("/home/sdata_loading_statistical");
            }else {
                response.setHomePage("/home/dashboard");
            }


            List<MenuResponse>  list = menuService.getMenusByUser(response.getUserId());
            List<String> roles = new ArrayList<>();

            for(MenuResponse menuResponse : list){
                roles.add(menuResponse.getPerms());
            }
            if(response.getIsSystemUser()!=null && response.getIsSystemUser() ==1){
                roles.add("admin");
            }

            response.setRoles(roles);
            return Response.ok(response);
        }else{
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(),"用户未登录或者用户已不存在");
        }

    }

    /**
     * 用户列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "用户列表(分页查询)", notes = "用户列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Response<DataGrid<UserResponse>> page(@ApiIgnore UserRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user)  {
        if(user.getOrganizerId()>0){
            request.setOrganizerId(user.getOrganizerId());
        }
        if(user.getUnitId()>0){
            request.setUnitId(user.getUnitId());
        }
        Page<UserResponse> page = userService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 新建用户
     * @param request
     * @return
     */
    @Log(module = "用户管理",description = "新建用户")
    @ApiOperation(value = "新建用户", notes = "新建用户")
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
        if(request.getOrganizerId() == null){
            request.setOrganizerId(user.getOrganizerId());
        }
        if(request.getUnitId() == null){
            request.setUnitId(user.getUnitId());
        }

        userService.save(request);

        return Response.ok() ;
    }

    /**
     * 编辑用户
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "编辑用户")
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
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
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
    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户")
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
    @ApiOperation(value = "修改用户拥有的角色", notes = "修改用户拥有的角色")
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
    @ApiOperation(value = "查询角色下的用户列表", notes = "查询角色下的用户列表")
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    public Response<DataGrid<UserResponse>> getUserByRoleCode(@PathVariable("roleId") Long roleId, PageRequest pageRequest) {
        Page<UserResponse> page = userService.getUsers(getPagination(pageRequest), roleId);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 修改用户密码
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @RequestMapping(value = "/modifypwd", method = RequestMethod.POST)
    public Response<Integer> modifyPassword(
            @ApiIgnore @CurrentUser CurrentUserResponse user,
            @RequestBody EditUserPasswordRequest request) {
        return Response.ok(userService.modifyPassword(user.getUserId(), request.getOriginalPassword(), request.getNewPassword()));
    }

}