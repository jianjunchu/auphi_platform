package org.firzjb.kettle.repository.controller;

import org.firzjb.base.annotation.Authorization;
import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.controller.BaseController;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.model.response.Response;
import org.firzjb.kettle.App;
import org.firzjb.kettle.PluginFactory;
import org.firzjb.kettle.base.GraphCodec;
import org.firzjb.kettle.bean.RepositoryCheckNode;
import org.firzjb.kettle.bean.RepositoryNode;
import org.firzjb.kettle.cluster.SlaveServerCodec;
import org.firzjb.kettle.core.database.DatabaseCodec;
import org.firzjb.kettle.repository.beans.DirectoryVO;
import org.firzjb.kettle.repository.beans.RepositoryCascaderVO;
import org.firzjb.kettle.repository.beans.RepositoryNodeType;
import org.firzjb.kettle.repository.beans.RepositoryObjectVO;
import org.firzjb.kettle.utils.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.firzjb.kettle.bean.RepositoryCheckNode;
import org.firzjb.kettle.bean.RepositoryNode;
import org.firzjb.kettle.repository.beans.DirectoryVO;
import org.firzjb.kettle.repository.beans.RepositoryCascaderVO;
import org.firzjb.kettle.repository.beans.RepositoryNodeType;
import org.firzjb.kettle.repository.beans.RepositoryObjectVO;
import org.firzjb.kettle.utils.ServerChecker;
import org.hsqldb.lib.AppendableException;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.missing.MissingEntry;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.repository.*;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.missing.MissingTrans;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 资源库接口api
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
@RestController
@RequestMapping("/repository")
@Api(tags = "资源库接口api")
public class KettleRepositoryController extends BaseController {

