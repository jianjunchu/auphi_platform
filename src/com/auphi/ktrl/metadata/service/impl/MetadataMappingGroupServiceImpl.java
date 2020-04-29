package com.auphi.ktrl.metadata.service.impl;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.dao.SystemDao;
import com.auphi.ktrl.conn.util.ConnectionPool;
import com.auphi.ktrl.metadata.domain.MetadataMappingGroup;
import com.auphi.ktrl.metadata.service.MetadataMappingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service("MetadataMappingGroupService")
public class MetadataMappingGroupServiceImpl implements MetadataMappingGroupService {

    @Autowired
    SystemDao systemDao;

    @Override
    public PaginationSupport<MetadataMappingGroup> getPage(MetadataMappingGroup dto) throws SQLException {

        List<MetadataMappingGroup> items = systemDao.queryForPage("metadataMappingGroup.selectList", dto,dto.getStart(),dto.getLimit());
        Integer total = (Integer)systemDao.queryForObject("metadataMappingGroup.selectCount",dto);
        PaginationSupport<MetadataMappingGroup> page = new PaginationSupport<MetadataMappingGroup>(items, total);
        return page;
    }

    @Override
    public MetadataMappingGroup save(MetadataMappingGroup object) {
        object.setId(ConnectionPool.nextId());
        object.setCreateTime(new Date());
        object.setUpdateTime(object.getCreateTime());

        this.systemDao.save("metadataMappingGroup.insert",object);

        return object;
    }


    @Override
    public int delete(Dto dto) {

        return this.systemDao.delete("metadataMappingGroup.deleteByIds", dto);
    }

    @Override
    public int delete(Long id) {
        Dto dto = new BaseDto();
        dto.put("id", id);
        return this.systemDao.delete("metadataMappingGroup.deleteById", id);
    }

    @Override
    public MetadataMappingGroup get(String id) {
        Dto dto = new BaseDto();
        dto.put("id", id);
        return (MetadataMappingGroup)this.systemDao.queryForObject("metadataMappingGroup.selectById", dto);
    }

    @Override
    public List<MetadataMappingGroup> getMappingGroupList() {
        List<MetadataMappingGroup> items = systemDao.queryForList("metadataMappingGroup.selectList");
        return items;
    }

    @Override
    public int update(MetadataMappingGroup Object) {
        return this.systemDao.update("metadataMappingGroup.updateById",Object);
    }




}
