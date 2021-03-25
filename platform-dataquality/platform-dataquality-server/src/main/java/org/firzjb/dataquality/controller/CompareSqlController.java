package org.firzjb.dataquality.controller;


import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.base.model.vo.DataGrid;
import org.firzjb.dataquality.model.request.CompareSqlRequest;
import org.firzjb.dataquality.model.request.CompareSqlResultRequest;
import org.firzjb.dataquality.model.response.CompareSqlFieldResponse;
import org.firzjb.dataquality.model.response.CompareSqlResponse;
import org.firzjb.dataquality.model.response.CompareSqlResultResponse;
import org.firzjb.dataquality.service.ICompareSqlFieldService;
import org.firzjb.dataquality.service.ICompareSqlResultService;
import org.firzjb.dataquality.service.ICompareSqlService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Api(tags = { "数据质量管理-数据稽核" })
@Authorization
@RestController
@RequestMapping("/dataQuality/compare")
public class CompareSqlController extends BaseController {

    @Autowired
    ICompareSqlFieldService compareSqlFieldService;

    @Autowired
    ICompareSqlService compareSqlService;

    @Autowired
    ICompareSqlResultService compareSqlResultService;

    /**
     * 数据稽核(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "数据稽核(分页查询)", notes = "数据稽核(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public Response<DataGrid<CompareSqlResultResponse>> page(
            @ApiIgnore CompareSqlResultRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        Page<CompareSqlResultResponse> page = compareSqlResultService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    @ApiOperation(value = "数据稽核", notes = "数据稽核-获取稽核字段")
    @RequestMapping(value = "/getCompareFields", method = RequestMethod.POST)
    public Response<List<CompareSqlFieldResponse>> getCompareFields(
            @RequestBody CompareSqlRequest request)  {

        List<CompareSqlFieldResponse> list = compareSqlFieldService.getCompareFields(request);

        return Response.ok(list);

    }


    @ApiOperation(value = "新建数据稽核", notes = "新建数据稽核")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<CompareSqlResponse> add(
            @RequestBody CompareSqlRequest request, @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        return Response.ok(compareSqlService.save(request)) ;
    }


    /**
     * 编辑数据稽核
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑数据稽核", notes = "编辑数据稽核")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<CompareSqlResponse> edit(
            @RequestBody CompareSqlRequest request)  {
        return Response.ok(compareSqlService.update(request)) ;
    }

    /**
     * 删除数据稽核
     * @param id
     * @return
     */
    @ApiOperation(value = "删除数据稽核", notes = "删除数据稽核", httpMethod = "DELETE")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public Response<Integer> del(
            @PathVariable Long id)  {
        return Response.ok(compareSqlService.del(id)) ;
    }

    /**
     * 根据Id查询数据稽核
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id查询数据稽核", notes = "根据Id查询数据稽核")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<CompareSqlResponse> get(
            @PathVariable Long id)  {

        return Response.ok(compareSqlService.get(id)) ;
    }

    /**
     * 刷新数据稽核
     * @return
     */
    @ApiOperation(value = "刷新数据稽核", notes = "刷新数据稽核")
    @RequestMapping(value = "/refreshAll", method = RequestMethod.GET)
    public Response<Boolean> refreshAll(@ApiIgnore CompareSqlResultRequest request,
                                        @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        CompareSqlRequest compareSqlRequest = new CompareSqlRequest();
        compareSqlRequest.setOrganizerId(user.getOrganizerId());
        List<CompareSqlResponse> list = compareSqlService.getCompareSqls(compareSqlRequest);

        for(CompareSqlResponse sql : list){
            compareSqlResultService.execCompareSql(sql.getCompareSqlId());
        }


        return Response.ok() ;
    }

}

