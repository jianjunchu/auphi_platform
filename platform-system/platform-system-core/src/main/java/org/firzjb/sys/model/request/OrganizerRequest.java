package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
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

    /**
     * 编码
     */
    private String code;

    /**
     * 上级ID
     */
    private Long parentId;

    private Long provinceId;

    private String contact;
    private String email;
    private String telphone;
    private String mobile;
    private String address;
    private String verifyCode;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 0 已注册未验证通过，1已注册并验证通过， 2 注销
     */
    private Integer status;






}
