package com.aofei.dataquality.model.request;

import com.aofei.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
public class CompareSqlResultRequest extends BaseRequest<CompareSqlResultRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;

    private Long compareSqlId;

    /**
     * ID in profile_table_group
     */
    private Long ruleGroupId;

    private Long compareSqlResultId;

    private Long compareSqlFieldId;

    private String fieldValue;

    /**
     * 字段名称
     */
    private String fieldName;


    private String refFieldValue;
    /**
     * 1 equals,   0  not equals
     */
    private Integer compareResult;


}
