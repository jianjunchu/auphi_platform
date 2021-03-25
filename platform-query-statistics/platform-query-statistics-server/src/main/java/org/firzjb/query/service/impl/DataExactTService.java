package org.firzjb.query.service.impl;

import org.firzjb.query.model.request.DataExactTRequest;
import org.firzjb.query.service.IDataExactTService;
import org.firzjb.query.utils.DatabaseLoader;
import org.firzjb.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DataExactTService   implements IDataExactTService {


    @Override
    public Page getPage(Page page, DataExactTRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select   a.batch_no AS batchNo, a.unit_no AS unitNo, a.backup_start  AS  backupStart, a.backup_end  AS  backupEnd, a.backup_time  AS backupTime, a.backup_count AS backupCount, a.backup_valid_count AS backupValidCount, a.backup_invalid_count AS backupInvalidCount, a.business  AS business, a.operation  AS operation from l_data_exact_t a where 1 = 1" );//where a.batch_no like '%"+request.getBatchNo()+"%'";
        if(!StringUtils.isEmpty(request.getBatchNo())){
            sql.append(" and a.batch_no like '%").append(request.getBatchNo()).append("%'");
        }
        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.backup_time",request));

        }
        return loader.getPage(page,sql.toString());

    }
}
