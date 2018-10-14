package com.aofei.kettle.core.repository;

import com.alibaba.fastjson.JSONObject;
import com.aofei.base.common.Const;
import com.aofei.kettle.core.database.DatabaseCodec;
import com.aofei.kettle.model.response.RepositoryDatabaseResponse;
import com.aofei.kettle.model.response.RepositoryResponse;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

public class RepositoryCodec {

	public static JSONObject encode(RepositoryMeta repositoryMeta) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", repositoryMeta.getName());
		jsonObject.put("description", repositoryMeta.getDescription());
		jsonObject.put("type", repositoryMeta.getId());
		
		if(repositoryMeta instanceof KettleDatabaseRepositoryMeta) {
			KettleDatabaseRepositoryMeta input = (KettleDatabaseRepositoryMeta) repositoryMeta;
			JSONObject extraOptions = new JSONObject();
			extraOptions.put("database", input.getConnection().getName());
			jsonObject.put("extraOptions", extraOptions);
		} else if(repositoryMeta instanceof KettleFileRepositoryMeta) {
			KettleFileRepositoryMeta input = (KettleFileRepositoryMeta) repositoryMeta;
			
			JSONObject extraOptions = new JSONObject();
			extraOptions.put("basedir", input.getBaseDirectory());
			extraOptions.put("hidingHidden", input.isHidingHiddenFiles() ? "Y" : "N");
			extraOptions.put("readOnly", input.isReadOnly() ? "Y" : "N");
			jsonObject.put("extraOptions", extraOptions);
		}
		
		return jsonObject;
	}
	
	public static RepositoryMeta decode(JSONObject jsonObject) throws KettleException {
		String id = jsonObject.getString("type");
		RepositoryMeta repositoryMeta = PluginRegistry.getInstance().loadClass( RepositoryPluginType.class, id, RepositoryMeta.class );
		repositoryMeta.setName(jsonObject.getString("name"));
		repositoryMeta.setDescription(jsonObject.getString("description"));
		repositoryMeta.setDefault(jsonObject.getBooleanValue("is_default"));
		/*if(repositoryMeta instanceof KettleDatabaseRepositoryMeta) {
			KettleDatabaseRepositoryMeta kettleDatabaseRepositoryMeta = (KettleDatabaseRepositoryMeta) repositoryMeta;

			RepositoriesMeta input = new RepositoriesMeta();
			if (input.readData()) {
				DatabaseMeta connection = input.searchDatabase(jsonObject.getJSONObject("extraOptions").getString("database"));
				kettleDatabaseRepositoryMeta.setConnection(connection);
			}
		} else if(repositoryMeta instanceof KettleFileRepositoryMeta) {
			KettleFileRepositoryMeta input = (KettleFileRepositoryMeta) repositoryMeta;

			JSONObject extraOptions = jsonObject.getJSONObject("extraOptions");
			input.setBaseDirectory(extraOptions.getString("basedir"));
			input.setReadOnly("Y".equalsIgnoreCase(extraOptions.getString("readOnly")));
			input.setHidingHiddenFiles("Y".equalsIgnoreCase(extraOptions.getString("hidingHidden")));
		}*/
		
		return repositoryMeta;
	}

	public static Repository decode(RepositoryResponse jsonObject, RepositoryDatabaseResponse databaseResponse) throws KettleException {
		KettleDatabaseRepositoryMeta repositoryMeta = (KettleDatabaseRepositoryMeta) PluginRegistry.getInstance().loadClass( RepositoryPluginType.class, "", RepositoryMeta.class );
		repositoryMeta.setName(jsonObject.getRepositoryName());
		repositoryMeta.setDescription(jsonObject.getDescription());
		repositoryMeta.setDefault(jsonObject.getIsDefault() !=null && jsonObject.getIsDefault() ==1);

		DatabaseMeta databaseMeta = DatabaseCodec.decode(databaseResponse);
		repositoryMeta.setConnection(databaseMeta);

		KettleDatabaseRepository databaseRepository = new KettleDatabaseRepository();
		databaseRepository.init(repositoryMeta);
		databaseRepository.connect(Const.REPOSITORY_USERNAME,Const.REPOSITORY_PASSWORD);

		return databaseRepository;
	}
}
