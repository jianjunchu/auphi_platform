package org.firzjb.datasource.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 文件查询
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
public class DiskFileRequest extends BaseRequest<DiskFileRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private String organizerName;

    /**
     * 文件操作路径
     */
    @ApiModelProperty(value = "路径")
    private String path;




}
