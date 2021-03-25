package org.firzjb.datasource.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 文件移动
 * </p>
 *
 * @author Tony
 * @since 2018-09-22
 */
@Data
public class FileRenameToRequest {

    /**
     * 要移动的文件
     */
    @ApiModelProperty(value = "要移动的文件")
    private String startFile;

    /**
     * 文件移动位置
     */
    @ApiModelProperty(value = "移动的位置(目录)")
    private String endPath;

    @ApiModelProperty(hidden = true)
    private Long organizerId;
}