	/**
	 * 创建一个资源库目录
	 * @param dir 父目录，如果不存在父目录就使用根目录
	 * @param name 新目录的名称
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@ApiOperation(value = "创建一个资源库目录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "dir", value = "父目录，如果不存在父目录就使用根目录", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "name", value = "新目录的名称", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/createDir")
	protected void createDir(@RequestParam String dir, @RequestParam String name,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		dir = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(),dir);
		Repository repository = App.getInstance().getRepository();

		try {
			RepositoryDirectoryInterface path = repository.findDirectory(dir);
			if(path == null)
				path = repository.getUserHomeDirectory();

			RepositoryDirectoryInterface child = path.findChild(name);
			if(child == null) {
				repository.createRepositoryDirectory(path, name.trim());
			} else {
				JsonUtils.fail("该目录已经存在，请重新输入！");
				return;
			}
			JsonUtils.success("目录创建成功！");
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}


	}

	/**
	 * 在资源库中创建一个转换
	 * @param dir 父目录，如果不存在父目录就使用根目录
	 * @param transName 转换名称
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@ApiOperation(value = "在资源库中创建一个转换")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "dir", value = "父目录，如果不存在父目录就使用根目录", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "name", value = "转换名称", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/createTrans")
	protected void createTrans(@RequestParam String dir, @RequestParam String transName,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {

		dir = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(),dir);

		Repository repository = App.getInstance().getRepository();
		try {
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null)
				directory = repository.getUserHomeDirectory();

			if(repository.exists(transName, directory, RepositoryObjectType.TRANSFORMATION)) {
				JsonUtils.fail("该转换已经存在，请重新输入！");
				return;
			}

			TransMeta transMeta = new TransMeta();
			transMeta.setRepository(App.getInstance().getRepository());
			transMeta.setMetaStore(App.getInstance().getMetaStore());
			transMeta.setName(transName);
			transMeta.setRepositoryDirectory(directory);

			repository.save(transMeta, "创建转换", null);

			String transPath = directory.getPath();
			if(!transPath.endsWith("/"))
				transPath = transPath + '/';
			transPath = transPath + transName;
			JsonUtils.success(transPath);
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}



	}

	/**
	 * 在资源库中创建一个作业
	 * @param dir 父目录，如果不存在父目录就使用根目录
	 * @param jobName 作业名称
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@ApiOperation(value = "在资源库中创建一个作业")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "dir", value = "父目录，如果不存在父目录就使用根目录", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "name", value = "作业名称", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/createJob")
	protected void createJob(@RequestParam String dir, @RequestParam String jobName,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		dir = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(),dir);
		Repository repository = App.getInstance().getRepository();
		try {
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null)
				directory = repository.getUserHomeDirectory();

			if(repository.exists(jobName, directory, RepositoryObjectType.JOB)) {
				JsonUtils.fail("该转换已经存在，请重新输入！");
				return;
			}

			JobMeta jobMeta = new JobMeta();
			jobMeta.setRepository(App.getInstance().getRepository());
			jobMeta.setMetaStore(App.getInstance().getMetaStore());
			jobMeta.setName(jobName);
			jobMeta.setRepositoryDirectory(directory);

			repository.save(jobMeta, "创建作业", null);

			String jobPath = directory.getPath();
			if(!jobPath.endsWith("/"))
				jobPath = jobPath + '/';
			jobPath = jobPath + jobName;

			JsonUtils.success(jobPath);
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}


	}

	/**
	 * 删除
	 * @param path
	 * @param type
	 * @throws KettleException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/drop")
	protected void drop(@RequestParam String path, @RequestParam String type) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();

		try {

			String dir = path.substring(0, path.lastIndexOf("/"));
			String name = path.substring(path.lastIndexOf("/") + 1);
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null) {
				JsonUtils.fail("删除失败，定位不到目录.");
				return;
			}

			if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(type)
					|| RepositoryObjectType.TRANSFORMATION.getExtension().equals(type)) {
				ObjectId id_transformation = repository.getTransformationID(name, directory);
				if(id_transformation != null) {
					repository.deleteTransformation(id_transformation);
				}
			} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(type)
					|| RepositoryObjectType.JOB.getExtension().equals(type)) {
				ObjectId id_job = repository.getJobId(name, directory);
				if(id_job != null) {
					repository.deleteJob(id_job);
				}
			} else if(StringUtils.isEmpty(type) || "dir".equalsIgnoreCase(type)) {
				directory = repository.findDirectory(path);
				dropDirectory(repository, directory);
			}

			JsonUtils.success("操作成功");

		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}

	private void dropDirectory(Repository repository, RepositoryDirectoryInterface directory) throws KettleException, IOException {
		List<RepositoryElementMetaInterface> jobAndTransformationObjects = repository.getJobAndTransformationObjects(directory.getObjectId(), true);
		for(RepositoryElementMetaInterface jobAndTransformationObject : jobAndTransformationObjects) {
			if(jobAndTransformationObject.getObjectType() == RepositoryObjectType.TRANSFORMATION) {
				repository.deleteTransformation(jobAndTransformationObject.getObjectId());
			} else if(jobAndTransformationObject.getObjectType() == RepositoryObjectType.JOB) {
				repository.deleteJob(jobAndTransformationObject.getObjectId());
			}
		}


		for( RepositoryDirectoryInterface subDirectory : directory.getChildren()) {
			dropDirectory(repository, subDirectory);
		}

		repository.deleteRepositoryDirectory(directory);

	}




	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/dropVerify")
	protected void dropVerify(@RequestParam String path, @RequestParam String type) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();

		try {
			String dir = path.substring(0, path.lastIndexOf("/"));
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null) {
				JsonUtils.fail("校验失败，定位不到目录.");
				return;
			}

			if(StringUtils.isEmpty(type) || "dir".equalsIgnoreCase(type)) {
				directory = repository.findDirectory(path);
				if(repository.getJobAndTransformationObjects(directory.getObjectId(), true).size() > 0) {
					JsonUtils.fail("该目录下存在子元素，您确定要移除该目录吗?");
					return;
				}

				if(repository.getDirectoryNames(directory.getObjectId()).length > 0) {
					JsonUtils.fail("该目录下存在子目录，您确定要移除该目录吗?");
					return;
				}

			} else {
				JsonUtils.success("操作成功");
			}

			JsonUtils.success("操作成功");
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}


	/**
	 * 重命名
	 * @param path 路径
	 * @param newName 新的名字
	 * @param type
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/rename")
	protected void rename(@RequestParam String path, String newName, @RequestParam String type, @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();

		String dir = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);
		RepositoryDirectoryInterface directory = repository.findDirectory(dir);
		if(directory == null)
			directory = repository.getUserHomeDirectory();

		if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(type)
				|| RepositoryObjectType.TRANSFORMATION.getExtension().equals(type)) {
			ObjectId id_transformation = repository.getTransformationID(name, directory);

			//TODO 直接调用rename死锁！为什么
			if(id_transformation != null) {
				TransMeta transMeta = repository.loadTransformation(id_transformation, null);
				transMeta.setName(newName);
				transMeta.setModifiedDate(new Date());
				transMeta.setModifiedUser(user.getUsername());
				repository.save(transMeta, "重命名：oldname=" + name, null);

				if(repository instanceof KettleFileRepository) {
					repository.deleteTransformation(id_transformation);
				}
			}
		} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(type)
				|| RepositoryObjectType.JOB.getExtension().equals(type)) {
			ObjectId id_job = repository.getJobId(name, directory);
			//TODO 直接调用rename死锁！为什么
			if(id_job != null) {
				JobMeta jobMeta = repository.loadJob(id_job, null);
				jobMeta.setName(newName);
				jobMeta.setModifiedDate(new Date());
				jobMeta.setModifiedUser(user.getUsername());
				repository.save(jobMeta, "重命名：oldname=" + name, null);

				if(repository instanceof KettleFileRepository) {
					repository.deleteJob(id_job);
				}
			}
		} else if(StringUtils.isEmpty(type) || "dir".equalsIgnoreCase(type)) {
			directory = repository.findDirectory(path);
			repository.renameRepositoryDirectory(directory.getObjectId(), null, newName);
		}

		repository.disconnect();

		JsonUtils.success("操作成功");
	}

	/**
	 * 移动文件
	 * @param path
	 * @param newPath
	 * @param type
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/renameTo")
	protected void renameTo(@RequestParam String path, String newPath, @RequestParam String type, @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();

		String dir = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);
		RepositoryDirectoryInterface directory = repository.findDirectory(dir);
		if(directory == null)
			directory = repository.getUserHomeDirectory();

		newPath = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(),newPath);

		RepositoryDirectoryInterface new_directory = repository.findDirectory(newPath);

		if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(type)
				|| RepositoryObjectType.TRANSFORMATION.getExtension().equals(type)) {
			ObjectId id_transformation = repository.getTransformationID(name, directory);

			//TODO 直接调用rename死锁！为什么
			if(id_transformation != null) {
				TransMeta transMeta = repository.loadTransformation(id_transformation, null);
				transMeta.setRepositoryDirectory(new_directory);
				transMeta.setModifiedDate(new Date());
				transMeta.setModifiedUser(user.getUsername());
				repository.save(transMeta, "重命名：oldname=" + name, null);

				if(repository instanceof KettleFileRepository) {
					repository.deleteTransformation(id_transformation);
				}
			}
		} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(type)
				|| RepositoryObjectType.JOB.getExtension().equals(type)) {
			ObjectId id_job = repository.getJobId(name, directory);
			//TODO 直接调用rename死锁！为什么
			if(id_job != null) {
				JobMeta jobMeta = repository.loadJob(id_job, null);
				jobMeta.setRepositoryDirectory(new_directory);
				jobMeta.setModifiedDate(new Date());
				jobMeta.setModifiedUser(user.getUsername());
				repository.save(jobMeta, "重命名：oldname=" + name, null);

				if(repository instanceof KettleFileRepository) {
					repository.deleteJob(id_job);
				}
			}
		} else if(StringUtils.isEmpty(type) || "dir".equalsIgnoreCase(type)) {
			directory = repository.findDirectory(path);
			directory.setParent(new_directory);

		}

		repository.disconnect();

		JsonUtils.success("操作成功");
	}


	/**
	 * 打开文件
	 * @param path 路径
	 * @param type
	 * @param user
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/open")
	protected void open(@RequestParam String path, @RequestParam String type,@CurrentUser CurrentUserResponse user) throws Exception {
		String dir = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);

		Repository repository = App.getInstance().getRepository();


		try {
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null)
				directory = repository.getUserHomeDirectory();

			if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(type)) {
				TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);
				transMeta.setRepositoryDirectory(directory);

				GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
				String graphXml = codec.encode(transMeta,user);

				JsonUtils.responseXml(StringEscapeHelper.encode(graphXml));
			} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(type)) {
				JobMeta jobMeta = repository.loadJob(name, directory, null, null);
				jobMeta.setRepositoryDirectory(directory);

				GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.JOB_CODEC);
				String graphXml = codec.encode(jobMeta,user);

				JsonUtils.responseXml(StringEscapeHelper.encode(graphXml));
			}
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}
	}

	/**
	 *  加载资源库中的一个转换或作业
	 * @param path 对象路径
	 * @param type 对象类型：transformation or job
	 * @param user
	 * @throws Exception
	 */
	@ApiOperation(value = "加载资源库中的一个转换或作业")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "path", value = "对象路径", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "type", value = "对象类型：transformation or job", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping("/open2")
	protected void open2(@RequestParam String path, @RequestParam String type, @CurrentUser CurrentUserResponse user) throws Exception {
		String dir = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);

