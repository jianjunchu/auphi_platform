package com.aofei.dataquality.model.request;

import com.aofei.base.model.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
public class CompareSqlFieldRequest extends BaseRequest<CompareSqlFieldRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long compareSqlFieldId;
    /**
     * 稽核ID
     */
    private Long compareSqlId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 比较的字段名称
     */
    private String refFieldName;
    /**
     * 字段说明
     */
    private String fieldDesc;
    /**
     * 0 等值比较，1 范围比较
     */
    private Integer fieldCompareType;

    private BigDecimal minRatio;

    private BigDecimal maxRatio;



}
