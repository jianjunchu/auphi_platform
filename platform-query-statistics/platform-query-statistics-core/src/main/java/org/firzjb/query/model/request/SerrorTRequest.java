package org.firzjb.query.model.request;

import org.firzjb.base.model.request.PageRequest;
import lombok.Data;

/**
 * 服务端错误记录表
 */
@Data
public class SerrorTRequest extends PageRequest {

    /**
     * 受理号
     */
    private String acceptNo;

    /**
     * 上传单位代码
     */
    private String unitNo;

    /**
     * 所属备份文件名
     */
    private String backupFile;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorDesc;

    /**
     * 操作
     */
    private String currOperation;

    /**
     * 处理状态
     * ‘0’未处理
     * ‘1’已下载
     *   缺省为‘0’
     */
    private String dealFlag;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;


}
