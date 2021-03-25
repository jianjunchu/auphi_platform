package org.firzjb.dataquality.entity;

import org.firzjb.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 数据质量管理-规则检查结果
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_CHECK_RESULT")
public class CheckResult extends DataEntity<CheckResult> {

    private static final long serialVersionUID = 1L;

    /**
     * 结果id
     */
    @TableId(value = "CHECK_RESULT_ID", type = IdType.ID_WORKER)
    private Long checkResultId;

    /**
     * 结果id
     */
    @TableField(value = "RULE_ID")
    private Long ruleId;

    /**
     * 规则描述
     */
    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private String ruleGroupId;

    @TableField(exist = false)
    private String ruleGroupName;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 检查总记录数
     */
    @TableField("TOTAL_CHECKED_NUM")
    private Long totalCheckedNum;
    /**
     * 通过记录数
     */
    @TableField("PASSED_NUM")
    private Long passedNum;
    /**
     * 未通过记录数
     */
    @TableField("NOT_PASSED_NUM")
    private Long notPassedNum;
    /**
     * 检查开始时间
     */
    @TableField("CHECK_START_TIME")
    private Date checkStartTime;
    /**
     * 检查结束时间
     */
    @TableField("CHECK_END_TIME")
    private Date checkEndTime;



    @Override
    protected Serializable pkVal() {
        return this.checkResultId;
    }

}
