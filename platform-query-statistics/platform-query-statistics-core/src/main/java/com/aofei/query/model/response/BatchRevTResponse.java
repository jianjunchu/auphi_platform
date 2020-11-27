package com.aofei.query.model.response;


import lombok.Data;

/**
 * 数据接收记录表
 */
@Data
public class BatchRevTResponse  {

    /**
     * 备份批次号
     */
    private String batchNo;

    /**
     * 文件个数
     */
    private Integer fileCount;

    /**
     * 接收时间
     */
    private String revTime;

}
