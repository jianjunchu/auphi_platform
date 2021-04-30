package org.firzjb.kettle.core;

import org.firzjb.kettle.utils.JsonUtils;
import org.firzjb.kettle.utils.StringEscapeHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 文件上传下载接口api，用于资源库导入和导出
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
@RestController
@RequestMapping(value = "/attachment")
@Api(tags = "文件上传下载接口api，用于资源库导入和导出")
public class AttachmentController {

	static LinkedList<File> files = new LinkedList<File>();
	static Timer deleter = new Timer();

	static {
		deleter.schedule(new TimerTask() {
			@Override
			public void run() {
				for(int i=0; i<files.size(); i++) {
					if(files.get(i).delete()) {
						files.remove(i);
						break;
					}
				}
			}
		}, 5000, 60 * 1000);
	}

	/**
	 * 文件下载
	 * @param filePath 文件路径
	 * @param remove 下载完后是否删除
	 * @throws KettleException
	 * @throws IOException
	 */
	@ApiOperation(value = "文件下载 ")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "filePath", value = "文件路径", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "remove", value = "下载完后是否删除", paramType="query", dataType = "remove")
	})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value="/download")
	protected void download(@RequestParam String filePath, @RequestParam boolean remove) throws KettleException, IOException {
		File file = new File(StringEscapeHelper.decode(filePath));
		if(file.isFile()) {
			JsonUtils.download(file);
			if(remove) {
				files.add(file);
			}
		}
	}

	/**
	 * 文件上传
	 * @param file
	 * @throws KettleException
	 * @throws IOException
	 */
	@ApiOperation(value = "文件上传 ")
	@RequestMapping("/upload")
	protected @ResponseBody void upload(@RequestParam(value="file") MultipartFile file) throws KettleException, IOException {

		String  tmp = new StringBuffer(System.getProperty("etl_platform.root")).append(File.separator).append("upload").toString();

		File dir = new File(tmp);
		dir.mkdirs();
		dir = new File(dir, String.format("%1$tY%1$tm%1$td", new Date()));
		dir.mkdirs();
		File f = new File(dir, file.getOriginalFilename());
		System.out.println("===upload to:===" + f.getAbsolutePath());

		OutputStream os = null;
		InputStream is = null;
		try {
			os = FileUtils.openOutputStream(f);
			is = file.getInputStream();

			FileCopyUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}

		JsonUtils.success(f.getAbsolutePath());
	}
}
