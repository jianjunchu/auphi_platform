package org.firzjb.query.controller;

import com.alibaba.fastjson.JSONObject;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.base.model.vo.DataGrid;
import org.firzjb.query.model.request.*;
import org.firzjb.query.model.response.DataLoadTResponse;
import org.firzjb.query.model.response.ErrorTResponse;
import org.firzjb.query.service.*;
import org.firzjb.utils.DateUtils;
import org.firzjb.utils.ExcelUtil;
import org.firzjb.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = { "查询统计-查询统计" })
@RestController
@RequestMapping("/queryStatistics")
public class DataQueryController extends BaseController {

    @Autowired
    private IBatchRevTService batchRevTService;

    @Autowired
    private IDataLoadTService dataLoadTService;

    @Autowired
    private ISerrorTService serrorTService;

    @Autowired
    private IPerrorLogService perrorLogService;

    @Autowired
    private IDataExactTService dataExactTService ;

    @Autowired
    private IPdataExtractTService pdataExtractTService ;

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
    public Response<DataGrid> filerecvPage(
            @ApiIgnore BatchRevTRequest request
            ,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {



        Page page = batchRevTService.getPage(getPagination(request), request);
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
    public Response<DataGrid<DataLoadTResponse>> backupFrequencyPage(@ApiIgnore DataLoadTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {

        request.setUnitNo(user.getUnitCode());
        Page<DataLoadTResponse> page = dataLoadTService.getBackupFrequencyPage(getPagination(request), request);
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
    @RequestMapping(value = "/backupFrequency/chartData", method = RequestMethod.GET)
    public Response<JSONObject> backupFrequencyChartData(@ApiIgnore DataLoadTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {
        request.setUnitNo(user.getUnitCode());
        JSONObject jsonObject  = dataLoadTService.getBackupRecordChartData(request);
        return Response.ok(jsonObject) ;
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
    public Response<DataGrid<DataLoadTResponse>> backupRecordPage(@ApiIgnore DataLoadTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, SQLException {
        if(!StringUtils.isEmpty(user.getUnitCode())){
            request.setUnitNo(user.getUnitCode());
        }
        Page<DataLoadTResponse> page = dataLoadTService.getPage(getPagination(request), request);
        return Response.ok(buildDataGrid(page)) ;
    }

    /**
     * 服务端错误数据查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "服务端错误数据查询(分页查询)", notes = "服务端错误数据查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/s_error/listPage", method = RequestMethod.GET)
    public Response<Object> s_error(
            HttpServletResponse response,
            @ApiIgnore SerrorTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws Exception {


        request.setUnitNo(user.getUnitCode());

        if(request.getExport() == 1){
            List<Map<String,Object>> dataList = serrorTService.getList(request);

            String[] headers = {"受理号","上传单位代码","所属备份文件名","错误代码","错误描述","操作","处理状态","创建时间","更新时间"};

            String[][] values = new String[dataList.size()][headers.length];

            for(int i = 0;i < dataList.size();i++ ){
                values[i][0] = dataList.get(i).get("acceptNo").toString();
                values[i][1] = dataList.get(i).get("unitNo").toString();
                values[i][2] = dataList.get(i).get("backupFile").toString();
                values[i][3] = dataList.get(i).get("errorCode").toString();
                values[i][4] = dataList.get(i).get("errorDesc").toString();
                values[i][5] = dataList.get(i).get("currOperation").toString();
                values[i][6] = dataList.get(i).get("dealFlag").toString();
                values[i][7] = dataList.get(i).get("createTime").toString();
                values[i][8] = dataList.get(i).get("updateTime").toString();
            }

            HSSFWorkbook workbook =  ExcelUtil.getHSSFWorkbook(null,null,"错误数据",headers,values,null);
            String path =  ExcelUtil.exportExcel(workbook,"错误数据_"+ System.currentTimeMillis()+".xls");

            return Response.ok(path) ;

        }else{
            Page<ErrorTResponse> page = serrorTService.getPage(getPagination(request), request);
            return Response.ok(buildDataGrid(page)) ;
        }
    }

    /**
     * 服务端错误数据查询(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "服务端错误数据查询(分页查询)", notes = "服务端错误数据查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/p_error/listPage", method = RequestMethod.GET)
    public Response<Object> p_error(
            HttpServletResponse response,
            @ApiIgnore PerrorTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws Exception {

        request.setUnitNo(user.getUnitCode());

        if(request.getExport() == 1){
            List<Map<String,Object>> dataList = perrorLogService.getList(request);

            String[] headers = {"单位代码","文件名","受理号/组号","错误代码","错误描述","操作","入库时间","处理状态","生成时间"};

            String[][] values = new String[dataList.size()][headers.length];

            for(int i = 0;i < dataList.size();i++ ){
                values[i][0] = dataList.get(i).get("UNIT_NO").toString();
                values[i][1] = dataList.get(i).get("EXTRACT_FILE").toString();
                values[i][2] = dataList.get(i).get("ACCEPT_GROUP_NO").toString();
                values[i][3] = dataList.get(i).get("ERROR_CODE").toString();
                values[i][4] = dataList.get(i).get("ERROR_DESC").toString();
                values[i][5] = dataList.get(i).get("OPERATION").toString();
                values[i][7] = dataList.get(i).get("DEAL_STATUS").toString();
                values[i][8] = dataList.get(i).get("INSERT_TIME").toString();
            }

            HSSFWorkbook workbook =  ExcelUtil.getHSSFWorkbook(null,null,"错误数据",headers,values,null);
            String path = ExcelUtil.exportExcel(workbook,"错误数据_"+ System.currentTimeMillis()+".xls");

            return Response.ok(path) ;

        }else{
            Page page = perrorLogService.getPage(getPagination(request), request);

            return Response.ok(buildDataGrid(page)) ;
        }


    }
    /**
     * 制证端  数据抽取记录查询
     * @param request
     * @return
     */
    @ApiOperation(value = "制证端  数据抽取记录查询(分页查询)", notes = "制证端  数据抽取记录查询(分页查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码(默认1)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页数量(默认10)", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "export", value = "1:导出", paramType = "query", dataType = "Integer"),

    })
    @RequestMapping(value = "/exact/listPage", method = RequestMethod.GET)
    public Response<Object> exactPage(
            HttpServletResponse response,
            @ApiIgnore PdataExactTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws Exception {

        request.setUnitNo(user.getUnitCode());
        if(request.getExport() == 1){
            List<Map<String,Object>> dataList = pdataExtractTService.getList(request);

            String[] headers = {"批次号","单位代码","文件名","待抽取总数","待抽取有效数量","待抽取无效数量","抽取时间段起始日期","抽取时间段截止日期","最大受理号","生成时间"};

            String[][] values = new String[dataList.size()][headers.length];

            for(int i = 0;i < dataList.size();i++ ){
                values[i][0] = dataList.get(i).get("BATCH_NO")+"";
                values[i][1] = dataList.get(i).get("UNIT_NO")+"";
                values[i][2] = dataList.get(i).get("EXTRACT_FILE")+"";
                values[i][3] = dataList.get(i).get("EXTRACT_TOTAL_COUNT")+"";
                values[i][4] = dataList.get(i).get("EXTRACT_VALID_COUNT")+"";
                values[i][5] = dataList.get(i).get("EXTRACT_INVALID_COUNT")+"";
                values[i][6] = dataList.get(i).get("EXTRACT_START")+"";
                values[i][7] = dataList.get(i).get("EXTRACT_END")+"";
                values[i][8] = dataList.get(i).get("MAX_ACCEPT_NO")+"";
                values[i][9] = dataList.get(i).get("INSERT_TIME")+"";
            }

            HSSFWorkbook workbook =  ExcelUtil.getHSSFWorkbook(null,null,"数据抽取记录",headers,values,null);
            String path= ExcelUtil.exportExcel(workbook,DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSS) +".xls");

            return Response.ok(path) ;

        }else{
            Page page = pdataExtractTService.getPage(getPagination(request), request);
            return Response.ok(buildDataGrid(page)) ;
        }
    }


    /**
     * 服务端 数据装载情况 地图显示
     * @param request
     * @return
     */
    @ApiOperation(value = "服务端 数据装载情况 地图显示", notes = "服务端 数据装载情况 地图显示")
    @RequestMapping(value = "/exact/sdata_loading_statistical", method = RequestMethod.GET)
    public Response<JSONObject> sdata_loading_statistical(
            @ApiIgnore DataLoadTRequest request,@ApiIgnore @CurrentUser CurrentUserResponse user) throws Exception {

        request.setUnitNo(user.getUnitCode());

        if(StringUtils.isEmpty(request.getStartBackupTime())){
            long l = System.currentTimeMillis() - 3600 * 1000 * 24 * 15;
            request.setStartBackupTime(DateUtils.format(new Date(l),"yyyyMMdd000000"));
        }
        if(StringUtils.isEmpty(request.getEndBackupTime())){

            request.setEndBackupTime(DateUtils.format(new Date(),"yyyyMMdd235959"));
        }

        JSONObject dataList = dataLoadTService.get_sdata_loading_statistical(request);
        return Response.ok(dataList);

    }

}
