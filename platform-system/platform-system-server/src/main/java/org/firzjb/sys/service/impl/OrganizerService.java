package org.firzjb.sys.service.impl;

import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.sys.entity.Organizer;
import org.firzjb.sys.mapper.OrganizerMapper;
import org.firzjb.sys.model.request.OrganizerRequest;
import org.firzjb.sys.model.response.OrganizerResponse;
import org.firzjb.sys.service.IOrganizerService;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.sys.mapper.OrganizerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 组织管理 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-09-14
 */
@Service
public class OrganizerService extends BaseService<OrganizerMapper, Organizer> implements IOrganizerService {

    @Override
    public Page<OrganizerResponse> getPage(Page<Organizer> page, OrganizerRequest request) {
        List<Organizer> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, OrganizerResponse.class);
    }

    /**
     * 获取 Organizer 列表
     * @param request
     * @return
     */
    @Override
    public List<OrganizerResponse> getOrganizers(OrganizerRequest request) {
        List<Organizer> list = baseMapper.findList(request);
        return BeanCopier.copy(list,OrganizerResponse.class);
    }

    @Override
    @Log(module = "系统组织", description = "新建组织信息")
    @Transactional
    public OrganizerResponse save(OrganizerRequest request) {
        Organizer menu = BeanCopier.copy(request, Organizer.class);
        menu.preInsert();
        super.insert(menu);
        return BeanCopier.copy(menu, OrganizerResponse.class);
    }

    @Override
    @Log(module = "系统组织", description = "修改组织信息")
    @Transactional
    public OrganizerResponse update(OrganizerRequest request) {
        Organizer existing = selectById(request.getOrganizerId());
        if (existing != null) {
            existing.setCode(request.getCode());
            existing.setName(request.getName());
            existing.setEmail(request.getEmail());
            existing.setTelphone(request.getTelphone());
            existing.setAddress(request.getAddress());
            existing.setStatus(request.getStatus());
            existing.setProvinceId(request.getProvinceId());

            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, OrganizerResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public OrganizerResponse get(Long menuId) {
        Organizer existing = selectById(menuId);
        if(existing!=null){
            return BeanCopier.copy(existing, OrganizerResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    @Log(module = "系统组织", description = "删除组织信息")
    @Transactional
    public int del(Long menuId) {
        Organizer existing = selectById(menuId);
        if (existing != null) {
            super.deleteById(menuId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }



}
