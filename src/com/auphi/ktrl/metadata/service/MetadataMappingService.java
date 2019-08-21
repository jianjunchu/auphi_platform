package com.auphi.ktrl.metadata.service;

import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.ktrl.metadata.domain.MetadataMapping;

import java.sql.SQLException;
import java.util.List;

public interface MetadataMappingService {

    PaginationSupport<MetadataMapping> getPage(Dto<String, Object> dto) throws SQLException;

    void save(MetadataMapping mapping);

    int update(MetadataMapping mapping);

    int delete(Dto dto);

    int delete(Long id);

    void save(List<MetadataMapping> mappings);

    MetadataMapping get(Long id);
}
