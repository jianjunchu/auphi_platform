package org.firzjb.query.model.request;


import org.firzjb.base.model.request.PageRequest;
import lombok.Data;

/**
 * 数据接收记录表
 */
@Data
public class BatchRevTRequest extends PageRequest {

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
