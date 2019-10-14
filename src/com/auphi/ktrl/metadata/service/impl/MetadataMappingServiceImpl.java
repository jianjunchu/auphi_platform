package com.auphi.ktrl.metadata.service.impl;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.dao.SystemDao;
import com.auphi.ktrl.conn.util.ConnectionPool;
import com.auphi.ktrl.metadata.domain.MetadataMapping;
import com.auphi.ktrl.metadata.domain.MetadataMappingGroup;
import com.auphi.ktrl.metadata.service.MetadataMappingService;
import com.auphi.ktrl.system.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service("MetadataMappingService")
public class MetadataMappingServiceImpl implements MetadataMappingService {

    @Autowired
    SystemDao systemDao;

    @Override
    public PaginationSupport<MetadataMapping> getPage(MetadataMapping dto) throws SQLException {


        List<MetadataMapping> items = systemDao.queryForPage("metadataMapping.selectList", dto, dto.getStart(), dto.getLimit());
        Integer total = (Integer)systemDao.queryForObject("metadataMapping.selectCount",dto);
        PaginationSupport<MetadataMapping> page = new PaginationSupport<MetadataMapping>(items, total);
        return page;
    }

    @Override
    public void save(MetadataMapping object) {
        object.setId(ConnectionPool.nextId());
        this.systemDao.save("metadataMapping.insert",object);
    }


    @Override
    public int delete(Dto dto) {

        return this.systemDao.delete("metadataMapping.deleteByIds", dto);
    }

    @Override
    public int delete(Long id) {

        return this.systemDao.delete("metadataMapping.deleteById", id);
    }

    @Transactional
    @Override
    public void save(List<MetadataMapping> mappings) {
        for(MetadataMapping metadataMapping : mappings){
            metadataMapping.setId(ConnectionPool.nextId());
            save(metadataMapping);
        }
    }

    @Override
    public MetadataMapping get(Long id) {
        Dto dto = new BaseDto();
        dto.put("id", id);
        return (MetadataMapping)this.systemDao.queryForObject("metadataMapping.selectById", dto);
    }

    @Override
    public int updateDestTable(MetadataMapping ex) {
        return this.systemDao.update("metadataMapping.updateDestTable",ex);

    }

    @Override
    public int countSourceTable(MetadataMapping metadataMapping) {
        return (Integer)this.systemDao.queryForObject("metadataMapping.selectSourceTableCount",metadataMapping);
    }

    @Override
    public int update(MetadataMapping Object) {
        return this.systemDao.update("metadataMapping.updateById",Object);
    }
}
