package org.firzjb.sys.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.sys.entity.Role;
import org.firzjb.sys.mapper.RoleMapper;
import org.firzjb.sys.model.request.RoleRequest;
import org.firzjb.sys.model.response.RoleResponse;
import org.firzjb.sys.service.IRoleService;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.sys.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-09-14
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> implements IRoleService {

    @Override
    public Page<RoleResponse> getPage(Page<Role> page, RoleRequest request) {
        List<Role> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, RoleResponse.class);
    }

    @Override
    public List<RoleResponse> getRoles(RoleRequest request) {
        List<Role> list = baseMapper.findList(request);
        return BeanCopier.copy(list,RoleResponse.class);
    }

    @Override
    @Log(module = "系统角色", description = "新建角色信息")
    public RoleResponse save(RoleRequest request) {
        Role Role = BeanCopier.copy(request, Role.class);
        Role.preInsert();
        super.insert(Role);
        return BeanCopier.copy(Role, RoleResponse.class);
    }

    @Override
    @Transactional
    @Log(module = "系统角色", description = "修改角色信息")
    public RoleResponse update(RoleRequest request) {
        Role existing = selectById(request.getRoleId());
        if (existing != null) {
            existing.setRoleName(request.getRoleName());
            existing.setDescription(request.getDescription());
            if(request.getOrganizerId()!=null && request.getOrganizerId()>0){
                existing.setOrganizerId(request.getOrganizerId());

            }
            existing.setIsSystemRole(request.getIsSystemRole());
            existing.preUpdate();

            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, RoleResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public RoleResponse get(Long roleId) {
        Role existing = selectById(roleId);
        if(existing!=null){
            return BeanCopier.copy(existing, RoleResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    @Log(module = "系统角色", description = "删除角色信息")
    public int del(Long roleId) {
        Role existing = selectById(roleId);
        if (existing != null) {
            super.deleteById(roleId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public List<RoleResponse> getRolesByUser(Long userId) {
        List<Role> roles = baseMapper.findRoleByUserId(userId);
        List<RoleResponse> responses = BeanCopier.copy(roles, RoleResponse.class);
        return responses;
    }
}
