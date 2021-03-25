package org.firzjb.datasource.service;

import org.firzjb.datasource.entity.Interface;
import org.firzjb.datasource.model.request.InterfaceRequest;
import org.firzjb.datasource.model.response.InterfaceResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.datasource.model.request.InterfaceRequest;
import org.firzjb.datasource.model.response.InterfaceResponse;

import java.util.List;

/**
 * <p>
 * 数据接口管理 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
public interface IInterfaceService extends IService<Interface> {

    /**
     * 获取 数据接口 列表
     * @param page
     * @param request
     * @return
     */
    Page<InterfaceResponse> getPage(Page<Interface> page, InterfaceRequest request);

    /**
     * 保存 数据接口 信息
     * @param request
     * @return
     */
    InterfaceResponse save(InterfaceRequest request);

    /**
     * 更新 数据接口 信息
     * @param request
     * @return
     */
    InterfaceResponse update(InterfaceRequest request);

    /**
     * 根据Id 查询 数据接口
     * @param interfaceId
     * @return
     */
    InterfaceResponse get(Long interfaceId);
    /**
     * 根据Id 删除 数据接口
     * @param ruleId
     * @return
     */
    int del(Long ruleId);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<InterfaceResponse> getInterfaces(InterfaceRequest request);


}
