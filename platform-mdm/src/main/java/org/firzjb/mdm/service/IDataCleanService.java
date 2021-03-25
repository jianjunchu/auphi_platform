package org.firzjb.mdm.service;

import org.firzjb.mdm.entity.DataClean;
import org.firzjb.mdm.model.request.DataCleanRequest;
import org.firzjb.mdm.model.response.DataCleanResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.mdm.entity.DataClean;
import org.firzjb.mdm.model.request.DataCleanRequest;
import org.firzjb.mdm.model.response.DataCleanResponse;

import java.util.List;

/**
 * <p>
 * 数据映射 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
public interface IDataCleanService extends IService<DataClean> {

    Page<DataCleanResponse> getPage(Page<DataClean> page, DataCleanRequest request);

    List<DataCleanResponse> getDataCleans(DataCleanRequest request);

    DataCleanResponse save(DataCleanRequest request);

    DataCleanResponse update(DataCleanRequest request);

    int del(Long id);

    DataCleanResponse get(Long id);
}
