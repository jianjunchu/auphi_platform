package org.firzjb.base.service.impl;


import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.firzjb.base.mapper.BaseMapper;

import java.util.List;

public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T>  {

    /**
     * 将list的数据封装到Page对象
     * @param source
     * @param destinationClass
     * @return
     */
    public Page convert(Page source, Class destinationClass) {
        List result = BeanCopier.copy(source.getRecords(), destinationClass);
        source.setRecords(result);
        return source;
    }


}
