package com.aofei.dataquality.model.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 数据质量规则分组
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
@Data
public class RuleGroupResponse  {

    private static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    @ApiModelProperty(value = "分组Id")
    private Long groupId;
    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 分组描述
     */
    @ApiModelProperty(value = "分组描述")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
