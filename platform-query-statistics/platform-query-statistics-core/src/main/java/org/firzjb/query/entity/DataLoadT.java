package org.firzjb.query.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 数据装载日志表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DataLoadT extends Model {

    /**
     * 备份批次号
     */
    @TableId(value = "batch_no", type = IdType.INPUT)
    private String batchNo;

    /**
     * 发送单位代码
     */
    @TableField(value = "unit_no")
    private String unitNo;

    /**
     * 时间起点
     */
    @TableField(value = "backup_start")
    private String backupStart;

    /**
     * 时间终点
     */
    @TableField(value = "backup_end")
    private String backupEnd;

    /**
     * 抽取时间
     */
    @TableField(value = "backup_time")
    private String backupTime;

    /**
     * 待抽取数量
     * 缺省为0
     */
    @TableField(value = "backup_count")
    private Long backupCount;

    /**
     * 实际抽取数量
     * 缺省为0
     */
    @TableField(value = "backup_valid_count")
    private Long backupValidCount;

    /**
     * 抽取错误数据数量
     * 缺省为0
     */
    @TableField(value = "backup_invalid_count")
    private Long backupInvalidCount;

    /**
     * 装载时间
     */
    @TableField(value = "load_time")
    private String loadTime;

    /**
     * 待装载数量
     * 缺省为0
     */
    @TableField(value = "load_count")
    private Long loadCount;

    /**
     * 实际装载数量
     * 缺省为0
     */
    @TableField(value = "load_valid_count")
    private Long loadValidCount;

    /**
     * 装载错误数据数量
     * 缺省为0
     */
    @TableField(value = "load_invalid_count")
    private Long loadInvalidCount;

    /**
     * 装载错误数据数量
     * 缺省为0
     */
    @TableField(value = "business")
    private String business;

    /**
     * ‘身份证数据’
     */
    @TableField(value = "operation")
    private String operation;

    @Override
    protected Serializable pkVal() {
        return this.batchNo;
    }
}