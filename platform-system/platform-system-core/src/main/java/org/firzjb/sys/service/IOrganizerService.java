package org.firzjb.sys.service;

import org.firzjb.sys.entity.Organizer;
import org.firzjb.sys.model.request.OrganizerRequest;
import org.firzjb.sys.model.response.OrganizerResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.sys.entity.Organizer;
import org.firzjb.sys.model.request.OrganizerRequest;
import org.firzjb.sys.model.response.OrganizerResponse;

import java.util.List;

/**
 * <p>
 * 组织管理 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-09-14
 */
public interface IOrganizerService extends IService<Organizer> {

    /**
     * 获取 Organizer 列表
     * @param page
     * @param request
     * @return
     */
    Page<OrganizerResponse> getPage(Page<Organizer> page, OrganizerRequest request);

    /**
     * 获取 Organizer 列表
     * @param request
     * @return
     */
    List<OrganizerResponse> getOrganizers(OrganizerRequest request);
    /**
     * 保存 Organizer 信息
     * @param request
     * @return
     */
    OrganizerResponse save(OrganizerRequest request);

    /**
     * 更新 Organizer 信息
     * @param request
     * @return
     */
    OrganizerResponse update(OrganizerRequest request);

    /**
     * 根据Id 查询 Organizer
     * @param id
     * @return
     */
    OrganizerResponse get(Long id);
    /**
     * 根据Id 删除 Organizer
     * @param id
     * @return
     */
    int del(Long id);


}
