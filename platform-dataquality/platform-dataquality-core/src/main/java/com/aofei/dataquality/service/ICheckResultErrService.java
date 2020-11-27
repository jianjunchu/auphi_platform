package com.aofei.dataquality.service;

import com.aofei.dataquality.entity.CheckResultErr;
import com.aofei.dataquality.model.request.CheckResultErrRequest;
import com.aofei.dataquality.model.response.CheckResultErrResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 数据质量管理-错误字段值记录 服务类
 * </p>
 *
 * @author Tony
 * @since 2020-11-17
 */
public interface ICheckResultErrService extends IService<CheckResultErr> {

    /**
     * 获取 错误字段值记录 列表
     * @param page
     * @param request
     * @return
     */
    Page<CheckResultErrResponse> getPage(Page<CheckResultErr> page, CheckResultErrRequest request);

    /**
     * 保存 错误字段值记录 信息
     * @param request
     * @return
     */
    CheckResultErrResponse save(CheckResultErrRequest request);

    /**
     * 更新 错误字段值记录 信息
     * @param request
     * @return
     */
    CheckResultErrResponse update(CheckResultErrRequest request);

    /**
     * 根据Id 查询 错误字段值记录
     * @param ruleId
     * @return
     */
    CheckResultErrResponse get(Long ruleId);
    /**
     * 根据Id 删除 错误字段值记录
     * @param ruleId
     * @return
     */
    int del(Long ruleId);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<CheckResultErrResponse> getCheckResultErrs(CheckResultErrRequest request);
}
