package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import org.firzjb.kettle.utils.ReflectUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

@Component("MongoDbOutput")
@Scope("prototype")
public class MongoDbOutput extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		ReflectUtils.set(stepMetaInterface, "hostnames", cell.getAttribute("hostnames"));
		ReflectUtils.set(stepMetaInterface, "port", cell.getAttribute("port"));
		ReflectUtils.set(stepMetaInterface, "useSSLSocketFactory", "Y".equalsIgnoreCase(cell.getAttribute("useSSLSocketFactory")));
		ReflectUtils.set(stepMetaInterface, "useAllReplicaSetMembers", "Y".equalsIgnoreCase(cell.getAttribute("useAllReplicaSetMembers")));
		ReflectUtils.set(stepMetaInterface, "authenticationDatabaseName", cell.getAttribute("authenticationDatabaseName"));
		ReflectUtils.set(stepMetaInterface, "authenticationUser", cell.getAttribute("authenticationUser"));
		ReflectUtils.set(stepMetaInterface, "authenticationPassword", cell.getAttribute("authenticationPassword"));
		ReflectUtils.set(stepMetaInterface, "authenticationMechanism", cell.getAttribute("authenticationMechanism"));
		ReflectUtils.set(stepMetaInterface, "useKerberosAuthentication", "Y".equalsIgnoreCase(cell.getAttribute("useKerberosAuthentication")));
		ReflectUtils.set(stepMetaInterface, "connectTimeout", cell.getAttribute("connectTimeout"));
		ReflectUtils.set(stepMetaInterface, "socketTimeout", cell.getAttribute("socketTimeout"));

		ReflectUtils.set(stepMetaInterface, "dbName", cell.getAttribute("dbName"));
		ReflectUtils.set(stepMetaInterface, "collection", cell.getAttribute("collection"));
		ReflectUtils.set(stepMetaInterface, "batchInsertSize", cell.getAttribute("batchInsertSize"));
		ReflectUtils.set(stepMetaInterface, "truncate", "Y".equalsIgnoreCase(cell.getAttribute("truncate")));
		ReflectUtils.set(stepMetaInterface, "update", "Y".equalsIgnoreCase(cell.getAttribute("update")));
		ReflectUtils.set(stepMetaInterface, "upsert", "Y".equalsIgnoreCase(cell.getAttribute("upsert")));
		ReflectUtils.set(stepMetaInterface, "multi", "Y".equalsIgnoreCase(cell.getAttribute("multi")));
		ReflectUtils.set(stepMetaInterface, "modifierUpdate", "Y".equalsIgnoreCase(cell.getAttribute("modifierUpdate")));
		ReflectUtils.set(stepMetaInterface, "writeConcern", cell.getAttribute("writeConcern"));
		ReflectUtils.set(stepMetaInterface, "wTimeout", cell.getAttribute("wTimeout"));
		ReflectUtils.set(stepMetaInterface, "journal", "Y".equalsIgnoreCase(cell.getAttribute("journal")));
		ReflectUtils.set(stepMetaInterface, "readPreference", cell.getAttribute("readPreference"));
		ReflectUtils.set(stepMetaInterface, "writeRetries", cell.getAttribute("writeRetries"));
		ReflectUtils.set(stepMetaInterface, "writeRetryDelay", cell.getAttribute("writeRetryDelay"));


		Class clazz = ReflectUtils.getInnerClazz(stepMetaInterface.getClass(), "MongoField");
		JSONArray fieldsArray = JSONArray.fromObject(cell.getAttribute("mongoFields"));
		ArrayList mongoFields = new ArrayList();
		for(int i=0; i<fieldsArray.size(); i++) {
			JSONObject jsonObject = fieldsArray.getJSONObject(i);

			Object mongoField = clazz.newInstance();
			ReflectUtils.setFieldValue(mongoField, "m_incomingFieldName", jsonObject.optString( "incoming_field_name" ) );
			ReflectUtils.setFieldValue(mongoField, "m_mongoDocPath", jsonObject.optString( "mongo_doc_path" ) );
			ReflectUtils.setFieldValue(mongoField, "m_useIncomingFieldNameAsMongoFieldName", "Y".equalsIgnoreCase(jsonObject.optString( "use_incoming_field_name_as_mongo_field_name" )) );
			ReflectUtils.setFieldValue(mongoField, "insertNull", "Y".equalsIgnoreCase(jsonObject.optString( "allow_null" )) );
			ReflectUtils.setFieldValue(mongoField, "m_JSON", "Y".equalsIgnoreCase(jsonObject.optString( "json_field" )) );
			ReflectUtils.setFieldValue(mongoField, "m_updateMatchField", "Y".equalsIgnoreCase(jsonObject.optString( "update_match_field" )));
			ReflectUtils.setFieldValue(mongoField, "m_modifierUpdateOperation", jsonObject.optString( "modifier_update_operation" ) );
			ReflectUtils.setFieldValue(mongoField, "m_modifierOperationApplyPolicy", jsonObject.optString( "modifier_policy" ) );

			mongoFields.add(mongoField);
		}
		ReflectUtils.set(stepMetaInterface, "mongoFields", mongoFields);

		clazz = clazz = ReflectUtils.getInnerClazz(stepMetaInterface.getClass(), "MongoIndex");
		JSONArray indexArray = JSONArray.fromObject(cell.getAttribute("mongoIndexes"));
		ArrayList mongoIndexes = new ArrayList();
		for(int i=0; i<indexArray.size(); i++) {
			JSONObject jsonObject = indexArray.getJSONObject(i);

			Object mongoIndex = clazz.newInstance();
			ReflectUtils.setFieldValue(mongoIndex, "m_pathToFields", jsonObject.optString( "path_to_fields" ) );
			ReflectUtils.setFieldValue(mongoIndex, "m_drop", "Y".equalsIgnoreCase(jsonObject.optString( "drop" )) );
			ReflectUtils.setFieldValue(mongoIndex, "m_unique", "Y".equalsIgnoreCase(jsonObject.optString( "unique" )) );
			ReflectUtils.setFieldValue(mongoIndex, "m_sparse", "Y".equalsIgnoreCase(jsonObject.optString( "sparse" )) );
			mongoIndexes.add(mongoIndex);
		}
		ReflectUtils.set(stepMetaInterface, "mongoIndexes", mongoIndexes);

	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		//configuration connection
		e.setAttribute("hostnames", ReflectUtils.getString(stepMetaInterface, "hostnames"));
		e.setAttribute("port", ReflectUtils.getString(stepMetaInterface, "port"));
		e.setAttribute("useSSLSocketFactory", ReflectUtils.getBoolean(stepMetaInterface, "useSSLSocketFactory") ? "Y" : "N");
		e.setAttribute("useAllReplicaSetMembers", ReflectUtils.getBoolean(stepMetaInterface, "useAllReplicaSetMembers") ? "Y" : "N");
		e.setAttribute("authenticationDatabaseName", ReflectUtils.getString(stepMetaInterface, "authenticationDatabaseName"));
		e.setAttribute("authenticationUser", ReflectUtils.getString(stepMetaInterface, "authenticationUser"));
		e.setAttribute("authenticationPassword", ReflectUtils.getString(stepMetaInterface, "authenticationPassword"));
		e.setAttribute("authenticationMechanism", ReflectUtils.getString(stepMetaInterface, "authenticationMechanism"));
		e.setAttribute("useKerberosAuthentication", ReflectUtils.getBoolean(stepMetaInterface, "useKerberosAuthentication") ? "Y" : "N");
		e.setAttribute("connectTimeout", ReflectUtils.getString(stepMetaInterface, "connectTimeout"));
		e.setAttribute("socketTimeout", ReflectUtils.getString(stepMetaInterface, "socketTimeout"));

		//output options
		e.setAttribute("dbName", ReflectUtils.getString(stepMetaInterface, "dbName"));
		e.setAttribute("collection", ReflectUtils.getString(stepMetaInterface, "collection"));
		e.setAttribute("batchInsertSize", ReflectUtils.getString(stepMetaInterface, "batchInsertSize"));
		e.setAttribute("truncate", ReflectUtils.getBoolean(stepMetaInterface, "truncate") ? "Y" : "N");
		e.setAttribute("update", ReflectUtils.getBoolean(stepMetaInterface, "update") ? "Y" : "N");
		e.setAttribute("upsert", ReflectUtils.getBoolean(stepMetaInterface, "upsert") ? "Y" : "N");
		e.setAttribute("multi", ReflectUtils.getBoolean(stepMetaInterface, "multi") ? "Y" : "N");
		e.setAttribute("modifierUpdate", ReflectUtils.getBoolean(stepMetaInterface, "modifierUpdate") ? "Y" : "N");
		e.setAttribute("writeConcern", ReflectUtils.getString(stepMetaInterface, "writeConcern"));
		e.setAttribute("wTimeout", ReflectUtils.getString(stepMetaInterface, "wTimeout"));
		e.setAttribute("journal", ReflectUtils.getBoolean(stepMetaInterface, "journal") ? "Y" : "N");
		e.setAttribute("readPreference", ReflectUtils.getString(stepMetaInterface, "readPreference"));
		e.setAttribute("writeRetries", ReflectUtils.getString(stepMetaInterface, "writeRetries"));
		e.setAttribute("writeRetryDelay", ReflectUtils.getString(stepMetaInterface, "writeRetryDelay"));


		//document fields
		List mongoFields = (List) ReflectUtils.get(stepMetaInterface, "mongoFields");
		JSONArray jsonArray = new JSONArray();
	    if(mongoFields != null) {
	    	for(Object mongoField: mongoFields) {
	    		JSONObject jsonObject = new JSONObject();
				jsonObject.put("incoming_field_name", ReflectUtils.getFieldValue(mongoField, "m_incomingFieldName"));
				jsonObject.put("mongo_doc_path", ReflectUtils.getFieldValue(mongoField, "m_mongoDocPath"));
				jsonObject.put("use_incoming_field_name_as_mongo_field_name", ReflectUtils.getFieldBoolean(mongoField, "m_useIncomingFieldNameAsMongoFieldName") ? "Y" : "N");
				jsonObject.put("allow_null", ReflectUtils.getFieldBoolean(mongoField, "insertNull") ? "Y" : "N");
				jsonObject.put("json_field", ReflectUtils.getFieldBoolean(mongoField, "m_JSON") ? "Y" : "N");

				jsonObject.put("update_match_field", ReflectUtils.getFieldBoolean(mongoField, "m_updateMatchField") ? "Y" : "N");
				jsonObject.put("modifier_update_operation", ReflectUtils.getFieldValue(mongoField, "m_modifierUpdateOperation"));
				jsonObject.put("modifier_policy", ReflectUtils.getFieldValue(mongoField, "m_modifierOperationApplyPolicy"));

				jsonArray.add(jsonObject);
	    	}
	    }
	    e.setAttribute("mongoFields", jsonArray.toString());


	    List mongoIndexes = (List) ReflectUtils.get(stepMetaInterface, "mongoIndexes");
		jsonArray = new JSONArray();
	    if(mongoIndexes != null) {
	    	for(Object mongoIndex: mongoIndexes) {
	    		JSONObject jsonObject = new JSONObject();
				jsonObject.put("path_to_fields", ReflectUtils.getFieldValue(mongoIndex, "m_pathToFields"));
				jsonObject.put("drop", ReflectUtils.getFieldBoolean(mongoIndex, "m_drop") ? "Y" :"N");
				jsonObject.put("unique", ReflectUtils.getFieldBoolean(mongoIndex, "m_unique") ? "Y" :"N");
				jsonObject.put("sparse", ReflectUtils.getFieldBoolean(mongoIndex, "m_sparse") ? "Y" :"N");
				jsonArray.add(jsonObject);
	    	}
	    }
	    e.setAttribute("mongoIndexes", jsonArray.toString());

		return e;
	}

}
