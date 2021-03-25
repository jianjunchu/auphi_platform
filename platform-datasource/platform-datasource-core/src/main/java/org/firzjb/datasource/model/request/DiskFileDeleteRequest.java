package org.firzjb.datasource.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 删除文件或文件夹
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
public class DiskFileDeleteRequest  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 要删除的文件路径(文件的路径和名称 '/'开头）
     */
    @ApiModelProperty(value = "路径(文件的路径和名称 '/'开头）")
    private String path;




}
