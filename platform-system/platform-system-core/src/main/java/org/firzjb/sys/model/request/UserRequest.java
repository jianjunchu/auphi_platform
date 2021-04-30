package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 15:06
 */
@Setter
@Getter
public class UserRequest<User> extends BaseRequest<User> {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称（名称）
     */
    private String nickName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String mobilephone;
    /**
     * 描述
     */
    private String description;
    /**
     * 系统用户
     */
    private Integer isSystemUser;
    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 部门id
     */
    private Long unitId;

    /**
     * 用户状态
     */
    @ApiModelProperty(hidden = true)
    private Integer userStatus;

    /**
     * 最后一次登录时间
     */
    @ApiModelProperty(hidden = true)
    private Date lastLoginTime;
    /**
     * 最后一次登录IP
     */
    @ApiModelProperty(hidden = true)
    private String lastLoginIp;

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

    public UserRequest() {
    }

    public UserRequest(Long userId) {
       setUserId(userId);
    }
}
