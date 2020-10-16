package com.aofei.kettle.trans.steps.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.PluginFactory;
import com.aofei.kettle.base.GraphCodec;
import com.aofei.kettle.utils.JSONObject;
import com.aofei.kettle.utils.JsonUtils;
import com.aofei.kettle.utils.ReflectUtils;

@RestController
@RequestMapping(value = "/mongodb")
public class MongodbController {

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/setupDBNames")
	public void setupDBNames(@RequestParam String graphXml, @RequestParam String stepName,
			@CurrentUser CurrentUserResponse user) throws Exception {
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class PKG = stepMetaInterface.getClass();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);

		String hostname = ReflectUtils.getString(stepMetaInterface, "hostnames");
		if (!Const.isEmpty(hostname)) {
			try {
				List<String> dbNames = new ArrayList<String>();

				Class clazz = Class.forName("org.pentaho.mongo.wrapper.MongoWrapperUtil", true,
						stepMetaInterface.getClass().getClassLoader());
				Object wrapper = ReflectUtils.callStatic(clazz, "createMongoClientWrapper", stepMetaInterface,
						transMeta, null);
				try {
					dbNames = (List<String>) ReflectUtils.get(wrapper, "databaseNames");
				} finally {
					ReflectUtils.call(wrapper, "dispose");
				}
				
				ArrayList list = new ArrayList();
				for (String s : dbNames) {
					HashMap rec = new HashMap();
					rec.put("name", s);
					list.add(rec);
				}
				
				jsonObject.put("success", true);
				jsonObject.put("data", list);
			} catch (Exception e) {
				e.printStackTrace();
				String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.UnableToConnect", "host name(s)"); 
				jsonObject.put("message", message);
			}
		} else {
			 String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.MissingConnectionDetails", "host name(s)"); 
			 jsonObject.put("message", message);
		}
		
		
		JsonUtils.response(jsonObject);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/setupCollectionNamesForDB")
	public void setupCollectionNamesForDB(@RequestParam String graphXml, @RequestParam String stepName,
			@CurrentUser CurrentUserResponse user) throws Exception {
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class PKG = stepMetaInterface.getClass();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);
		
		String hostname = transMeta.environmentSubstitute(ReflectUtils.getString(stepMetaInterface, "hostnames"));
		String dB = transMeta.environmentSubstitute( ReflectUtils.getString(stepMetaInterface, "dbName") );
		
		
		if ( !Const.isEmpty( hostname ) && !Const.isEmpty( dB ) ) {
			try {
				Class clazz = Class.forName("org.pentaho.mongo.wrapper.MongoWrapperUtil", true,
						stepMetaInterface.getClass().getClassLoader());
				Object wrapper = ReflectUtils.callStatic(clazz, "createMongoClientWrapper", stepMetaInterface,
						transMeta, null);
				
				Set<String> collections = new HashSet<String>();
		        try {
		        	collections = (Set<String>) ReflectUtils.call(wrapper, "getCollectionsNames", dB);
		        } finally {
		        	ReflectUtils.call(wrapper, "dispose");
		        }
		        
		        ArrayList list = new ArrayList();
		        for (String s : collections) {
					HashMap rec = new HashMap();
					rec.put("name", s);
					list.add(rec);
				}
				
				jsonObject.put("success", true);
				jsonObject.put("data", list);
			} catch (Exception e) {
				e.printStackTrace();
				String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.UnableToConnect", "host name(s)"); 
				jsonObject.put("message", message);
			}
		} else {
			
			String missingConnDetails = "";
			if (Const.isEmpty(hostname)) {
				missingConnDetails += "host name(s)";
			}
			if (Const.isEmpty(dB)) {
				missingConnDetails += " database";
			}
			
			 String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.MissingConnectionDetails", missingConnDetails); 
			 jsonObject.put("message", message);
		}
		
		
		JsonUtils.response(jsonObject);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/setupCustomWriteConcernNames")
	public void setupCustomWriteConcernNames(@RequestParam String graphXml, @RequestParam String stepName,
			@CurrentUser CurrentUserResponse user) throws Exception {
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class PKG = stepMetaInterface.getClass();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);
		
		String hostname = transMeta.environmentSubstitute(ReflectUtils.getString(stepMetaInterface, "hostnames"));

	    if ( !Const.isEmpty( hostname ) ) {
	      try {
	    	List<String> custom = new ArrayList<String>();

			Class clazz = Class.forName("org.pentaho.mongo.wrapper.MongoWrapperUtil", true,
					stepMetaInterface.getClass().getClassLoader());
			Object wrapper = ReflectUtils.callStatic(clazz, "createMongoClientWrapper", stepMetaInterface,
					transMeta, null);
			try {
				custom = (List<String>) ReflectUtils.get(wrapper, "lastErrorModes");
			} finally {
				ReflectUtils.call(wrapper, "dispose");
			}
			
			ArrayList list = new ArrayList();
			if ( custom.size() > 0 ) {

				for (String s : custom) {
					HashMap rec = new HashMap();
					rec.put("name", s);
					list.add(rec);
				}
	        }
			
			
			jsonObject.put("success", true);
			jsonObject.put("data", list);
	        
	      } catch ( Exception e ) {
	    	e.printStackTrace();
			String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.UnableToConnect", "host name(s)"); 
			jsonObject.put("message", message);
	      }
	    } else {
			 String message = BaseMessages.getString( PKG, "MongoDbOutputDialog.ErrorMessage.MissingConnectionDetails", "host name(s)"); 
			 jsonObject.put("message", message);
		}
	    
	    JsonUtils.response(jsonObject);
	    
	}

}
