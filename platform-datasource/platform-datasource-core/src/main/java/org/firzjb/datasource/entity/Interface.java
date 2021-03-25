package org.firzjb.datasource.entity;

import org.firzjb.base.entity.DataEntity;
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
 * 数据接口管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASOURCE_INTERFACE")
public class Interface extends DataEntity<Interface> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "INTERFACE_ID", type = IdType.ID_WORKER)
    private Long interfaceId;
    /**
     * 组织 id
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 接口名称
     */
    @TableField("INTERFACE_NAME")
    private String interfaceName;

    @TableField("INTERFACE_URL")
    private String interfaceUrl;
    /**
     * 接口请求方式, 1 get 2 post
     */
    @TableField("REQUEST_TYPE")
    private String requestType;
    /**
     * 接口说明
     */
    @TableField("INTERFACE_DESC")
    private String interfaceDesc;
    /**
     * 返回数据格式的描述
     */
    @TableField("RETURN_DESC")
    private String returnDesc;
    /**
     * 访问超时时间,单位分钟
     */
    @TableField("TIMEOUT")
    private Integer timeout;



    @Override
    protected Serializable pkVal() {
        return this.interfaceId;
    }

}
