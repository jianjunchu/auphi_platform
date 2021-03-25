package org.firzjb.dataservice.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import org.firzjb.base.entity.DataEntity;
import org.firzjb.base.model.response.BaseResponse;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_USER")
public class ServiceUserResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

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
