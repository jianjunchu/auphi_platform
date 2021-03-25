package org.firzjb.datasource.model.response;

import org.firzjb.base.entity.DataEntity;
import org.firzjb.base.model.response.BaseResponse;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * FTP管理
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@Data
public class FtpResponse extends BaseResponse{

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_FTP", type = IdType.ID_WORKER)
    private Long ftpId;
    @TableField("NAME")
    private String name;
    @TableField("HOST_NAME")
    private String hostName;
    @TableField("PORT")
    private Integer port;
    @TableField("USERNAME")
    private String username;
    @TableField("PASSWORD")
    private String password;



}
