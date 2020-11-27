package com.aofei.base.model.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BaseRequest<T> extends PageRequest {



    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    protected Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    protected Date updateTime;


    /**
     * 是否已删除 1=已删除 0=未删除
     */
    @ApiModelProperty(hidden = true)
    private Integer isDeleted;




}
