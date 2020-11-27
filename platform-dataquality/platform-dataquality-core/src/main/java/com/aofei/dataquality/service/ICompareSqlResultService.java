package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.CompareSqlResult;
import com.aofei.dataquality.model.request.CompareSqlResultRequest;
import com.aofei.dataquality.model.response.CompareSqlResultResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-13
 */
public interface ICompareSqlResultService extends IService<CompareSqlResult> {

    /**
     * 获取  列表
     * @param page
     * @param request
     * @return
     */
    Page<CompareSqlResultResponse> getPage(Page<CompareSqlResult> page, CompareSqlResultRequest request);

    boolean execCompareSql(Long compareSqlId);
}
