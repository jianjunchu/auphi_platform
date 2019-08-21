package com.auphi.ktrl.metadata.service;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.ktrl.metadata.domain.MetadataMappingGroup;

import java.sql.SQLException;
import java.util.List;

public interface MetadataMappingGroupService {

    PaginationSupport<MetadataMappingGroup> getPage(Dto<String, Object> dto) throws SQLException;

    void save(MetadataMappingGroup mapping);

    int update(MetadataMappingGroup mapping);

    int delete(Dto dto);

    int delete(Long id);

    MetadataMappingGroup get(String id);

    List<MetadataMappingGroup> getMappingGroupList();
}
