package org.firzjb.datasource.controller;

import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.common.Const;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.exception.ApplicationException;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.datasource.exception.DiskError;
import org.firzjb.datasource.model.request.DiskFileCreateRequest;
import org.firzjb.datasource.model.request.DiskFileDeleteRequest;
import org.firzjb.datasource.model.request.DiskFileRequest;
import org.firzjb.datasource.model.request.FileRenameToRequest;
import org.firzjb.datasource.model.response.DiskFileListResponse;
import org.firzjb.datasource.model.response.DiskFileResponse;
import org.firzjb.datasource.model.response.ResidualSpaceResponse;
import org.firzjb.datasource.service.IDiskFileService;
import org.firzjb.utils.DiskFileUtil;
import org.firzjb.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 数据源管理-本地文件管理
 * 用户本地文件管理;
 * 上传文件
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Log4j
@Api(tags = { "数据源管理-本地文件管理" })
@Authorization
@RestController
@RequestMapping(value = "/datasource/disk", produces = {"application/json;charset=UTF-8"})
public class DiskFileController extends BaseController {



    @Autowired
    private IDiskFileService diskFileService;

    /**
     * 服务器文件列表
     * @param request
     * @return
     */
    @ApiOperation(value = "服务器文件列表", notes = "服务器文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "目录", paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/explorer", method = RequestMethod.POST)
    public Response<List<DiskFileListResponse>> explorer(
            @ApiIgnore DiskFileRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        request.setOrganizerName(user.getOrganizerName());

        List<DiskFileResponse> list = diskFileService.getFileExplorer(request);

        DiskFileListResponse response = new DiskFileListResponse();
        response.setList(list);
        response.setPath(request.getPath());
        return Response.ok(response) ;
    }

    /**
     * 返回指定目录下的文件目录名称 tree形结构
     *
     * @param request
     *  path:root目录
     *
     * @return
     *  Response<DiskFileResponse>
     *  String path: 路径
     *  String filename:文件名称;
     *  Integer isdir: 是否是文件夹 0:否 1:是
     *  String size:文件大小;
     *  Boolean leaf: 文件夹是否还有文件;
     *  Long lastModified:最后修改时间;
     *  List<DiskFileResponse> children :子文件夹
     */
    @ApiOperation(value = "服务器文:件列表", notes = "服务器文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "目录", paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public Response<DiskFileResponse> tree(
            @ApiIgnore DiskFileRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        request.setOrganizerName(user.getOrganizerName());



        List<DiskFileResponse> list = diskFileService.getFileTree(request);

        return Response.ok(list) ;
    }


    /**
     * 文件移动
     *
     * @param request
     *  startFile : 要移动的文件
     *  endPath : 移动的位置
     * @param user
     *  当前登录的用户
     * @return
     *    Response<Boolean> true:移动成功 ; false:移动失败
     *
     */
    @ApiOperation(value = "服务器文件移动", notes = "服务器文件移动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startFile", value = "要移动的文件", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endPath", value = "移动的位置(目录)", paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/renameTo", method = RequestMethod.POST)
    public Response<Boolean> renameTo(
            @RequestBody FileRenameToRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user){

        request.setOrganizerId(user.getOrganizerId());

       Boolean res = diskFileService.renameTo(request);

        return Response.ok(res) ;
    }


    /**
     * 服务器文件列表
     * @return
     */
    @ApiOperation(value = "磁盘剩余空间", notes = "磁盘剩余空间")
    @RequestMapping(value = "/residual", method = RequestMethod.POST)
    public Response<ResidualSpaceResponse> residual(
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {

        String path =  Const.getUserDir(user.getOrganizerId());

        long total = DiskFileUtil.getTotalSizeOfFilesInDir(new File(path));


        ResidualSpaceResponse response = new ResidualSpaceResponse();
        response.setDiskSpace(DiskFileUtil.getPrintSize(user.getDiskSpace()));
        response.setResidualSpace(DiskFileUtil.getPrintSize(total));
        response.setPercentage(DiskFileUtil.getPercentage(total,user.getDiskSpace()));
       return Response.ok(response);
    }



    /**
     * 创建文件夹
     * @param request
     * @return
     */
    @ApiOperation(value = "创建文件夹", notes = "创建文件夹")
    @RequestMapping(value = "/mkdir", method = RequestMethod.POST)
    public Response<Boolean> mkdir(
            @RequestBody DiskFileCreateRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user)  {
        request.setOrganizerId(user.getOrganizerId());
        request.setOrganizerName(user.getOrganizerName());
        return Response.ok(diskFileService.mkdir(request)) ;
    }

    /**
     * 上传文件
     * @param request
     * @return
     */
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiResponses(value = {
            @ApiResponse(code = 300001, message = "Insufficient disk space"),
            @ApiResponse(code = 200, message = "success")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径('/'开头）", paramType = "form",  dataType = "string"),
            @ApiImplicitParam(name = "file", value = "文件", paramType = "form", required = true, dataType = "__file")
    })
    @PostMapping(value = "/upload",consumes = "multipart/*",headers = "content-type=multipart/form-data")
    public Response<Boolean> upload(
            HttpServletRequest request,
            @ApiIgnore @CurrentUser CurrentUserResponse user) throws IOException {

        String userPath = Const.getUserDir(user.getOrganizerId());
        long total = DiskFileUtil.getTotalSizeOfFilesInDir(new File(userPath));

        String path = ServletRequestUtils.getStringParameter(request,"path", userPath);
        if(StringUtils.isEmpty(path) || "/".equalsIgnoreCase(path)){
            path = userPath;
        }else{
            if(!path.startsWith(userPath)){
                path = userPath + path;
            }
        }

        File dir=new File(path);
        if(!dir.exists()){//如果文件夹不存在
            dir.mkdirs();//创建文件夹
        }

        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();
            while(iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());

                long all = total+file.getSize();
                if(all > user.getDiskSpace()){
                    throw new ApplicationException(DiskError.INSUFFICIENT_DISK_SPACE.getCode(),DiskError.INSUFFICIENT_DISK_SPACE.getMessage());
                }
                if(file!=null) {
                    File filepath = new File(path,file.getOriginalFilename());

                    //上传
                    file.transferTo(filepath);
                }
            }
        }
        return Response.ok(true);
    }


    /**
     * 删除文件
     * @param request
     * @param user
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<Integer> upload(
                @RequestBody DiskFileDeleteRequest request,
                @ApiIgnore @CurrentUser CurrentUserResponse user) throws IOException {

        String userPath = Const.getUserDir(user.getOrganizerId());
        String path = request.getPath();
        if(!path.startsWith(userPath)){
            path = userPath+path;
        }

        File file = new File(path);
        if (!file.exists()) {
            new ApplicationException();
        } else {
            if (file.isFile()){
                boolean delete =  diskFileService.deleteFile(path);
                return Response.ok(1);
            }else{
                return Response.ok(diskFileService.deleteDirectory(path));
            }

        }
        return Response.ok(0);
    }



}
