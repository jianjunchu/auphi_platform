package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Tony
 * @since 2018-12-18
 */
@Data
public class SmsCountryRequest extends BaseRequest<SmsCountryRequest> {

    private static final long serialVersionUID = 1L;

    private String countryCode;

    private String countryName;



}
