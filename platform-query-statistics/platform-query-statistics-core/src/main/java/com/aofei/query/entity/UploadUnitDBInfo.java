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

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("upload_unit_DB_info")
public class UploadUnitDBInfo extends Model {

    /**
     * 上传单位代码
     */
    @TableId(value = "unit_no", type = IdType.INPUT)
    private String unitNo;


    /**
     * 数据库类型
     * L:本地
     * Y：异地
     * G：港澳台
     */
    @TableField(value = "db_type")
    private String dbType;

    /**
     * 数据库服务名
     */
    @TableField(value = "sid")
    private String sid;


    /**
     * 数据库主机名/IP
     */
    @TableField(value = "db_host")
    private String dbHost;

    /**
     * 数据库用户名（密文）
     */
    @TableField(value = "db_user")
    private String dbUser;

    /**
     * 数据库密码（密文）
     */
    @TableField(value = "db_pwd")
    private String dbPwd;

    /**
     * 端口号
     */
    @TableField(value = "db_port")
    private String dbPort;

    /**
     * 是否需要备份，缺省是0
     * ‘0’:是
     * ‘1’:否
     */
    @TableField(value = "is_backup")
    private String isBackup;

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
