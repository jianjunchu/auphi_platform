package org.firzjb.datasource.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DiskFileTreeResponse {

    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "名称")
    private String filename;
}
