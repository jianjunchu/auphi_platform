package com.aofei.query.model.request;

import com.aofei.base.model.request.PageRequest;
import lombok.Data;

/**
 * 数据抽取日志
 */
@Data
public class DataExactTRequest extends PageRequest {

    /**
     * 受理号
     */
    private String acceptNo;

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
     * 装载错误数据数量
     * 缺省为0
     */
    private String business;

    /**
     * ‘身份证数据’
     */
    private String operation;


}
