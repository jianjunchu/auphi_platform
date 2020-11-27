package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.CompareSql;
import com.aofei.dataquality.model.request.CompareSqlRequest;
import com.aofei.dataquality.model.response.CompareSqlResponse;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
public interface ICompareSqlService extends IService<CompareSql> {

    /**
     * 保存 稽核 信息
     * @param request
     * @return
     */
    CompareSqlResponse save(CompareSqlRequest request);

    /**
     * 更新 稽核 信息
     * @param request
     * @return
     */
    CompareSqlResponse update(CompareSqlRequest request);

    /**
     * 根据Id 查询 稽核
     * @param compareSqlId
     * @return
     */
    CompareSqlResponse get(Long compareSqlId);
    /**
     * 根据Id 删除 稽核
     * @param compareSqlId
     * @return
     */
    int del(Long compareSqlId);

    List<CompareSqlResponse> getCompareSqls(CompareSqlRequest request);


}
