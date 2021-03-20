package com.aofei.sys.controller;


import com.aofei.base.controller.BaseController;
import com.aofei.base.model.response.Response;
import com.aofei.sys.entity.Province;
import com.aofei.sys.model.request.ProvinceRequest;
import com.aofei.sys.model.response.ProvinceResponse;
import com.aofei.sys.service.IProvinceService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 省信息 前端控制器
 * </p>
 *
 * @author Tony
 * @since 2021-03-16
 */
@Log4j
@Api(tags = { "系统管理-省市信息接口" })
@RestController
@RequestMapping(value = "/sys/province", produces = {"application/json;charset=UTF-8"})
public class ProvinceController extends BaseController {


    @Autowired
    private IProvinceService provinceService;
    /**
     * 资源库列表(分页查询)
     * @param request
     * @return
     */
    @ApiOperation(value = "资源库列表(分页查询)", notes = "资源库列表(分页查询)")
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public Response<List<ProvinceResponse>> listProvinces(@ApiIgnore ProvinceRequest request)  {

        List<Province> list = provinceService.selectList(new EntityWrapper<Province>());

        return Response.ok(list) ;
    }
}

