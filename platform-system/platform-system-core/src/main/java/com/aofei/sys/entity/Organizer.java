package com.aofei.sys.entity;

import com.aofei.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 组织信息
 * </p>
 *
 * @author Tony
 * @since 2018-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("SYS_ORGANIZER")
public class Organizer extends DataEntity<Organizer> {


    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    @TableId(value = "ORGANIZER_ID", type = IdType.ID_WORKER)
    private Long organizerId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZER_NAME")
    private String name;

    /**
     * 编码
     */
    @TableField("ORGANIZER_CODE")
    private String code;

    /**
     * 上级ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    @TableField("PROVINCE_ID")
    private Long provinceId;

    @TableField(exist = false)
    private String provinceName;

    @TableField("ORGANIZER_CONTACT")
    private String contact;
    @TableField("ORGANIZER_EMAIL")
    private String email;
    @TableField("ORGANIZER_TELPHONE")
    private String telphone;
    @TableField("ORGANIZER_MOBILE")
    private String mobile;
    @TableField("ORGANIZER_ADDRESS")
    private String address;
    @TableField("ORGANIZER_VERIFY_CODE")
    private String verifyCode;
    /**
     * 0 已注册未验证通过，1已注册并验证通过， 2 注销
     */
    @TableField("ORGANIZER_STATUS")
    private Integer status;

    /**
     * 经度
     */
    @TableField("LONGITUDE")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("LATITUDE")
    private Double latitude;


    @Override
    protected Serializable pkVal() {
        return this.organizerId;
    }

}
