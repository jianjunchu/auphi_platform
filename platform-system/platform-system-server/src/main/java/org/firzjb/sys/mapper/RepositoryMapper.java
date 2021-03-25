package org.firzjb.sys.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.sys.entity.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资源库管理 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-09-21
 */
@MyBatisMapper
public interface RepositoryMapper extends BaseMapper<Repository> {

    /**
     * 根据名称查询资源库信息
     * @param repositoryName
     * @return
     */
    Repository findByRepositoryName(@Param("repositoryName") String repositoryName);

    /**
     * 执行所有去掉Default
     */
    void cancelAllDefault();
}
