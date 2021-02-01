package com.aofei.kettle.trans.steps.addsequence;

import com.aofei.kettle.App;
import com.aofei.kettle.utils.JSONObject;
import com.aofei.kettle.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Transformation转换 - 添加序列 - 接口api
 *
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@RestController
@RequestMapping(value = "/sequence")
@Api(tags = "Transformation转换 - 添加序列 - 接口api")
public class AddSequenceController {

	/**
	 * 校验数据库是否支持序列
	 * @param name 数据库连接名称
	 * @throws IOException
	 * @throws KettleException
	 */
	@ApiOperation(value = "校验数据库是否支持序列")
	@RequestMapping(method = RequestMethod.POST, value = "/support")
	protected @ResponseBody void support(String name) throws IOException, KettleException {
		Repository repository = App.getInstance().getRepository();
		try {

			ObjectId id_database = repository.getDatabaseID(name);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("support_sequence", false);
			if (id_database != null) {
				DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);
				boolean supportsSequences = databaseMeta.supportsSequences();
				jsonObject.put("support_sequence", supportsSequences);
			}

			repository.disconnect();
			JsonUtils.response(jsonObject);

		}catch (Exception e){
			throw e;
		}finally {
			repository.disconnect();
		}

	}

	/**
	 *  加载数据库中的序列
	 * @param name 数据库名称连接
	 * @return
	 * @throws IOException
	 * @throws KettleException
	 */
	@ApiOperation(value = "加载数据库中的序列")
	@RequestMapping(method = RequestMethod.POST, value = "/sequences")
	protected @ResponseBody List sequences(String name) throws IOException, KettleException {
		Repository repository = App.getInstance().getRepository();
		ObjectId id_database = repository.getDatabaseID(name);


		ArrayList list = new ArrayList();
		if (id_database != null) {
			DatabaseMeta databaseMeta = repository.loadDatabaseMeta(id_database, null);

			Database database = new Database(loggingObject, databaseMeta);
			try {
				database.connect();
				String[] sequences = database.getSequences();

				if (null != sequences && sequences.length > 0) {
					sequences = Const.sortStrings(sequences);

					for(String sequence : sequences) {
						HashMap rec = new HashMap();
						rec.put("name", sequence);
						list.add(rec);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				repository.disconnect();
				if (database != null) {
					database.disconnect();
					database = null;
				}
			}

		}

		return list;
	}

	public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("AddSequenceController", LoggingObjectType.DATABASE, null );

}
