package com.aofei.datasource.entity;

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
 * 数据接口返回值字段映射
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATASOURCE_INTERFACE_RESPONSE_MAPPING")
public class InterfaceResponseMapping extends DataEntity<InterfaceResponseMapping> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "INTERFACE_RESPONSE_MAPPING_ID", type = IdType.ID_WORKER)
    private Long interfaceResponseMappingId;
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
     * 要解析的JSON路径
     */
    @TableField("JSON_PATH")
    private String jsonPath;
    /**
     * JSON路径对应的新的字段名
     */
    @TableField("FIELD_NAME")
    private String fieldName;
    /**
     * 字段说明
     */
    @TableField("FIELD_DESC")
    private String fieldDesc;



    @Override
    protected Serializable pkVal() {
        return this.interfaceResponseMappingId;
    }

}
