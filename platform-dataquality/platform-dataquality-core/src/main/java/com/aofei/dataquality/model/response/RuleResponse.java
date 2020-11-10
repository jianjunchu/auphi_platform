package com.aofei.dataquality.model.response;

import com.aofei.dataquality.model.request.RuleAttrRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
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
public class RuleResponse {

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
     * 规则分组
     */
    @ApiModelProperty(value = "规则分组")
    private String ruleGroupName;
    /**
     * 规则描述
     */
    @ApiModelProperty(value = "规则描述")
    private String description;
    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    private String ruleType;
    /**
     * 数据库ID
     */
    @ApiModelProperty(value = "数据库ID")
    private Long databaseId;

    /**
     * 数据库
     */
    @ApiModelProperty(value = "数据库")
    private String databaseName;
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
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Integer isEnable;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private List<RuleAttrResponse> attr;
}
