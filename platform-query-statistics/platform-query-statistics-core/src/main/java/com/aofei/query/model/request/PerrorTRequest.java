package com.aofei.query.model.request;

import com.aofei.base.model.request.PageRequest;
import lombok.Data;

/**
 * 制证端错误记录表
 */
@Data
public class PerrorTRequest extends PageRequest {


    /**
     * 上传单位代码
     */
    private String unitNo;




}
