package org.firzjb.query.model.response;

import lombok.Data;

/**
 * 装载信息记录表
 * 备份数据记录的基本情况
 */
@Data
public class InfoTResponse  {


    /**
     * 单位代码
     */
    private String unitNo;


    /**
     * 受理号
     */
    private String acceptNo;

    /**
     * 身份证号码
     */
    private String idNo;


    /**
     * 姓名
     */
    private String name;

    /**
     * 备份文件名
     */
    private String backupFile;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;


}
