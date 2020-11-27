package com.aofei.sys.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.sys.entity.Config;
import com.aofei.sys.mapper.ConfigMapper;
import com.aofei.sys.service.IConfigService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
@Service
public class ConfigService extends BaseService<ConfigMapper, Config> implements IConfigService {



    @Override
    public String getConfigByName(String name) {

        List<Config> list = selectList(new EntityWrapper<Config>().eq("NAME",name).eq("DEL_FLAG",0));
        if(list!=null && list.size()>0){
            return list.get(0).getValue();
        }
        return "";
    }


}
