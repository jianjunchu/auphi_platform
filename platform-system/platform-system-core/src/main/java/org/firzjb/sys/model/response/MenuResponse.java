package org.firzjb.sys.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-18 14:23
 */
@Setter
@Getter
public class MenuResponse {


    private Long menuId;
    /**
     * 父菜单ID，一级菜单为0
     */
    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    private Long parentId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;
    /**
     * 菜单URL
     */
    @ApiModelProperty(value = "菜单URL")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)")
    private String perms;
    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    @ApiModelProperty(value = "类型   0：目录   1：菜单   2：按钮")
    private Integer type;
    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    /**
     * 状态  0：正常   1：禁用
     */
    @ApiModelProperty(value = "状态  0：正常   1：禁用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
