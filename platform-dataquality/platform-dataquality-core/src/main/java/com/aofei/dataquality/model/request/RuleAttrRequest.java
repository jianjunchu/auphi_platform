package com.aofei.dataquality.model.request;

import com.aofei.base.entity.DataEntity;
import com.aofei.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据质量管理-规则属性值
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
@Data
public class RuleAttrRequest extends BaseRequest<RuleAttrRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 规则属性值ID
     */
    private Long ruleAttrId;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;

    /**
     * 规则id
     */
    private Long ruleId;

    private String code;
    private String valueStr;


}
