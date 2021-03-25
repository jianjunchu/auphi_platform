package org.firzjb.sys.model.response;

import lombok.Data;

/**
 * <p>
 *  短信注册 国家代码对象
 * </p>
 *
 * @author Tony
 * @since 2018-12-18
 */
@Data
public class SmsCountryResponse  {

    private static final long serialVersionUID = 1L;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 国家名称
     */
    private String countryName;


}
