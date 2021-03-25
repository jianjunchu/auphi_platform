package org.firzjb.dataquality.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 数据质量管理-规则管理
 * </p>
 *
 * @author Tony
 * @since 2020-11-02
 */
@Data
public class RuleRequest extends BaseRequest<RuleRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 规则id
     */
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;
    /**
     * 分组id
     */
    @ApiModelProperty(value = "规则分组id")
    private Long ruleGroupId;
    /**
     * 规则描述
     */
    @ApiModelProperty(value = "规则描述")
    private String description;
    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    private Integer ruleType;
    /**
     * 数据库ID
     */
    @ApiModelProperty(value = "数据库ID")
    private Long databaseId;
    /**
     * 模式名
     */
    @ApiModelProperty(value = "模式名")
    private String schemaName;
    /**
     * 主题表名
     */
    @ApiModelProperty(value = "主题表名")
    private String tableName;
    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String fieldName;

    /**
     * 条件
     */
    @ApiModelProperty(value = "条件")
    private String condition;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;

    @ApiModelProperty(value = "字段原生类型")
    private String fieldOriginalType;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Integer isEnable;

    /**
     *
     */
    private List<RuleAttrRequest> attr;

}
