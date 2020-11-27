package com.aofei.query.entity;

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
 * 错误记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("s_error_t")
public class ErrorT extends Model {

    /**
     * 受理号
     */
    @TableId(value = "accept_no", type = IdType.INPUT)
    private String acceptNo;

    /**
     * 上传单位代码
     */
    @TableField(value = "unit_no")
    private String unitNo;

    /**
     * 所属备份文件名
     */
    @TableField(value = "backup_file")
    private String backupFile;

    /**
     * 错误代码
     */
    @TableField(value = "error_code")
    private String errorCode;

    /**
     * 错误描述
     */
    @TableField(value = "error_desc")
    private String errorDesc;

    /**
     * 操作
     */
    @TableField(value = "curr_operation")
    private String currOperation;

    /**
     * 处理状态
     * ‘0’未处理
     * ‘1’已下载
     *   缺省为‘0’
     */
    @TableField(value = "deal_flag")
    private String dealFlag;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private String updateTime;

    @Override
    protected Serializable pkVal() {
        return this.acceptNo;
    }
}
