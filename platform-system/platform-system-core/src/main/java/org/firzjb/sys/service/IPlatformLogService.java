package org.firzjb.sys.service;

import org.firzjb.sys.entity.PlatformLog;
import org.firzjb.sys.model.request.PlatformLogRequest;
import org.firzjb.sys.model.response.PlatformLogResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.sys.model.request.PlatformLogRequest;
import org.firzjb.sys.model.response.PlatformLogResponse;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-09-18
 */
public interface IPlatformLogService extends IService<PlatformLog> {


    /**
     * 获取 系统日志 列表
     * @param page
     * @param request
     * @return
     */
    Page<PlatformLogResponse> getPage(Page<PlatformLog> page, PlatformLogRequest request);
}
