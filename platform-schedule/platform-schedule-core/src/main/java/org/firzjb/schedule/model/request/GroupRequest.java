package org.firzjb.schedule.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-10-05 20:45
 */
@Data
public class GroupRequest extends BaseRequest {


    private String groupId;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 部门
     */
    @ApiModelProperty(hidden = true)
    private Long unitId;

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
}
