package org.firzjb.datasource.model.request;

import org.firzjb.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * HADOOP管理
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@Data
public class HadoopRequest extends BaseRequest<HadoopRequest> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;
    @TableField("SERVER")
    private String server;
    @TableField("PORT")
    private Integer port;
    @TableField("USERID")
    private String userid;
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(hidden = true)
    private Long organizerId;
}
