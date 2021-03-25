package org.firzjb.sys.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-09-15
 */
@MyBatisMapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findMenusByUser(@Param("userId")Long userId);

    List<Menu> findMenusByRole(@Param("roleId")Long roleId);
}
