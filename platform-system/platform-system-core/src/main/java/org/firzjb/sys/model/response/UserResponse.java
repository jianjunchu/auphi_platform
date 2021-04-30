package org.firzjb.sys.model.response;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-13 15:28
 */
@Getter
@Setter
public class UserResponse {



    /**
     * 主键
     */
    private Long userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "登录名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(hidden = true)
    private String password;
    /**
     * 昵称（名称）
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 手机
     */
    @ApiModelProperty(value = "手机号")
    private String mobilephone;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 系统用户
     */
    @ApiModelProperty(value = "系统用户")
    private Integer isSystemUser;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID")
    private Long organizerId;



    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织名称")
    private String organizerName;

    /**
     * 部门id
     */
    private Long unitId;

    private String unitCode;

    private String unitName;

    /**
     * 磁盘空间 字节 默认   1073741824字节
     */
    private Long diskSpace;
    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    private Integer userStatus;

    /**
     * 最后一次登录时间
     */
    @ApiModelProperty(value = "最后一次登录时间")
    private Date lastLoginTime;
    /**
     * 最后一次登录IP
     */
    @ApiModelProperty(value = "最后一次登录IP")
    private String lastLoginIp;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备用字段1
     */
    private String field1;

    /**
     * 备用字段2
     */
    private String field2;

    /**
     * 备用字段3
     */
    private String field3;

    /**
     * 备用字段4
     */
    private String field4;

    private String homePage;

    private List<String> roles;

}
