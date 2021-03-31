package org.firzjb.datasource.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiskFileResponse {

    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "名称")
    private String filename;
    @ApiModelProperty(value = "是否是文件夹 0:否 1:是")
    private Integer isdir;
    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "是否还有文件")
    private Boolean leaf;

    @ApiModelProperty(value = "最后修改时间")
    private Long lastModified;

    @ApiModelProperty(value = "子文件夹")
    private List<DiskFileResponse> children = new ArrayList<>();
}