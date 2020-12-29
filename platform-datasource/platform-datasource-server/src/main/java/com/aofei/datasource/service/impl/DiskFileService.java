package com.aofei.datasource.service.impl;

import com.aofei.base.common.Const;
import com.aofei.base.exception.ApplicationException;
import com.aofei.datasource.exception.DiskError;
import com.aofei.datasource.model.request.DiskFileCreateRequest;
import com.aofei.datasource.model.request.DiskFileRequest;
import com.aofei.datasource.model.request.FileRenameToRequest;
import com.aofei.datasource.model.response.DiskFileResponse;
import com.aofei.datasource.service.IDiskFileService;
import com.aofei.utils.DiskFileUtil;
import com.aofei.utils.StringUtils;
import org.apache.commons.vfs2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Tony
 * @create 2018-10-21 21:50
 */
@Service
public class DiskFileService implements IDiskFileService {

    private Logger logger = LoggerFactory.getLogger(DiskFileService.class);


    @Override
    public List<DiskFileResponse> getFileTree(DiskFileRequest request) {

        List<DiskFileResponse> list = new LinkedList<>();
        String path = request.getPath();
        if(StringUtils.isEmpty(path)){
            path = Const.getUserDir(request.getOrganizerId());
        }else{
            path = Const.getUserDir(request.getOrganizerId())+path;
        }
        logger.info("path==>"+path);
        File file= new File(path);

        DiskFileResponse json = new DiskFileResponse();

        json.setFilename("本地文件");
        json.setIsdir(Const.YES);
        json.setLeaf(false);
        json.setPath("/");
        json.setLastModified(0L);
        json.setChildren(getAllFiles(request,file,json.getChildren()));

        list.add(json);


        return  list;

    }


    private List<DiskFileResponse> getAllFiles (DiskFileRequest request,File file, List<DiskFileResponse> array) {
        File[] files = file.listFiles();

        for (File current : files) {
            if (current.isDirectory()) {
                DiskFileResponse json = new DiskFileResponse();

                json.setFilename(current.getName());
                json.setIsdir(Const.YES);
                json.setLeaf(!current.isDirectory());
                json.setPath(Const.getUserFilePath(request.getOrganizerId(),current.getAbsolutePath()));
                json.setLastModified(current.lastModified());
                json.setChildren(getAllFiles(request,current, json.getChildren()));
                array.add(json);

            } else {
                DiskFileResponse json = new DiskFileResponse();

                json.setFilename(current.getName());
                json.setIsdir(Const.NO);
                json.setLeaf(!current.isDirectory());
                json.setPath(Const.getUserFilePath(request.getOrganizerId(),current.getAbsolutePath()));
                json.setLastModified(current.lastModified());

                array.add(json);
            }
        }
        return array;
    }


    @Override
    public List<DiskFileResponse> getFileExplorer(DiskFileRequest request)  {
        List<DiskFileResponse> list = new LinkedList<>();
        String path = request.getPath();
        if(StringUtils.isEmpty(path)){
            path = Const.getUserDir(request.getOrganizerId());
        }else{
            path = Const.getUserDir(request.getOrganizerId())+path;
        }
        logger.info("path==>"+path);
        File[] files = new File(path).listFiles();
        if(files !=null && files.length>0){
            for(File file : files) {
                DiskFileResponse response  = new DiskFileResponse();

                if(file.isHidden())
                    continue;

                response.setFilename(file.getName());
                response.setIsdir(file.isDirectory() ? Const.YES : Const.NO);
                response.setLeaf(!file.isDirectory());
                response.setPath(Const.getUserFilePath(request.getOrganizerId(),file.getAbsolutePath()));
                response.setLastModified(file.lastModified());

                list.add(response);
            }
        }


        return list;
    }



    @Override
    public boolean mkdir(DiskFileCreateRequest request) {
        String userPath = Const.getUserDir(request.getOrganizerId());
        String path = request.getPath();
        if(StringUtils.isEmpty(path)){
            path = userPath;
        }

        if(!path.startsWith(userPath)){
            path = userPath+path;
        }
        path = path+File.separator+request.getName();
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
            return true;
        }else{
            throw new ApplicationException(DiskError.DIR_EXISTS.getCode(),DiskError.DIR_EXISTS.getMessage());
        }

    }



    @Override
    public boolean deleteFile(String path) throws FileSystemException {
        FileSystemManager fsManager = VFS.getManager();
        return fsManager.resolveFile(path).delete();
    }

    @Override
    public int deleteDirectory(String path) throws FileSystemException {


        DiskFileUtil.delFolder(path);
        // 测试表明删除文件夹删除不了
        return 1;
    }

    @Override
    public Boolean renameTo(FileRenameToRequest request) {

        String userPath = Const.getUserDir(request.getOrganizerId());
        String startFilePath = request.getStartFile();
        String endPath = request.getEndPath();
        if(!startFilePath.startsWith(userPath)){
            startFilePath = userPath+startFilePath;
        }

        if(!endPath.startsWith(userPath)){
            endPath = userPath+endPath;
        }

        //#源文件路径
        File startFile = new File(startFilePath);

        //#目的目录路径
        File endDirection=new File(endPath);

        File endFile=new File(endDirection+ File.separator+ startFile.getName());

        try {
	        //#调用File类的核心方法renameTo
            if (startFile.renameTo(endFile)) {
               return true;
            } else {
                return false;
            }
        }catch(Exception e) {
            return false;
        }

    }

    /**
     * 根据自定义类别获取FileSelector
     *
     * @param type 自己定义的类别，详见{@link:TestMonitorFile.Type}
     * @return
     * @auther <a href="mailto:leader1212@sina.com.cn">天涯</a>
     * Sep 1, 2010 3:59:30 PM
     */
    public static FileSelector getFileSelector (Type type) {
        return new FileTypeSelector(getFileType(type));
    }

    /**
     * 需要拷贝的文件类型
     * 实际测试中，FILE和FILE_OR_FOLDER效果一样。
     * @author ABBE
     *
     */
    public static enum Type {
        FILE, FOLDER, FILE_OR_FOLDER
    }

    /**
     * 根据自定义类别获取FileType
     *
     * @param type 自己定义的类别，详见{@link:TestMonitorFile.Type}
     * @return
     * @auther <a href="mailto:leader1212@sina.com.cn">天涯</a>
     * Sep 1, 2010 3:59:30 PM
     */
    public static FileType getFileType (Type type) {
        FileType fileType = null;
        if (type.equals(Type.FILE)) {
            fileType = FileType.FILE;
        } else if (type.equals(Type.FOLDER)) {
            fileType = FileType.FOLDER;
        } else if (type.equals(Type.FILE_OR_FOLDER)) {
            fileType = FileType.FILE_OR_FOLDER;
        }
        return fileType;
    }
}
