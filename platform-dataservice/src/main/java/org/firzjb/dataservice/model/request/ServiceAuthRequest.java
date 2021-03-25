package org.firzjb.dataservice.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import org.firzjb.base.model.request.BaseRequest;

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
public class ServiceAuthRequest extends BaseRequest<ServiceAuthRequest> {

    private static final long serialVersionUID = 1L;

    private Long authId;
    /**
     * 组织 ID
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize=false)
    private Long organizerId;
    /**
     * 授权用户
     */
    private Long userId;
    /**
     * 授权服务接口
     */
    private Long serviceId;
    /**
     * 授权 IP
     */
    private String authIp;
    /**
     * 业务用途
     */
    private String useDesc;
    /**
     * 使用部门
     */
    private String useDept;
    /**
     * 使用人员
     */
    private String userName;


}
