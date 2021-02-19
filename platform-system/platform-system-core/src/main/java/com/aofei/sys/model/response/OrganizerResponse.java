package com.aofei.sys.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 组织信息
 * </p>
 *
 * @author Tony
 * @since 2018-10-09
 */
@Data
public class OrganizerResponse  {


    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    private Long organizerId;
    /**
     * 组织名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 上级ID
     */
    private Long parentId;


    private String contact;

    private String email;

    private String telphone;

    private String mobile;

    private String address;

    private String verifyCode;
    /**
     * 0 已注册未验证通过，1已注册并验证通过， 2 注销
     */
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
