package com.aofei.sys.model.request;

import com.aofei.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-09-18 14:23
 */
@Getter
@Setter
public class RoleRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 备注
     */
    private String description;

    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 设计器权限
     */
    private Long priviledges;
    /**
     * 是否是系统保留权限
     */
    private Integer isSystemRole;
}
