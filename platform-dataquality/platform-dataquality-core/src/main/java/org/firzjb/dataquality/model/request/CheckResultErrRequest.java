package org.firzjb.dataquality.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;

/**
 * <p>
 * 数据质量管理-错误字段值记录
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Data
public class CheckResultErrRequest extends BaseRequest<CheckResultErrRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 错误记录id
     */
    private Long checkResultErrId;
    /**
     * 组织ID
     */
    private Long organizerId;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 结果id
     */
    private Long checkResultId;
    /**
     * 错误数据记录
     */
    private String errorValue;
    /**
     * 错误描述
     */
    private String errorDesc;
    /**
     * 修改建议
     */
    private String errorSuggestion;

    /**
     * 创建者ID
     */
    private String createUser;
    /**
     * 创建者ID
     */
    private String updateUser;


}
