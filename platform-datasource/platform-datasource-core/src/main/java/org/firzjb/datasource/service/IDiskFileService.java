package org.firzjb.datasource.service;


import org.firzjb.datasource.model.request.DiskFileCreateRequest;
import org.firzjb.datasource.model.request.DiskFileRequest;
import org.firzjb.datasource.model.request.FileRenameToRequest;
import org.firzjb.datasource.model.response.DiskFileResponse;
import org.apache.commons.vfs2.FileSystemException;
import org.firzjb.datasource.model.request.DiskFileCreateRequest;
import org.firzjb.datasource.model.request.DiskFileRequest;
import org.firzjb.datasource.model.request.FileRenameToRequest;
import org.firzjb.datasource.model.response.DiskFileResponse;

import java.util.List;

public interface IDiskFileService  {

    /**
     * 获取指定路径下的本地文件列表
     * @param request
     * @return
     */
    List<DiskFileResponse> getFileExplorer(DiskFileRequest request) ;

    /**
     *  获取指定路径下的本地文件树形数据
     * @param request
     * @return
     */
    List<DiskFileResponse> getFileTree(DiskFileRequest request) ;


    /**
     * 创建新的文件夹
     * @param request
     * @return
     */
    boolean mkdir(DiskFileCreateRequest request);


    /**
     * 删除文件
     * @param path
     * @return
     * @throws FileSystemException
     */
    boolean deleteFile(String path) throws FileSystemException;

    /**
     * 删除文件夹
     * @param path
     * @return
     * @throws FileSystemException
     */
    int deleteDirectory(String path) throws FileSystemException;

    /**
     * 移动文件夹
     * @param request
     * @return
     */
    Boolean renameTo(FileRenameToRequest request);
}
