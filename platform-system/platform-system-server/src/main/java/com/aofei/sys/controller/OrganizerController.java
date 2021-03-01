package com.aofei.sys.controller;


import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.controller.BaseController;
import com.aofei.base.exception.ApplicationException;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.log.annotation.Log;
import com.aofei.sys.entity.Organizer;
import com.aofei.sys.entity.User;
import com.aofei.sys.exception.SystemError;
import com.aofei.sys.model.request.OrganizerRequest;
import com.aofei.sys.model.response.OrganizerResponse;
import com.aofei.sys.service.IOrganizerService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Log4j
@Api(tags = { "系统管理-组织管理模块接口" })
@RestController
@RequestMapping(value = "/sys/org", produces = {"application/json;charset=UTF-8"})
public class OrganizerController extends BaseController {


    @Autowired
    IOrganizerService organizerService;

    /**
     * 组织列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "组织管理(分页查询)", notes = "组织列表(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Response<DataGrid<OrganizerResponse>> page(@ApiIgnore OrganizerRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setParentId(user.getOrganizerId());
        Page<OrganizerResponse> page = organizerService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 菜单列表
     * @param request
     * @return
     */
    @ApiOperation(value = "组织管理", notes = "组织列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "组织名称(模糊查询)", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<OrganizerResponse>> list(@ApiIgnore OrganizerRequest request)  {

        List<OrganizerResponse> list = organizerService.getOrganizers(request);
        return Response.ok(list) ;
    }

    /**
     * 新建组织
     * @param request
     * @return
     */
    @Log(module = "组织管理",description = "新建组织")
    @ApiOperation(value = "新建组织", notes = "新建组织")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<OrganizerResponse> add(
            @RequestBody OrganizerRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        int codeCount = organizerService.selectCount(new EntityWrapper<Organizer>()
                .eq("PARENT_ID",request.getParentId())
                .eq("ORGANIZER_CODE",request.getCode())
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(codeCount>0){
            throw new ApplicationException(SystemError.PHONE_NUMBER_EXIST.getCode(),"编号重复");
        }
        if(request.getParentId() == null){
            request.setParentId(user.getOrganizerId());
        }
        request.setStatus(1);
        organizerService.save(request);

        return Response.ok() ;
    }
    /**
     * 编辑组织
     * @param request
     * @return
     */
    @ApiOperation(value = "组织管理", notes = "编辑组织")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<OrganizerResponse> edit(
            @RequestBody OrganizerRequest request)  {

        int codeCount = organizerService.selectCount(new EntityWrapper<Organizer>()
                .ne("ORGANIZER_ID",request.getOrganizerId())
                .eq("PARENT_ID",request.getParentId())
                .eq("ORGANIZER_CODE",request.getCode())
                .eq("DEL_FLAG",User.DEL_FLAG_NORMAL));
        if(codeCount>0){
            throw new ApplicationException(SystemError.PHONE_NUMBER_EXIST.getCode(),"编号重复");
        }

        organizerService.update(request);

        return Response.ok() ;
    }

    /**
     * 删除组织
     * @param id
     * @return
     */
    @ApiOperation(value = "组织管理", notes = "删除组织", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {

        return Response.ok(organizerService.del(id)) ;
    }

    /**
     * 根据Id查询组织
     * @param id
     * @return
     */
    @ApiOperation(value = "组织管理", notes = "根据Id查询组织")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<OrganizerResponse> get(
            @PathVariable Long id)  {

        return Response.ok(organizerService.get(id)) ;
    }

}