		Repository repository = App.getInstance().getRepository();

		try {
			RepositoryDirectoryInterface directory = repository.findDirectory(dir);
			if(directory == null)
				directory = repository.getUserHomeDirectory();

			if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(type)) {
				TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);
				transMeta.setRepositoryDirectory(directory);

				GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
				String graphXml = codec.encode(transMeta, user);

				JsonUtils.success(StringEscapeHelper.encode(graphXml));
			} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(type)) {

				JobMeta jobMeta = repository.loadJob(name, directory, null, null);
				jobMeta.setRepositoryDirectory(directory);

				GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.JOB_CODEC);
				String graphXml = codec.encode(jobMeta, user);

				JsonUtils.success(StringEscapeHelper.encode(graphXml));
			}
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}






	}

	/**
	 * 复制资源库中的一个转换或作业
	 * @param path 对象路径
	 * @param type 对象类型：transformation or job
	 * @param newname 新的文件名称
	 * @param user
	 * @throws Exception
	 */
	@ApiOperation(value = "复制资源库中的一个转换或作业")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "path", value = "对象路径", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "type", value = "对象类型：transformation or job", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping("/clone")
	protected void clone(String path, String type, String newname, @CurrentUser CurrentUserResponse user) throws Exception {
		String dir = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);

		Repository repository = App.getInstance().getRepository();
		RepositoryDirectoryInterface directory = repository.findDirectory(dir);
		if(directory == null) {
			JsonUtils.fail("克隆失败，目录：" + dir + "不存在！");
			return;
		}

		if(RepositoryObjectType.TRANSFORMATION.getExtension().equals(type)) {
			TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);
			transMeta.setRepositoryDirectory(directory);
			transMeta.setObjectId(null);
			transMeta.setName(newname);

			repository.save(transMeta, null, null);

		} else if(RepositoryObjectType.JOB.getExtension().equals(type)) {
			JobMeta jobMeta = repository.loadJob(name, directory, null, null);
	    	jobMeta.setRepositoryDirectory(directory);
	    	jobMeta.setObjectId(null);
	    	jobMeta.setName(newname);

	    	repository.save(jobMeta, null, null);
		}

		repository.disconnect();
		JsonUtils.success("克隆成功！");
	}

	/**
	 * 加载列表
	 * @param path
	 * @param user
	 * @return
	 * @throws KettleException
	 * @throws IOException
	 */
	@Authorization
	@RequestMapping(method=RequestMethod.POST, value="/listElements")
	protected  List<RepositoryObjectVO> listElements(@RequestParam String path, @ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		ArrayList list = new ArrayList();
		String root = org.firzjb.base.common.Const.getRootPath(user.getOrganizerId());
		Repository repository = App.getInstance().getRepository();
		RepositoryDirectoryInterface dir = null;
		if(StringUtils.hasText(path) && !"/".equalsIgnoreCase(path)){
			path = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(),path);
			dir = repository.findDirectory(path);
		}
		else{
			dir = repository.findDirectory(root);
		}

		List<RepositoryDirectoryInterface> directorys = dir.getChildren();
		for(RepositoryDirectoryInterface child : directorys) {
			DirectoryVO directory = new DirectoryVO(child,root);
			list.add(directory);
		}

		String transPath = dir.getPath();
		List<RepositoryElementMetaInterface> elements = repository.getTransformationObjects(dir.getObjectId(), false);
		if(elements != null) {
			for(RepositoryElementMetaInterface e : elements) {
				RepositoryObjectVO ro = new RepositoryObjectVO(e,root);
				list.add(ro);
			}
		}

		elements = repository.getJobObjects(dir.getObjectId(), false);
		if(elements != null) {
			for(RepositoryElementMetaInterface e : elements) {
				RepositoryObjectVO ro = new RepositoryObjectVO(e,root);
				list.add(ro);
			}
		}

		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if(o1 instanceof RepositoryObjectVO && o2 instanceof RepositoryObjectVO) {
					RepositoryObjectVO r1 = (RepositoryObjectVO) o1;
					RepositoryObjectVO r2 = (RepositoryObjectVO) o2;

					int value = r1.getModifiedDate().compareTo(r2.getModifiedDate());

					if(value > 0)
						return value - value * 2;
					else if(value < 0)
						return Math.abs(value);
					else
						return value;

				}
				return 0;
			}
		});

		repository.disconnect();
		return list;
	}

	/**
	 * 资源库浏览，生成树结构
	 * @param path
	 * @param loadElement
	 * @return
	 * @throws KettleException
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.POST, value="/explorer")
	protected @ResponseBody List explorer(@RequestParam String path, @RequestParam int loadElement) throws KettleException, IOException {


		ArrayList list = new ArrayList();

		Repository repository = App.getInstance().getRepository();
		RepositoryDirectoryInterface dir = null;
		if(StringUtils.hasText(path))
			dir = repository.findDirectory(path);
		else
			dir = repository.getUserHomeDirectory();

		List<RepositoryDirectoryInterface> directorys = dir.getChildren();
		for(RepositoryDirectoryInterface child : directorys) {
			list.add(RepositoryNode.initNode(child.getName(), child.getPath()));
		}

		if(RepositoryNodeType.includeTrans(loadElement)) {
			List<RepositoryElementMetaInterface> elements = repository.getTransformationObjects(dir.getObjectId(), false);
			if(elements != null) {
				for(RepositoryElementMetaInterface e : elements) {
					String transPath = dir.getPath();
					if(!transPath.endsWith("/"))
						transPath = transPath + '/';
					transPath = transPath + e.getName();

					list.add(RepositoryNode.initNode(e.getName(),  transPath, e.getObjectType()));
				}
			}
		}

		if(RepositoryNodeType.includeJob(loadElement)) {
			List<RepositoryElementMetaInterface> elements = repository.getJobObjects(dir.getObjectId(), false);
			if(elements != null) {
				for(RepositoryElementMetaInterface e : elements) {
					String transPath = dir.getPath();
					if(!transPath.endsWith("/"))
						transPath = transPath + '/';
					transPath = transPath + e.getName();

					list.add(RepositoryNode.initNode(e.getName(),  transPath, e.getObjectType()));
				}
			}
		}
		repository.disconnect();
		return list;
	}

	@RequestMapping(method=RequestMethod.POST, value="/exp")
	protected @ResponseBody void exp(@RequestParam String data) throws KettleException, IOException, AppendableException {
		Repository repository = null;
		FileOutputStream fos = null;
		ZipOutputStream out = null;
		try {
			repository = App.getInstance().getRepository();

			JSONArray jsonArray = JSONArray.fromObject(data);
			String  tmp = new StringBuffer(System.getProperty("etl_platform.root")).append(File.separator).append("tmp").append(File.separator).toString();
			File file = new File(tmp+"exp_" + repository.getName() +"_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", new Date()) + ".zip");

			fos = new FileOutputStream(file);
			out = new ZipOutputStream( fos );
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String path = jsonObject.optString("path");
				String entryPath = path.substring(1);
				String dir = path.substring(0, path.lastIndexOf("/"));
				String name = path.substring(path.lastIndexOf("/") + 1);

				RepositoryDirectoryInterface directory = repository.findDirectory(dir);
				if(RepositoryObjectType.TRANSFORMATION.getTypeDescription().equalsIgnoreCase(jsonObject.optString("type"))) {
					TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);
					String xml = XMLHandler.getXMLHeader() + "\n" + transMeta.getXML();
					out.putNextEntry(new ZipEntry(entryPath + RepositoryObjectType.TRANSFORMATION.getExtension()));
					out.write(xml.getBytes(Const.XML_ENCODING));
				} else if(RepositoryObjectType.JOB.getTypeDescription().equalsIgnoreCase(jsonObject.optString("type"))) {
					JobMeta jobMeta = repository.loadJob(name, directory, null, null);
					String xml = XMLHandler.getXMLHeader() + "\n" + jobMeta.getXML();
					out.putNextEntry(new ZipEntry(entryPath + RepositoryObjectType.JOB.getExtension()));
					out.write(xml.getBytes(Const.XML_ENCODING));
				}

			}


			JsonUtils.success(StringEscapeHelper.encode(file.getAbsolutePath()));

		}catch (Exception e){
			throw new AppendableException(e);
		}finally {
			if(out!=null){
				out.close();
			}
			if(fos !=null){
				fos.close();
			}
			if(repository != null){
				repository.disconnect();
			}

		}


	}

	private boolean singleImport(Repository repository, RepositoryDirectoryInterface parent, String fileName, InputStream is,@ApiIgnore @CurrentUser CurrentUserResponse user) {
		try {
			Document doc = XMLHandler.loadXMLFile(is);
			Element root = doc.getDocumentElement();

			if(fileName.endsWith(RepositoryObjectType.TRANSFORMATION.getExtension())) {

				TransMeta transMeta = new TransMeta();
				transMeta.loadXML(root, null, App.getInstance().getMetaStore(), repository, true, new Variables(), null);
				for(StepMeta stepMeta : transMeta.getSteps()) {
					if(stepMeta.getStepMetaInterface() instanceof MissingTrans) {
						System.out.println("......导入失败" + fileName + "，无法识别的转换组件：" + stepMeta.getName());
						return false;
					}
				}


				boolean flag = repository.exists(transMeta.getName(), parent, RepositoryObjectType.TRANSFORMATION);
			    if(flag) return false;

				transMeta.setRepositoryDirectory( parent );
			    transMeta.setTransstatus(-1);
			    transMeta.setModifiedDate(new Date());
				List<DatabaseMeta> list = transMeta.getDatabases();
				for(DatabaseMeta meta : list){
					meta.setCreateUser(user.getUsername());
					meta.setUpdateUser(user.getUsername());
					meta.setOrganizerId(user.getOrganizerId());
				}

			    repository.save(transMeta, "初次导入", null);
			} else if(fileName.endsWith(RepositoryObjectType.JOB.getExtension())) {
				JobMeta jobMeta = new JobMeta();
				jobMeta.loadXML(root, repository, null);

				for(JobEntryCopy jobEntryCopy : jobMeta.getJobCopies()) {
					if(jobEntryCopy.getEntry() instanceof MissingEntry) {
						System.out.println("......导入失败" + fileName + "，无法识别的作业组件：" + jobEntryCopy.getName());
						return false;
					}
				}

				boolean flag = repository.exists(jobMeta.getName(), parent, RepositoryObjectType.JOB);
			    if(flag) return false;


				jobMeta.setRepositoryDirectory(parent);
				jobMeta.setJobstatus(-1);
				jobMeta.setModifiedDate(new Date());

				repository.save(jobMeta, "初次导入", null);
			}

			return true;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

		return false;
	}

	/**
	 * 多文件导入，可以一次导入多个ktr或kjb文件
	 * @param repositoryCurrentDir 当前资源库目录
	 * @param filesPath 文件路径，可以是多个，文件需要先调用上传接口
	 * @param user
	 * @throws AppendableException
	 */
	@ApiOperation(value = "多文件导入，可以一次导入多个ktr或kjb文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "repositoryCurrentDir", value = "当前资源库目录", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "filesPath", value = "文件路径，可以是多个，文件需要先调用上传接口", paramType="query", dataType = "string")
	})
	@RequestMapping("/multiImport")
	protected @ResponseBody void multiImport(@RequestParam String repositoryCurrentDir, @RequestParam String filesPath, @ApiIgnore @CurrentUser CurrentUserResponse user) throws AppendableException {
		ArrayList list = new ArrayList();
		Repository repository = App.getInstance().getRepository();

		try {
			String root = org.firzjb.base.common.Const.getRootPath(user.getOrganizerId());
			if(StringUtils.hasText(repositoryCurrentDir)) {
				if(!repositoryCurrentDir.startsWith(root)) {
					repositoryCurrentDir = org.firzjb.base.common.Const.getUserPath(user.getOrganizerId(), repositoryCurrentDir);
				}
			} else {
				repositoryCurrentDir = root;
			}



			RepositoryDirectoryInterface dir = repository.findDirectory(repositoryCurrentDir);
			if(dir == null) {
				JsonUtils.fail("目录【" + repositoryCurrentDir + "】加载失败！");
				return;
			}

			JSONArray jsonArray = JSONArray.fromObject(filesPath);
			for(int i=0; i<jsonArray.size(); i++) {
				String filePath = jsonArray.getString(i);

				File file = new File(filePath);
				if(file.isFile()) {
					RepositoryDirectoryInterface parent = repository.findDirectory(repositoryCurrentDir);
					if(parent != null) {
						if(!singleImport(repository, parent, file.getName(), FileUtils.openInputStream(file),user)) {
							list.add(file.getName() + " 导入失败！请检查本目录下是否已存在该对象！");
						}
					}

					file.delete();
				}
			}


			if(list.size() > 0) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("success", false);
				jsonObject.put("files", list);
				JsonUtils.response(jsonObject);
			} else {
				JsonUtils.success("导入成功！");
			}
		}catch (Exception e){
			throw new AppendableException(e);
		}finally {
			if(repository !=null){
				repository.disconnect();
			}
		}





	}


	/**
	 * 导入
	 * @param filePath
	 * @param user
	 * @throws KettleException
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.POST, value="/imp")
	protected @ResponseBody void imp(@RequestParam String filePath, @ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		File file = new File(filePath);
		ZipFile zip = new ZipFile(file);

		ArrayList list = new ArrayList();
		Repository repository = App.getInstance().getRepository();

		try {
			Enumeration<? extends ZipEntry> enumeration = zip.entries();
			while(enumeration.hasMoreElements()) {
				ZipEntry entry = enumeration.nextElement();

            	if(entry.isDirectory())
            		continue;

                String entryFileName = entry.getName();
                String fileName = entryFileName;
                RepositoryDirectoryInterface parent = repository.getUserHomeDirectory();
                if(entryFileName.indexOf("/") > 0) {
                	List<String> paths = Lists.newArrayList(entryFileName.split("/"));
                	fileName = paths.remove(paths.size() - 1);

                	for(String dir : paths) {
                		RepositoryDirectoryInterface child = parent.findChild(dir);
                		if(child == null) {
                			child = repository.createRepositoryDirectory(parent, dir);
                		}
                		parent = child;
                	}

                }

    			if(!singleImport(repository, parent, fileName, zip.getInputStream(entry),user)) {
					list.add(entryFileName + " 导入失败！请检查资源库中是否已存在该对象！");
				}

            }
		} finally {
			zip.close();
			repository.disconnect();
		}


		if(list.size() > 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("success", false);
			jsonObject.put("files", list);
			JsonUtils.response(jsonObject);
		} else {
			JsonUtils.success("导入成功！");
		}
	}

	/**
	 *
	 * @param filePath
	 * @return
	 * @throws KettleException
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.POST, value="/imptree")
	protected @ResponseBody List imptree(@RequestParam String filePath) throws KettleException, IOException {
		FileInputStream fis = new FileInputStream(new File(filePath));
		ZipInputStream is = new ZipInputStream(fis);

		Repository repository = App.getInstance().getRepository();

		try {

			ArrayList<RepositoryCheckNode> list = new ArrayList<RepositoryCheckNode>();
			ZipFile zip = new ZipFile(new File(filePath));
			Enumeration<ZipEntry> iter = (Enumeration<ZipEntry>) zip.entries();
			while(iter.hasMoreElements()) {
				List<RepositoryCheckNode> temp = list;
				ZipEntry entry = iter.nextElement();

				if(entry.isDirectory())
					continue;

				String[] strings = entry.getName().split("/");
				String currentDir = "";
				for(int i=0; i<strings.length; i++) {
					currentDir += "/" + strings[i];

					boolean found = false;
					for(RepositoryCheckNode node : temp) {
						if(node.getText().equals(strings[i])) {
							temp = node.getChildren();
							found = true;
							break;
						}
					}

					if(!found) {
						RepositoryCheckNode node = null;
						if(i == (strings.length - 1)) {
							if(strings[i].endsWith(RepositoryObjectType.TRANSFORMATION.getExtension())) {
								node = RepositoryCheckNode.initNode(strings[i], currentDir, RepositoryObjectType.TRANSFORMATION, true);

								String parentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
								RepositoryDirectoryInterface dir = repository.findDirectory(parentDir);
								String name = strings[i].substring(0, strings[i].lastIndexOf("."));
								if(dir != null) {
									node.setRepoExist(repository.exists(name, dir, RepositoryObjectType.TRANSFORMATION));
									node.setChecked(true);
								} else {
									node.setRepoExist(false);
								}

							} else if(strings[i].endsWith(RepositoryObjectType.JOB.getExtension())) {
								node = RepositoryCheckNode.initNode(strings[i], currentDir, RepositoryObjectType.JOB, true);

								String parentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
								RepositoryDirectoryInterface dir = repository.findDirectory(parentDir);
								String name = strings[i].substring(0, strings[i].lastIndexOf("."));
								if(dir != null) {
									node.setRepoExist(repository.exists(name, dir, RepositoryObjectType.JOB));
								} else {
									node.setRepoExist(false);
								}
							}
						} else {
							node = RepositoryCheckNode.initNode(strings[i], currentDir);
						}
						temp.add(node);
						temp = node.getChildren();
					}
				}

			}



			return list;

		}catch (Exception e){
			throw e;
		}finally {
			is.close();
			fis.close();
			repository.disconnect();
		}


	}

	@RequestMapping(method=RequestMethod.POST, value="/cascader")
	protected Response<List<RepositoryCascaderVO>> cascader(@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();

		try {

			//List<RepositoryCascaderVO> list = new ArrayList<>();

			String root = org.firzjb.base.common.Const.getRootPath(user.getOrganizerId());
			RepositoryDirectoryInterface dir = repository.findDirectory(root);

			//RepositoryCascaderVO repositoryCascaderVO = new RepositoryCascaderVO(user.getOrganizerName(),user.getOrganizerId().toString());

			List<RepositoryCascaderVO> childs = getCascaderChildren(repository,dir);
		/*if(!childs.isEmpty()){
			repositoryCascaderVO.setChildren(childs);
			list.add(repositoryCascaderVO);
		}*/

			return Response.ok(childs);

		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}


	}

	private List<RepositoryCascaderVO> getCascaderChildren(Repository repository, RepositoryDirectoryInterface dir) throws KettleException {
		List<RepositoryCascaderVO> list = new ArrayList<>();
		List<RepositoryElementMetaInterface> elements = repository.getTransformationObjects(dir.getObjectId(), false);
		List<RepositoryDirectoryInterface> directorys = dir.getChildren();
		if(elements != null) {
			for(RepositoryElementMetaInterface e : elements) {
				RepositoryCascaderVO ro = new RepositoryCascaderVO(e,".ktr");
				list.add(ro);
			}
		}

		elements = repository.getJobObjects(dir.getObjectId(), false);
		if(elements != null) {
			for(RepositoryElementMetaInterface e : elements) {
				RepositoryCascaderVO ro = new RepositoryCascaderVO(e,".kjb");
				list.add(ro);
			}
		}

		for(RepositoryDirectoryInterface child : directorys) {
			RepositoryCascaderVO ro = new RepositoryCascaderVO(child);
			List<RepositoryCascaderVO> childs = getCascaderChildren(repository,child);
			if(!childs.isEmpty()){
				ro.setChildren(childs);
				list.add(ro);
			}
		}

		return list;


	}


	@RequestMapping(method=RequestMethod.POST, value="/exptree")
	protected @ResponseBody List exptree(@RequestParam int loadElement,@ApiIgnore @CurrentUser CurrentUserResponse user) throws KettleException, IOException {
		Repository repository = App.getInstance().getRepository();
		try {
			String root = org.firzjb.base.common.Const.getRootPath(user.getOrganizerId());
			RepositoryDirectoryInterface dir = repository.findDirectory(root);
			List list = browser(repository, dir, loadElement);
			return list;
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}




	private List browser(Repository repository, RepositoryDirectoryInterface dir, int loadElement) throws KettleException {
		ArrayList list = new ArrayList();

		List<RepositoryDirectoryInterface> directorys = dir.getChildren();
		for(RepositoryDirectoryInterface child : directorys) {
//			RepositoryCheckNode node = new RepositoryCheckNode(child.getName());
//			node.setChildren(browser(repository, child, loadElement));
//			node.setPath(child.getPath());
			list.add(RepositoryCheckNode.initNode(child.getName(), child.getPath(), browser(repository, child, loadElement)));
		}

		if(RepositoryNodeType.includeTrans(loadElement)) {
			List<RepositoryElementMetaInterface> elements = repository.getTransformationObjects(dir.getObjectId(), false);
			if(elements != null) {
				for(RepositoryElementMetaInterface e : elements) {
					String transPath = dir.getPath();
					if(!transPath.endsWith("/"))
						transPath = transPath + '/';
					transPath = transPath + e.getName();

					list.add(RepositoryCheckNode.initNode(e.getName(), transPath, e.getObjectType()));

				}
			}
		}

		if(RepositoryNodeType.includeJob(loadElement)) {
			List<RepositoryElementMetaInterface> elements = repository.getJobObjects(dir.getObjectId(), false);
			if(elements != null) {
				for(RepositoryElementMetaInterface e : elements) {
					String transPath = dir.getPath();
					if(!transPath.endsWith("/"))
						transPath = transPath + '/';
					transPath = transPath + e.getName();

					list.add(RepositoryCheckNode.initNode(e.getName(), transPath, e.getObjectType()));
				}
			}
		}

		return list;
	}

	@ApiOperation(value = "返回资源库中所有的子服务器信息")
	@ResponseBody
	@RequestMapping("/slaveservers")
	protected void slaveservers() throws IOException, KettleException {
		Repository repository = App.getInstance().getRepository();

		try {

			ObjectId[] slaveIDs = repository.getSlaveIDs(false);
			JSONArray jsonArray = new JSONArray();
			for(ObjectId id_slave: slaveIDs) {
				SlaveServer slaveServer = repository.loadSlaveServer(id_slave, null);
				jsonArray.add(SlaveServerCodec.encode(slaveServer));
			}

			JsonUtils.response(jsonArray);

		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}


	}

	@ApiOperation(value = "返回资源库中所有的数据库连接信息")
	@RequestMapping("/databases")
	protected @ResponseBody void databases() throws IOException, KettleException {
		Repository repository = App.getInstance().getRepository();
		try {
			ObjectId[] databaseIds = repository.getDatabaseIDs(false);
			JSONArray jsonArray = new JSONArray();
			for(ObjectId databaseId: databaseIds) {
				DatabaseMeta databaseMeta = repository.loadDatabaseMeta(databaseId, null);
				JSONObject jsonObject = DatabaseCodec.encode(databaseMeta);
				jsonObject.put("changedDate", XMLHandler.date2string(databaseMeta.getChangedDate()));
				jsonArray.add(jsonObject);
			}

			JsonUtils.response(jsonArray);
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}

	@ApiOperation(value = "返回资源库中所有的分区数据库连接信息")
	@RequestMapping("/partitionDatabases")
	protected @ResponseBody void partitionDatabases() throws IOException, KettleException {
		Repository repository = App.getInstance().getRepository();

		try {
			ObjectId[] databaseIds = repository.getDatabaseIDs(false);
			JSONArray jsonArray = new JSONArray();
			for(ObjectId databaseId: databaseIds) {
				DatabaseMeta databaseMeta = repository.loadDatabaseMeta(databaseId, null);

				if(databaseMeta.isPartitioned()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", databaseMeta.getName());
					jsonArray.add(jsonObject);
				}

			}

			JsonUtils.response(jsonArray);
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}

	@ApiOperation(value = "返回资源库中所有的数据库连接信息，包含连接是否可用的状态")
	@RequestMapping("/databaseStatus")
	protected @ResponseBody Collection databaseStatus() throws IOException, KettleException, InterruptedException, ExecutionException {
		Repository repository = App.getInstance().getRepository();

		try {
			ObjectId[] databaseIds = repository.getDatabaseIDs(false);
			ExecutorService executor = Executors.newCachedThreadPool();

			HashMap<String, JSONObject> result = new HashMap<String, JSONObject>();
			HashMap<String, Future<Integer>> dbStatus = new HashMap<String, Future<Integer>>();
			for(ObjectId databaseId: databaseIds) {
				DatabaseMeta databaseMeta = repository.loadDatabaseMeta(databaseId, null);
				JSONObject jsonObject = DatabaseCodec.encode(databaseMeta);
				result.put(databaseMeta.getName(), jsonObject);

				String port = databaseMeta.getDatabasePortNumberString();
				Future<Integer> f = executor.submit(new ServerChecker(databaseMeta.getHostname(), Integer.parseInt(port)));
				dbStatus.put(databaseMeta.getName(), f);
			}

			for(Map.Entry<String, Future<Integer>> entry : dbStatus.entrySet()) {
				Integer status = entry.getValue().get();
				result.get(entry.getKey()).put("status", status);
			}

			return result.values();
		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}


	}

	/**
	 *
	 * @throws IOException
	 * @throws KettleException
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus")
	protected void changeStatus(String path, int status) throws IOException, KettleException {
		if(path.endsWith(RepositoryObjectType.TRANSFORMATION.getExtension())) {
			TransMeta transMeta = RepositoryUtils.readTrans(path);
			transMeta.setTransstatus(status);
			App.getInstance().getRepository().save(transMeta, "更新转换状态：" + status, null);

			JsonUtils.success("操作成功！");
		} else if(path.endsWith(RepositoryObjectType.JOB.getExtension())) {
			JobMeta jobMeta = RepositoryUtils.readJob(path);
			jobMeta.setJobstatus(status);
			App.getInstance().getRepository().save(jobMeta, "更新作业状态：" + status, null);

			JsonUtils.success("操作成功！");
		}


		JsonUtils.fail("无法识别的类型！");
	}

	/**
	 * 断开资源库
	 *
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/logout")
	protected void logout() throws IOException {
//		App.getInstance().selectRepository(App.getInstance().getDefaultRepository());
		JsonUtils.session().invalidate();
		JsonUtils.success("操作成功！");
	}

}
