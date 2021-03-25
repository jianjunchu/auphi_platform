package org.firzjb.dataquality.service;

import org.firzjb.dataquality.entity.CompareSqlField;
import org.firzjb.dataquality.model.request.CompareSqlRequest;
import org.firzjb.dataquality.model.response.CompareSqlFieldResponse;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataquality.entity.CompareSqlField;
import org.firzjb.dataquality.model.request.CompareSqlRequest;
import org.firzjb.dataquality.model.response.CompareSqlFieldResponse;

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
