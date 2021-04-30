package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @auther 制证数据实时汇聚系统
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
