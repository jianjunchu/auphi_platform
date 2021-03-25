package org.firzjb.sys.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.sys.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-09-15
 */
@MyBatisMapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询用户拥有的角色列表
     * @param userId
     * @return
     */
    List<Role> findRoleByUserId(@Param("userId")Long userId);
}
