package org.firzjb.sys.controller;

import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.base.model.vo.DataGrid;
import org.firzjb.log.annotation.Log;
import org.firzjb.sys.model.request.RoleRequest;
import org.firzjb.sys.model.response.RoleResponse;
import org.firzjb.sys.service.IRoleMenuService;
import org.firzjb.sys.service.IRoleService;
import org.firzjb.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-12 20:07
 */
@Log4j
@Api(tags = { "系统管理-角色管理模块接口" })
@RestController
@Authorization
@RequestMapping(value = "/sys/role", produces = {"application/json;charset=UTF-8"})
public class RoleController extends BaseController {


    @Autowired
    IRoleService roleService;

    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IUserRoleService userRoleService;

    /**
     * 角色列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "角色列表(分页查询)", notes = "角色列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "roleName", value = "名称(模糊查询)", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<RoleResponse>> page(@ApiIgnore RoleRequest request)  {
        Page<RoleResponse> page = roleService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 角色列表
     * @param request
     * @return
     */
    @ApiOperation(value = "角色列表", notes = "角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "名称(模糊查询)", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<RoleResponse>> list(@ApiIgnore RoleRequest request)  {
        List<RoleResponse> list = roleService.getRoles(request);
        return Response.ok(list) ;
    }


    /**
     * 新建角色
     * @param request
     * @return
     */
    @Log(module = "角色管理",description = "新建角色")
    @ApiOperation(value = "新建角色", notes = "新建角色")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<RoleResponse> add(
            @RequestBody RoleRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user)  {

        if(request.getOrganizerId()==null || request.getOrganizerId()< 1){
            request.setOrganizerId(user.getOrganizerId());
        }

        return Response.ok(roleService.save(request)) ;
    }

    /**
     * 编辑角色
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<RoleResponse> edit(
            @RequestBody RoleRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user)  {

        return Response.ok(roleService.update(request)) ;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "删除角色", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long id)  {
        return Response.ok(roleService.del(id)) ;
    }

    /**
     * 根据Id查询角色
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询角色", notes = "根据Id查询角色")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<RoleResponse> get(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long id)  {

        return Response.ok(roleService.get(id)) ;
    }


    /**
     * 查询用户拥有的角色列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public Response<Map> getRoleByUsername(
            @ApiParam(value = "用户ID", required = true) @PathVariable("userId") Long userId) {
        List<RoleResponse> roles = roleService.getRolesByUser(userId);
        List<String> list = new ArrayList<>();
        for(RoleResponse roleResponse : roles){
            list.add(roleResponse.getRoleId().toString());
        }
        Map<String,Object> res = new HashMap<>();
        res.put("roles",roles);
        res.put("roleIds",list);

        return Response.ok(res);
    }



    /**
     * 删除角色下的用户
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除角色下的用户", notes = "删除角色下的用户")
    @RequestMapping(value = "{roleId}/user/{userId}/delete", method = RequestMethod.POST)
    public Response<Integer> deleteUserRole(
            @ApiParam(value = "角色ID", required = true)  @PathVariable("roleId") Long roleId,
            @ApiParam(value = "用户ID", required = true) @PathVariable("userId") Long userId) {
        return Response.ok(userRoleService.deleteUserRole(userId, roleId));
    }

    /**
     * 修改角色拥有的权限
     *
     * @return
     */
    @ApiOperation(value = "修改角色拥有的权限", notes = "修改角色拥有的权限")
    @RequestMapping(value = "{id}/resource/modify", method = RequestMethod.POST)
    public Response<Integer> changeUserRole(
            @ApiParam(value = "角色ID", required = true)@PathVariable("id") Long roleId,
            @ApiParam(value = "菜单ID数组json对象", required = true) @RequestBody List<Long> resources) {

        return Response.ok(roleMenuService.changeRolePermission(roleId, resources));
    }





}
