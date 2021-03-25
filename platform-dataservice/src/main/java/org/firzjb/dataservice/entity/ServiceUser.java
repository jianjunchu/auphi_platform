package org.firzjb.dataservice.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import org.firzjb.base.entity.DataEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据发布访问用户

 * </p>
 *
 * @author Tony
 * @since 2019-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_USER")
public class ServiceUser extends DataEntity<ServiceUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "USER_ID", type = IdType.ID_WORKER)
    private Long userId;
    /**
     * 组织 id
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;
    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;
    /**
     * 系统名称
     */
    @TableField("SYSTEM_NAME")
    private String systemName;
    /**
     * 系统 IP
     */
    @TableField("SYSTEM_IP")
    private String systemIp;
    /**
     * 系统描述
     */
    @TableField("SYSTEM_DESC")
    private String systemDesc;



    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
