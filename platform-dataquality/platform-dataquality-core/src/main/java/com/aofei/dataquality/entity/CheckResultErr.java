package com.aofei.dataquality.entity;

import com.aofei.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据质量管理-错误字段值记录
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_CHECK_RESULT_ERR")
public class CheckResultErr extends DataEntity<CheckResultErr> {

    private static final long serialVersionUID = 1L;

    /**
     * 错误记录id
     */
    @TableId(value = "CHECK_RESULT_ERR_ID", type = IdType.ID_WORKER)
    private Long checkResultErrId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 结果id
     */
    @TableField("CHECK_RESULT_ID")
    private Long checkResultId;
    /**
     * 错误数据记录
     */
    @TableField("ERROR_VALUE")
    private String errorValue;
    /**
     * 错误描述
     */
    @TableField("ERROR_DESC")
    private String errorDesc;
    /**
     * 修改建议
     */
    @TableField("ERROR_SUGGESTION")
    private String errorSuggestion;



    @Override
    protected Serializable pkVal() {
        return this.checkResultErrId;
    }

}
