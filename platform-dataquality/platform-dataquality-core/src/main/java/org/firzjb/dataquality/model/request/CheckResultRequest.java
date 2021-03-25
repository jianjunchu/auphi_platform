package org.firzjb.dataquality.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据质量管理-规则检查结果
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Data
public class CheckResultRequest extends BaseRequest<CheckResultRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 结果id
     */
    private Long checkResultId;

    private Long ruleId;

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
     * 创建者ID
     */
    private String createUser;
    /**
     * 创建者ID
     */
    private String updateUser;


    private List<CheckResultErrRequest> errs;

}
