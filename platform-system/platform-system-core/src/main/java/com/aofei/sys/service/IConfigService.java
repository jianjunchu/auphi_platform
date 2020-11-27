package com.aofei.sys.service;

import com.aofei.sys.entity.Config;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
public interface IConfigService extends IService<Config> {


    /**
     * 根据名称查询值
     * @param name
     * @return
     */
    String getConfigByName(String name);
}
