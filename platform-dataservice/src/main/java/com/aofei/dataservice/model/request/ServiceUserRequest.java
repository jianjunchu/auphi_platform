package com.aofei.dataservice.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.aofei.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Data
public class ServiceUserRequest extends BaseRequest<ServiceUserRequest> {

    /**
     * 用户 id
     */
    private Long userId;
    /**
     * 组织 id
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize=false)
    private Long organizerId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 系统 IP
     */
    private String systemIp;
    /**
     * 系统描述
     */
    private String systemDesc;




}
