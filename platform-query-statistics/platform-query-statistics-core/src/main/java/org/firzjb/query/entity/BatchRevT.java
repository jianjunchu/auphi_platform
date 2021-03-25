package org.firzjb.query.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 数据接收记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchRevT extends Model {

    /**
     * 备份批次号
     */
    @TableId(value = "batch_no", type = IdType.INPUT)
    private String batchNo;

    /**
     * 文件个数
     */
    @TableField(value = "file_count")
    private Integer fileCount;

    /**
     * 接收时间
     */
    @TableField(value = "rev_time")
    private String revTime;

    @Override
    protected Serializable pkVal() {
        return this.batchNo;
    }
}
