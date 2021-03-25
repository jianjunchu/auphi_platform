package org.firzjb.sys.service.impl;

import org.firzjb.base.common.Const;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.exception.StatusCode;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.log.annotation.Log;
import org.firzjb.sys.entity.Menu;
import org.firzjb.sys.mapper.MenuMapper;
import org.firzjb.sys.model.request.MenuRequest;
import org.firzjb.sys.model.response.MenuResponse;
import org.firzjb.sys.model.response.MenuTreeResponse;
import org.firzjb.sys.service.IMenuService;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.firzjb.sys.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author Tony
 * @since 2018-09-14
 */
@Service
public class MenuService extends BaseService<MenuMapper, Menu> implements IMenuService {

    @Override
    public Page<MenuResponse> getPage(Page<Menu> page, MenuRequest request) {
        List<Menu> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, MenuResponse.class);
    }

    /**
     * 获取 Menu 列表
     * @param request
     * @return
     */
    @Override
    public List<MenuResponse> getMenus(MenuRequest request) {
        List<Menu> list = baseMapper.findList(request);
        return BeanCopier.copy(list,MenuResponse.class);
    }

    @Override
    @Log(module = "系统菜单", description = "新建菜单信息")
    @Transactional
    public MenuResponse save(MenuRequest request) {
        Menu menu = BeanCopier.copy(request, Menu.class);
        menu.preInsert();
        super.insert(menu);
        return BeanCopier.copy(menu, MenuResponse.class);
    }

    @Override
    @Log(module = "系统菜单", description = "修改菜单信息")
    @Transactional
    public MenuResponse update(MenuRequest request) {
        Menu existing = selectById(request.getMenuId());
        if (existing != null) {
            existing.setName(request.getName());
            existing.setUrl(request.getUrl());
            existing.setIcon(request.getIcon());
            existing.setPerms(request.getPerms());
            existing.setStatus(request.getStatus());
            existing.setOrderNum(request.getOrderNum());
            existing.setParentId(request.getParentId());
            existing.preUpdate();

            super.insertOrUpdate(existing);

            return BeanCopier.copy(existing, MenuResponse.class);
        } else {
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public MenuResponse get(Long menuId) {
        Menu existing = selectById(menuId);
        if(existing!=null){
            return BeanCopier.copy(existing, MenuResponse.class);
        }else{
            //不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    @Log(module = "系统菜单", description = "删除菜单信息")
    @Transactional
    public int del(Long menuId) {
        Menu existing = selectById(menuId);
        if (existing != null) {
            super.deleteById(menuId);
            return 1;
        } else {
            // 不存在
            throw new ApplicationException(StatusCode.NOT_FOUND.getCode(), StatusCode.NOT_FOUND.getMessage());
        }
    }

    @Override
    public List<MenuResponse> getMenusByUser(Long userId) {
        List<Menu> list = baseMapper.findMenusByUser(userId);
        return BeanCopier.copy(list,MenuResponse.class);
    }

    @Override
    public List<MenuTreeResponse> getMenuTree() {


        List<MenuTreeResponse>   trees = new ArrayList<>();

        MenuRequest request = new MenuRequest();
        request.setParentId(0L);
        request.setStatus(Const.NO);
        request.setOrder("asc");
        request.setSort("a.ORDER_NUM");
        List<Menu> list = baseMapper.findList(request);



        for(Menu root: list){
            request.setParentId(root.getMenuId());
            MenuTreeResponse rootResponse = BeanCopier.copy(root,MenuTreeResponse.class);
            rootResponse.getRoles().add(root.getPerms());
            List<Menu> childrens = baseMapper.findList(request);
            for(Menu children :  childrens){
                MenuTreeResponse child = BeanCopier.copy(children,MenuTreeResponse.class);
                child.getRoles().add(child.getPerms());
                rootResponse.getRoles().add(child.getPerms());
                rootResponse.getChildren().add(child);
            }
            trees.add(rootResponse);
        }

        return trees;
    }

    @Override
    public List<MenuResponse> getMenusByRole(Long roleId) {
        List<Menu> list = baseMapper.findMenusByRole(roleId);
        return BeanCopier.copy(list,MenuResponse.class);
    }
}
