package org.firzjb.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditUserPasswordRequest {

    /**
     * 主键
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户新密码")
    private String newPassword;

    @ApiModelProperty(value = "用户新密码")
    private String originalPassword;

    @ApiModelProperty(value = "用户确认新密码")
    private String renewPassword;
}
