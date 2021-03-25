package org.firzjb.schedule.model.response;

import org.firzjb.base.model.response.BaseResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-10-05 20:44
 */
@Data
public class GroupResponse extends BaseResponse {

    private String groupId;

    /**
     * 部门
     */
    private Long unitId;

    private String unitName;

    private String unitCode;


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
