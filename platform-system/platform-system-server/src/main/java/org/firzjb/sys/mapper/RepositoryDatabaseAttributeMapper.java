package org.firzjb.sys.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.sys.entity.RepositoryDatabaseAttribute;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资源库链接属性 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-09-21
 */
@MyBatisMapper
public interface RepositoryDatabaseAttributeMapper extends BaseMapper<RepositoryDatabaseAttribute> {

    int deleteByDatabaseId(@Param("repositoryConnectionId") Long repositoryConnectionId);
}
