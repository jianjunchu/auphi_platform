package com.aofei.query.controller;

import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.Response;
import com.aofei.base.model.vo.DataGrid;
import com.aofei.query.model.request.BatchRevTRequest;
import com.aofei.query.model.request.DataExactTRequest;
import com.aofei.query.model.request.DataLoadTRequest;
import com.aofei.query.model.request.ErrorTRequest;
import com.aofei.query.model.response.BatchRevTResponse;
import com.aofei.query.model.response.DataExactTResponse;
import com.aofei.query.model.response.DataLoadTResponse;
import com.aofei.query.model.response.ErrorTResponse;
import com.aofei.query.service.IBatchRevTService;
import com.aofei.query.service.IDataExactTService;
import com.aofei.query.service.IDataLoadTService;
import com.aofei.query.service.IErrorTService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = { "查询统计-查询统计" })
@RestController
@RequestMapping("/queryStatistics")
public class DataQueryController extends BaseController {

    @Autowired
    private IBatchRevTService batchRevTService;

    @Autowired
    private IDataLoadTService dataLoadTService;

    @Autowired
    private IErrorTService errorTService ;

    @Autowired
    private IDataExactTService dataExactTService ;

    /**
     * 文件接收记录查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "文件接收记录查询(分页查询)", notes = "文件接收记录查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/filerecv/listPage", method = RequestMethod.GET)
    public Response<DataGrid<BatchRevTResponse>> filerecvPage(@ApiIgnore BatchRevTRequest request)  {

        Page<BatchRevTResponse> page = batchRevTService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 备份频率统计(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "备份频率统计(分页查询)", notes = "备份频率统计(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/backupFrequency/listPage", method = RequestMethod.GET)
    public Response<DataGrid<DataLoadTResponse>> backupFrequencyPage(@ApiIgnore DataLoadTRequest request)  {

        Page<DataLoadTResponse> page = dataLoadTService.getBackupFrequencyPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }


    /**
     * 备份记录查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "备份记录查询(分页查询)", notes = "备份记录查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/backupRecord/listPage", method = RequestMethod.GET)
    public Response<DataGrid<DataLoadTResponse>> backupRecordPage(@ApiIgnore DataLoadTRequest request)  {

        Page<DataLoadTResponse> page = dataLoadTService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 备份记录查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "错误数据查询(分页查询)", notes = "错误数据查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/error/listPage", method = RequestMethod.GET)
    public Response<DataGrid<ErrorTResponse>> backupRecordPage(@ApiIgnore ErrorTRequest request)  {

        Page<ErrorTResponse> page = errorTService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 备份记录查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "数据抽取记录查询(分页查询)", notes = "数据抽取记录查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/exact/listPage", method = RequestMethod.GET)
    public Response<DataGrid<DataExactTResponse>> exactPage(@ApiIgnore DataExactTRequest request)  {

        Page<DataExactTResponse> page = dataExactTService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }
}
