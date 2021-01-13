package com.aofei.base.mapper;

import com.aofei.base.model.request.PageRequest;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * @auther 傲飞数据整合平台
 * @create 2018-09-14 20:21
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.mapper.BaseMapper<T>{

    /**
     * 分页查询数据
     * @param page 分页对象
     * @param entity 查询条件
     * @return
     */
    List<T> findList(Page<T> page, PageRequest entity);

    /**
     * 查询列表
     * @param entity 查询条件
     * @return
     */
    List<T> findList(PageRequest entity);
}
