package org.firzjb.dataquality.service;

import org.firzjb.dataquality.entity.CompareSqlResult;
import org.firzjb.dataquality.model.request.CompareSqlResultRequest;
import org.firzjb.dataquality.model.response.CompareSqlResultResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.dataquality.entity.CompareSqlResult;
import org.firzjb.dataquality.model.request.CompareSqlResultRequest;
import org.firzjb.dataquality.model.response.CompareSqlResultResponse;

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
