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
 * 装载信息记录表
 * 备份数据记录的基本情况
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("s_info_t")
public class InfoT extends Model {


    /**
     * 单位代码
     */
    @TableId(value = "unit_no", type = IdType.INPUT)
    private String unitNo;


    /**
     * 受理号
     */
    @TableId(value = "accept_no", type = IdType.INPUT)
    private String acceptNo;

    /**
     * 身份证号码
     */
    @TableField(value = "id_no")
    private String idNo;


    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 备份文件名
     */
    @TableField(value = "backup_file")
    private String backupFile;


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
        return this.unitNo;
    }
}
