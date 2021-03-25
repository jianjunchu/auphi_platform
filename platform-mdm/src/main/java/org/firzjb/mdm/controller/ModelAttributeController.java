package org.firzjb.mdm.controller;

import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.Response;
import org.firzjb.mdm.model.request.ModelAttributeRequest;
import org.firzjb.mdm.model.request.ModelRequest;
import org.firzjb.mdm.model.response.ModelAttributeResponse;
import org.firzjb.mdm.model.response.ModelResponse;
import org.firzjb.mdm.service.IModelAttributeService;
import org.firzjb.mdm.service.IModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.firzjb.mdm.model.request.ModelAttributeRequest;
import org.firzjb.mdm.model.response.ModelAttributeResponse;
import org.firzjb.mdm.service.IModelAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = { "主数据管理-数据模型属性" })
@RestController
@RequestMapping("/mdm/attribute")
public class ModelAttributeController extends BaseController {

    @Autowired
    IModelAttributeService modelAttributeService;

    /**
     * 数据模型列表
     * @param request
     * @return
     */
    @ApiOperation(value = "所有数据模型属性列表", notes = "所有数据模型属性列表")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public Response<List<ModelAttributeResponse>> list(@ApiIgnore ModelAttributeRequest request)  {
        List<ModelAttributeResponse> list = modelAttributeService.getModelAttributes(request);
        return Response.ok(list) ;
    }
}
