package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Data
public class ConfigRequest extends BaseRequest<ConfigRequest> {

    private static final long serialVersionUID = 1L;

    private Long configId;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;


}
