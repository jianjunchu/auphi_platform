package org.firzjb.query.model.response;

import lombok.Data;

@Data
public class UploadUnitDBInfoResponse  {

    /**
     * 上传单位代码
     */
    private String unitNo;


    /**
     * 数据库类型
     * L:本地
     * Y：异地
     * G：港澳台
     */
    private String dbType;

    /**
     * 数据库服务名
     */
    private String sid;


    /**
     * 数据库主机名/IP
     */
    private String dbHost;

    /**
     * 数据库用户名（密文）
     */
    private String dbUser;

    /**
     * 数据库密码（密文）
     */
    private String dbPwd;

    /**
     * 端口号
     */
    private String dbPort;

    /**
     * 是否需要备份，缺省是0
     * ‘0’:是
     * ‘1’:否
     */
    private String isBackup;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;



}
