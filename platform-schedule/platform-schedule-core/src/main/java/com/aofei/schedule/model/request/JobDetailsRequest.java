package com.aofei.schedule.model.request;

import com.aofei.base.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-11-18
 */
@Data
public class JobDetailsRequest extends BaseRequest<JobDetailsRequest> {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String schedName;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private Long organizerId;

    /**
     * 组织ID
     */
    @ApiModelProperty(hidden = true)
    private Long unitId;

    private String jobName;

    private String jobGroup;

    private List<String>  jobClassNames;



}
