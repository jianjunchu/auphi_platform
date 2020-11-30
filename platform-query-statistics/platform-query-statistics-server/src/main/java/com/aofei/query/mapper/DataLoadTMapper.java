package com.aofei.query.mapper;

import com.aofei.base.annotation.ExtMyBatisMapper;
import com.aofei.base.mapper.BaseMapper;
import com.aofei.query.entity.DataLoadT;
import com.aofei.query.model.request.DataLoadTRequest;
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
