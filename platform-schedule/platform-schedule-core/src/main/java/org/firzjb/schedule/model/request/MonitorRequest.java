package org.firzjb.schedule.model.request;

import org.firzjb.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class MonitorRequest extends BaseRequest {

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private Long organizerId;

    private String qrtzJobName;

    private String qrtzJobGroup;
    private String  searchtime;
    private String  startTime;
    private String  endTime;
    private String  status;

    private Date fireTime;

}
