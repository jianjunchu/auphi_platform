package org.firzjb.query.service.impl;

import org.firzjb.query.model.request.InfoTRequest;
import org.firzjb.query.service.IInfoTService;
import org.firzjb.query.utils.DatabaseLoader;
import org.firzjb.utils.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class InfoTService  implements IInfoTService {

    @Override
    public Page getPage(Page page, InfoTRequest request) throws KettleException, SQLException {
        DatabaseLoader loader = new DatabaseLoader();

        StringBuffer sql = new StringBuffer("select ")
                .append("    a.unit_no AS unitNo, a.accept_no AS acceptNo, a.id_no AS idNo, a.name AS name, a.backup_file AS backupFile, a.create_time AS createTime, a.update_time AS updateTime ")
                .append(" from s_info_t a where 1 = 1" );

        if(!StringUtils.isEmpty(request.getUnitNo())){
            sql.append(" and a.unit_no = '").append(request.getUnitNo()).append("'");
        }

        if(!StringUtils.isEmpty(request.getAcceptNo())){
            sql.append(" and a.accept_no like '%").append(request.getAcceptNo()).append("%'");
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
