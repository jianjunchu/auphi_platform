package com.aofei.base.mapper;

import com.aofei.base.model.request.PageRequest;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * @auther Tony
 * @create 2018-09-14 20:21
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.mapper.BaseMapper<T>{

    List<T> findList(Page<T> page, PageRequest entity);

    List<T> findList(PageRequest entity);
}
