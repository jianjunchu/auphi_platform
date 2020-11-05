package com.aofei.dataquality.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.aofei.base.entity.DataEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据质量管理-规则属性值
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_RULE_ATTR")
public class RuleAttr extends DataEntity<RuleAttr> {

    private static final long serialVersionUID = 1L;

    /**
     * 规则属性值ID
     */
    @TableId(value = "RULE_ATTR_ID", type = IdType.ID_WORKER)
    private Long ruleAttrId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 规则id
     */
    @TableField("RULE_ID")
    private Long ruleId;
    @TableField("CODE")
    private String code;
    @TableField("VALUE_STR")
    private String valueStr;

    @TableField("VALUE_RANK")
    private String valueRank;





    @Override
    protected Serializable pkVal() {
        return this.ruleAttrId;
    }

}
