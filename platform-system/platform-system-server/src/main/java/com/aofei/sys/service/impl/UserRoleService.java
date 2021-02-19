package com.aofei.sys.service.impl;

import com.aofei.base.service.impl.BaseService;
import com.aofei.log.annotation.Log;
import com.aofei.sys.entity.UserRole;
import com.aofei.sys.mapper.UserRoleMapper;
import com.aofei.sys.service.IUserRoleService;
import com.aofei.utils.Utils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
