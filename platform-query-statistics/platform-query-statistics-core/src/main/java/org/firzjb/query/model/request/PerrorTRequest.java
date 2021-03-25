package org.firzjb.query.model.request;

import org.firzjb.base.model.request.PageRequest;
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
