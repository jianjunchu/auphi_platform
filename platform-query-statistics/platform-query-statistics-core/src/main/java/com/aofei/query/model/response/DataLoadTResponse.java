package com.aofei.query.model.response;

import lombok.Data;

/**
 * 数据装载日志表
 */
@Data
public class DataLoadTResponse  {

    /**
     * 备份批次号
     */
    private String batchNo;

    /**
     * 发送单位代码
     */
    private String unitNo;

    /**
     * 时间起点
     */
    private String backupStart;

    /**
     * 时间终点
     */
    private String backupEnd;

    /**
     * 抽取时间
     */
    private String backupTime;




    /**
     * 待抽取数量
     * 缺省为0
     */
    private Long backupCount;

    /**
     * 实际抽取数量
     * 缺省为0
     */
    private Long backupValidCount;

    /**
     * 抽取错误数据数量
     * 缺省为0
     */
    private Long backupInvalidCount;

    /**
     * 装载时间
     */
    private String loadTime;

    /**
     * 待装载数量
     * 缺省为0
     */
    private Long loadCount;

    /**
     * 实际装载数量
     * 缺省为0
     */
    private Long loadValidCount;

    /**
     * 装载错误数据数量
     * 缺省为0
     */
    private Long loadInvalidCount;

    /**
     * 装载错误数据数量
     * 缺省为0
     */
    private String business;

    /**
     * ‘身份证数据’
     */
    private String operation;


}
