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
 * 数据接口参数管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASOURCE_INTERFACE_PARAMETER")
public class InterfaceParameter extends DataEntity<InterfaceParameter> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "INTERFACE_PARAMETER_ID", type = IdType.ID_WORKER)
    private Long interfaceParameterId;
    /**
     * 组织 id
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 接口id
     */
    @TableField("INTERFACE_ID")
    private Long interfaceId;
    /**
     * 参数名称
     */
    @TableField("PARAMETER_NAME")
    private String parameterName;
    /**
     * 参数数据类型
     */
    @TableField("PARAMETER_TYPE")
    private String parameterType;
    /**
     * 参数说明
     */
    @TableField("PARAMETER_DESC")
    private String parameterDesc;



    @Override
    protected Serializable pkVal() {
        return this.interfaceParameterId;
    }

}
