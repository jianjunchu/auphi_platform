package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.CompareSqlField;
import com.aofei.dataquality.model.request.CompareSqlRequest;
import com.aofei.dataquality.model.response.CompareSqlFieldResponse;
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
public interface ICompareSqlFieldService extends IService<CompareSqlField> {

    List<CompareSqlFieldResponse> getCompareFields(CompareSqlRequest request);
}
