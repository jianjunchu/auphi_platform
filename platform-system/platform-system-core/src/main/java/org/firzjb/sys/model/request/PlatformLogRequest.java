package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author Tony
 * @since 2018-09-15
 */
@Data
public class PlatformLogRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long platformLogId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 模块
     */
    private String module;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * 请求参数
     */
    private String params;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 创建时间
     */
    private Date createDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

    /**
     * 是否删除  1：已删除  0：正常
     */
    private Integer delFlag;


    /**
     * 创建时间
     */
    private String type;

}
