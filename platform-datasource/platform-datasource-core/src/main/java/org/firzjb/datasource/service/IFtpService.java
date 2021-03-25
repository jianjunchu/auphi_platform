package org.firzjb.datasource.service;

import org.firzjb.datasource.entity.Ftp;
import org.firzjb.datasource.model.request.FtpRequest;
import org.firzjb.datasource.model.response.FtpResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.firzjb.datasource.entity.Ftp;
import org.firzjb.datasource.model.request.FtpRequest;
import org.firzjb.datasource.model.response.FtpResponse;

import java.util.List;

/**
 * <p>
 * FTP管理 服务类
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
public interface IFtpService extends IService<Ftp> {

    Page<FtpResponse> getPage(Page<Ftp> page, FtpRequest request);

    List<FtpResponse> getFtps(FtpRequest request);

    FtpResponse save(FtpRequest request);

    FtpResponse update(FtpRequest request);

    int del(Long id);

    FtpResponse get(Long id);
}
