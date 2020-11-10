package com.aofei.sys.model.request;

import com.aofei.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * <p>
 * 组织信息
 * </p>
 *
 * @author Tony
 * @since 2018-10-09
 */
@Data
public class OrganizerRequest extends BaseRequest<OrganizerRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    private Long organizerId;
    /**
     * 组织名称
     */
    private String name;
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






}
