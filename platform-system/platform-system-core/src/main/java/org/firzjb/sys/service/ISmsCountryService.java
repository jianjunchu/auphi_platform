package org.firzjb.sys.service;

import org.firzjb.sys.entity.SmsCountry;
import org.firzjb.sys.model.response.SmsCountryResponse;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.sys.entity.SmsCountry;
import org.firzjb.sys.model.response.SmsCountryResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2018-12-18
 */
public interface ISmsCountryService extends IService<SmsCountry> {

    List<SmsCountryResponse> getSmsCountrys();
}
