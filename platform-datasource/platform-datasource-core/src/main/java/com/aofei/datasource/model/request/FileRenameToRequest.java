package com.aofei.datasource.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileRenameToRequest {

    @ApiModelProperty(value = "要移动的文件")
    private String startFile;

    @ApiModelProperty(value = "移动的位置(目录)")
    private String endPath;

    @ApiModelProperty(hidden = true)
    private Long organizerId;
}
