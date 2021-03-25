package org.firzjb.query.mapper;

import org.firzjb.base.annotation.ExtMyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.query.entity.DataLoadT;
import org.firzjb.query.model.request.DataLoadTRequest;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * 数据装载日志表
 */
@ExtMyBatisMapper
public interface DataLoadTMapper extends BaseMapper<DataLoadT> {


    List<DataLoadT> findBackupFrequencyList(Page<DataLoadT> page, DataLoadTRequest request);

    List<DataLoadT> findBackupFrequencyList(DataLoadTRequest request);

    List<DataLoadT> findBackupRecordChartDat(DataLoadTRequest request);
}
