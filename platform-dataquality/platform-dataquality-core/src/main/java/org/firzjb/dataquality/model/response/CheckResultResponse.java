package org.firzjb.dataquality.model.response;

import lombok.Data;

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
public class CheckResultResponse  {

    private static final long serialVersionUID = 1L;

    /**
     * 结果id
     */
    private Long checkResultId;

    private Long ruleId;

    /**
     * 规则描述
     */
    private String description;

    private String ruleGroupId;

    private String ruleGroupName;

    /**
     * 组织ID
     */
    private Long organizerId;
    /**
     * 检查总记录数
     */
    private Long totalCheckedNum;
    /**
     * 通过记录数
     */
    private Long passedNum;
    /**
     * 未通过记录数
     */
    private Long notPassedNum;
    /**
     * 检查开始时间
     */
    private Date checkStartTime;
    /**
     * 检查结束时间
     */
    private Date checkEndTime;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 最后修改用户
     */
    private String updateUser;
    /**
     * 创建时间
     */
    private Date createTime;

}
