package org.firzjb.sys.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.sys.entity.RepositoryDatabase;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资源库 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-09-21
 */
@MyBatisMapper
public interface RepositoryDatabaseMapper extends BaseMapper<RepositoryDatabase> {

    /**
     * 根据数据量连接的名字查询
     * @param connectionName
     * @return
     */
    RepositoryDatabase findByConnectionName(@Param("connectionName") String connectionName);
}
