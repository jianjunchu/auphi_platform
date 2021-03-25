package org.firzjb.datasource.entity;

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
 * <p>
 * 本地数据库
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("R_DATABASE")
public class DatabaseEntity extends Model<DatabaseEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库ID
     */
    @TableId(value = "ID_DATABASE", type = IdType.ID_WORKER)
    private Long databaseId;

    /**
     * 数据库连接名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 数据库类型
     */
    @TableField("ID_DATABASE_TYPE")
    private Integer databaseTypeId;

    /**
     * 数据库类型名称 R_DATABASE_TYPE
     */
    @TableField(exist = false)
    private String databaseTypeName;

    /**
     * 数据库连接类型ID
     */
    @TableField("ID_DATABASE_CONTYPE")
    private Integer databaseContypeId;

    /**
     * 数据库连接类型名称 R_DATABASE_CONTYPE
     */
    @TableField(exist = false)
    private String databaseContypeName;

    /**
     * 数据库主机名称或者IP地址
     */
    @TableField("HOST_NAME")
    private String hostName;

    /**
     *数据库名称
     */
    @TableField("DATABASE_NAME")
    private String databaseName;

    /**
     * 端口号
     */
    @TableField("PORT")
    private Integer port;

    /**
     * 数据库登录用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 数据库登录密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 数据库
     */
    @TableField("SERVERNAME")
    private String servername;

    /**
     *
     */
    @TableField("DATA_TBS")
    private String dataTbs;

    @TableField("INDEX_TBS")
    private String indexTbs;


    @Override
    protected Serializable pkVal() {
        return this.databaseId;
    }

}
