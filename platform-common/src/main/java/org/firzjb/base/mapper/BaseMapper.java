package org.firzjb.base.mapper;

import org.firzjb.base.model.request.PageRequest;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.base.model.request.PageRequest;

import java.util.List;

/**
 * @auther 制证数据实时汇聚系统
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
