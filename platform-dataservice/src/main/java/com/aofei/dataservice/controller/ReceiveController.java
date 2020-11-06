package com.aofei.dataservice.controller;

import com.aofei.base.model.response.Response;
import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  数据发布接口管理控制器
 * </p>
 *
 * @author Tony
 * @since 2018-11-10
 */
@Log4j
@RestController
@RequestMapping("/dataservice/receive")
@Api(tags = { "数据服务-数据接收接口管理" })
public class ReceiveController {

    /**
     * 根据Id查询对外数据接出接口
     * @param id
     * @return
     */
    @ApiOperation(value = "数据服务接口-根据Id查询数据接收接口", notes = "根据Id查询数据接收接口")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<ServiceInterfaceResponse> get(
            @PathVariable Long id)  {
        return null;
    }


}
