package org.firzjb.dataquality.model.request;

import org.firzjb.base.entity.DataEntity;
import org.firzjb.base.model.request.BaseRequest;
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
 * 数据质量规则分组
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
@Data
public class RuleGroupRequest extends BaseRequest<RuleGroupRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    @ApiModelProperty(value = "分组Id")
    private Long groupId;
    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true,value = "组织ID")
    private Long organizerId;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 分组描述
     */
    @ApiModelProperty(value = "分组描述")
    private String description;


}
