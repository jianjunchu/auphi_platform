package org.firzjb.dataservice.entity;

import java.io.Serializable;
import java.util.Date;

import org.firzjb.base.entity.BaseEntity;
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
 *
 * </p>
 *
 * @author Tony
 * @since 2019-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_MONITOR")
public class ServiceMonitor extends BaseEntity<ServiceMonitor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MONITOR_ID", type = IdType.ID_WORKER)
    private Long monitorId;
    /**
     * 组织 ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 服务接口 ID
     */
    @TableField("SERVICE_ID")
    private Long serviceId;

    @TableField(exist = false)
    private String serviceName;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;
    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;
    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;
    /**
     * 执行的用户
     */
    @TableField("SERVICE_USER_ID")
    private Long serviceUserId;

    @TableField(exist = false)
    private String  username;

    /**
     * 系统名称
     */
    @TableField("SYSTEM_NAME")
    private String systemName;



    @Override
    protected Serializable pkVal() {
        return this.monitorId;
    }

}
