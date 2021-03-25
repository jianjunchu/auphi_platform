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

/**
 * <p>
 * 数据质量规则分组
 * </p>
 *
 * @author Tony
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("DATA_QUALITY_RULE_GROUP")
public class RuleGroup extends DataEntity<RuleGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    @TableId(value = "GROUP_ID", type = IdType.ID_WORKER)
    private Long groupId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;
    /**
     * 分组名称
     */
    @TableField("GROUP_NAME")
    private String groupName;
    /**
     * 分组描述
     */
    @TableField("DESCRIPTION")
    private String description;



    @Override
    protected Serializable pkVal() {
        return this.groupId;
    }

}
