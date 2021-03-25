package org.firzjb.datasource.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 本地数据库请求参数
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
public class DatabaseRequest extends BaseRequest<DatabaseRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 数据库名称
     */
    @ApiModelProperty(value = "数据库名称")
    private String name;

    /**
     * 资源库名称
     */
    @ApiModelProperty(value = "资源库名称")
    private String repository;




}
