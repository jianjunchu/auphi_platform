package com.aofei.dataservice.model.request;

import com.aofei.base.entity.DataEntity;
import com.aofei.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tony
 * @since 2019-07-27
 */
@Data
public class ServiceInterfaceFieldRequest extends BaseRequest<ServiceInterfaceFieldRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 字段 ID
     */
    private Long fieldId;
    /**
     * 所属接口 ID
     */
    private Long serviceId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 路径$开头
     */
    private String jsonPath;


}
