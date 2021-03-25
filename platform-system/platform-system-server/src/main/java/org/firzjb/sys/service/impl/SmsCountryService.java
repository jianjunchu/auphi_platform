package org.firzjb.sys.service.impl;

import org.firzjb.base.service.impl.BaseService;
import org.firzjb.sys.entity.SmsCountry;
import org.firzjb.sys.mapper.SmsCountryMapper;
import org.firzjb.sys.model.request.SmsCountryRequest;
import org.firzjb.sys.model.response.SmsCountryResponse;
import org.firzjb.sys.service.ISmsCountryService;
import org.firzjb.utils.BeanCopier;
import org.firzjb.sys.mapper.SmsCountryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-12-18
 */
@Service
public class SmsCountryService extends BaseService<SmsCountryMapper, SmsCountry> implements ISmsCountryService {

    @Override
    public List<SmsCountryResponse> getSmsCountrys() {
        List<SmsCountry> list = baseMapper.findList(new SmsCountryRequest());
        return BeanCopier.copy(list, SmsCountryResponse.class);
    }
}
