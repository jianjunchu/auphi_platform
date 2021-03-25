package org.firzjb.sys.model.response;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
public class ConfigResponse  {

    private static final long serialVersionUID = 1L;

    private Long configId;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;

    /**
     * 创建时间
     */
    private Date createTime;

}
