package org.firzjb.dataservice.entity;

import java.io.Serializable;
import java.util.Date;
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
 * @since 2019-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASERVICE_INTERFACE_FIELD")
public class ServiceInterfaceField extends DataEntity<ServiceInterfaceField> {

    private static final long serialVersionUID = 1L;

    /**
     * 字段 ID
     */
    @TableId(value = "FIELD_ID", type = IdType.ID_WORKER)
    private Long fieldId;
    /**
     * 所属接口 ID
     */
    @TableField("SERVICE_ID")
    private Long serviceId;
    /**
     * 字段名称
     */
    @TableField("FIELD_NAME")
    private String fieldName;
    /**
     * 字段类型
     */
    @TableField("FIELD_TYPE")
    private String fieldType;
    /**
     * 路径$开头
     */
    @TableField("JSON_PATH")
    private String jsonPath;



    @Override
    protected Serializable pkVal() {
        return this.fieldId;
    }

}
