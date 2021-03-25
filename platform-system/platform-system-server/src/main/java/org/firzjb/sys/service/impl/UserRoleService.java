package org.firzjb.sys.service.impl;

import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.sys.entity.UserRole;
import org.firzjb.sys.mapper.UserRoleMapper;
import org.firzjb.sys.service.IUserRoleService;
import org.firzjb.utils.Utils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.firzjb.sys.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-09-14
 */
@Service
public class UserRoleService extends BaseService<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    @Transactional
    @Log(module = "系统用户", description = "修改用户角色")
    public Integer changeUserRole(Long userId, List<Long> roles) {
        if (!Utils.isEmpty(userId)) {
            super.delete(new EntityWrapper<UserRole>().eq("C_USER_ID", userId));

            if (!Utils.isEmpty(roles)) {

                for (Long roleId : roles) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(roleId);
                    userRole.setUserId(userId);
                    userRole.insert();
                }

                return 1;
            }
        }
        return -1;
    }

    @Override
    @Transactional
    @Log(module = "系统角色", description = "删除角色下指定的用户")
    public int deleteUserRole(Long userId, Long roleId) {
        if(userId!=null && roleId!=null){
            super.delete(new EntityWrapper<UserRole>().eq("C_ROLE_ID", roleId).eq("C_USER_ID", userId));
            return 1;
        }
        return -1;
    }
}
