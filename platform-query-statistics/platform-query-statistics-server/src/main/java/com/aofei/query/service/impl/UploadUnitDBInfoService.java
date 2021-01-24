package com.aofei.query.service.impl;

import com.aofei.query.model.request.UploadUnitDBInfoRequest;
import com.aofei.query.service.IUploadUnitDBInfoService;
import com.aofei.query.utils.DatabaseLoader;
import com.aofei.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UploadUnitDBInfoService  implements IUploadUnitDBInfoService {

    @Override
    public Page getPage(Page page, UploadUnitDBInfoRequest request) throws KettleException, SQLException {

        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
                .append("  a.unit_no AS unitNo, a.db_type AS dbType, a.sid AS sid, a.db_host AS dbHost, a.db_user AS dbUser, a.db_pwd AS dbPwd, a.db_port AS dbPort, a.is_backup AS isBackup, a.create_time AS createTime, a.update_time AS updateTime ")
                .append(" from upload_unit_DB_info a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getSearch_time())
                && "create_time".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.create_time",request));

        }
        if(!StringUtils.isEmpty(request.getSearch_time())
                && "update_time".equalsIgnoreCase(request.getSearch_time())
                && !StringUtils.isEmpty(request.getSearch_satrt())
                && !StringUtils.isEmpty(request.getSearch_end())){
            sql.append(" AND ").append(loader.getDateBetween("a.update_time",request));

        }
        return loader.getPage(page,sql.toString());


    }
}
