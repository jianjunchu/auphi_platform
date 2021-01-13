package com.aofei.datasource.model.response;

import lombok.Data;

/**
 * <p>
 * 数据库响应类型
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */

@Data
public class DatabaseResponse  {

    private static final long serialVersionUID = 1L;


    /**
     * 数据库ID
     */
    private Long databaseId;

    /**
     * 数据库连接名称
     */
    private String name;

    /**
     * 数据库类型
     */
    private Integer databaseTypeId;

    /**
     * 数据库类型名称 R_DATABASE_TYPE
     */
    private String databaseTypeName;

    /**
     * 数据库连接类型ID
     */
    private Integer databaseContypeId;

    /**
     * 数据库连接类型名称 R_DATABASE_CONTYPE
     */
    private String databaseContypeName;

    /**
     * 数据库主机名称或者IP地址
     */
    private String hostName;

    /**
     *数据库名称
     */
    private String databaseName;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 数据库登录用户名
     */
    private String username;

    /**
     * 数据库登录密码
     */
    private String password;

    /**
     * 数据库
     */
    private String servername;

    /**
     *
     */
    private String dataTbs;

    private String indexTbs;

}
