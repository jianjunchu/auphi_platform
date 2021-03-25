package org.firzjb.schedule.entity;

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
 * 调度分组
 * </p>
 *
 * @author Tony
 * @since 2018-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("QRTZ_GROUP")
public class Group extends DataEntity<Group> {

    private static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    @TableId(value = "ID_GROUP", type = IdType.ID_WORKER_STR)
    private String groupId;
    /**
     * 组织ID
     */
    @TableField("ORGANIZER_ID")
    private Long organizerId;

    /**
     * 部门
     */
    @TableField("UNIT_ID")
    private Long unitId;

    @TableField(exist = false)
    private String unitCode;

    @TableField(exist = false)
    private String unitName;
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
