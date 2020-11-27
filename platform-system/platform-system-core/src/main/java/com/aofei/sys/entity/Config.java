package com.aofei.sys.entity;

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
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("SYS_CONFIG")
public class Config extends DataEntity<Config> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CONFIG_ID", type = IdType.ID_WORKER)
    private Long configId;
    /**
     * 名称
     */
    @TableField("NAME")
    private String name;
    /**
     * 值
     */
    @TableField("VALUE")
    private String value;



    @Override
    protected Serializable pkVal() {
        return this.configId;
    }

}
