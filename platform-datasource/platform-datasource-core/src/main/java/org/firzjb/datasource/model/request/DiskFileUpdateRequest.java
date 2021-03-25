package org.firzjb.datasource.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 文件或文件夹修改名称
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
public class DiskFileUpdateRequest  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private String organizerName;

    /**
     * 原文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String name;

    /**
     * 文件的路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 新的文件名称
     */
    @ApiModelProperty(value = "新文件名称")
    private String newName;



}
