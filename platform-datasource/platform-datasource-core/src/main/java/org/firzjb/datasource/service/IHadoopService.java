package org.firzjb.datasource.service;

import org.firzjb.datasource.entity.Hadoop;
import org.firzjb.datasource.model.request.HadoopRequest;
import org.firzjb.datasource.model.response.HadoopResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.datasource.model.request.HadoopRequest;
import org.firzjb.datasource.model.response.HadoopResponse;

import java.util.List;

/**
 * <p>
 * HADOOP管理 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
public interface IHadoopService extends IService<Hadoop> {

    Page<HadoopResponse> getPage(Page<Hadoop> page, HadoopRequest request);

    List<HadoopResponse> getHadoops(HadoopRequest request);

    HadoopResponse save(HadoopRequest request);

    HadoopResponse update(HadoopRequest request);

    int del(Long id);

    HadoopResponse get(Long id);
}
