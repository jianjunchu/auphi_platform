package com.aofei.kettle.trans.steps;

import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.core.PropsUI;
import com.aofei.kettle.trans.step.AbstractStep;
import com.aofei.kettle.utils.JSONArray;
import com.aofei.kettle.utils.JSONObject;
import com.aofei.kettle.utils.ReflectUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("JsonOutput")
@Scope("prototype")
public class JsonOutput extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		ReflectUtils.setFieldValue(stepMetaInterface, "outputValue", cell.getAttribute("outputValue"));
		ReflectUtils.setFieldValue(stepMetaInterface, "jsonBloc", cell.getAttribute("jsonBloc"));
		ReflectUtils.setFieldValue(stepMetaInterface, "nrRowsInBloc", cell.getAttribute("nrRowsInBloc"));
		ReflectUtils.setFieldValue(stepMetaInterface, "operationType", Const.toInt(cell.getAttribute("operation_type"), 0));
		ReflectUtils.setFieldValue(stepMetaInterface, "compatibilityMode", "Y".equalsIgnoreCase(cell.getAttribute("compatibility_mode")));
		ReflectUtils.setFieldValue(stepMetaInterface, "encoding", cell.getAttribute("encoding"));
		ReflectUtils.setFieldValue(stepMetaInterface, "AddToResult", "Y".equalsIgnoreCase(cell.getAttribute("addtoresult")));
		
		ReflectUtils.setFieldValue(stepMetaInterface, "fileName", cell.getAttribute("name"));
		ReflectUtils.setFieldValue(stepMetaInterface, "extension", cell.getAttribute("extention"));
		ReflectUtils.setFieldValue(stepMetaInterface, "fileAppended", "Y".equalsIgnoreCase(cell.getAttribute("append")));
		ReflectUtils.setFieldValue(stepMetaInterface, "stepNrInFilename", "Y".equalsIgnoreCase(cell.getAttribute("split")));
		ReflectUtils.setFieldValue(stepMetaInterface, "partNrInFilename", "Y".equalsIgnoreCase(cell.getAttribute("haspartno")));
		ReflectUtils.setFieldValue(stepMetaInterface, "dateInFilename", "Y".equalsIgnoreCase(cell.getAttribute("add_date")));
		ReflectUtils.setFieldValue(stepMetaInterface, "timeInFilename", "Y".equalsIgnoreCase(cell.getAttribute("add_time")));
		ReflectUtils.setFieldValue(stepMetaInterface, "createparentfolder", "Y".equalsIgnoreCase(cell.getAttribute("create_parent_folder")));
		ReflectUtils.setFieldValue(stepMetaInterface, "DoNotOpenNewFileInit", "Y".equalsIgnoreCase(cell.getAttribute("DoNotOpenNewFileInit")));
		ReflectUtils.setFieldValue(stepMetaInterface, "servletOutput", "Y".equalsIgnoreCase(cell.getAttribute("servlet_output")));
		
		ClassLoader classLoader = stepMetaInterface.getClass().getClassLoader();
		Class<?> clazz = classLoader.loadClass("org.pentaho.di.trans.steps.jsonoutput.JsonOutputField");
		String fields = cell.getAttribute("fields");
		JSONArray fieldsArray = StringUtils.hasText(fields) ? JSONArray.fromObject(fields) : new JSONArray();
		ReflectUtils.call(stepMetaInterface, "allocate", fieldsArray.size());
		
		for(int i=0; i<fieldsArray.size(); i++) {
			JSONObject jsonObject = fieldsArray.getJSONObject(i);
			
			Object jsonOutputField = clazz.newInstance();
			ReflectUtils.set(jsonOutputField, "fieldName", jsonObject.optString( "name" ) );
			ReflectUtils.set(jsonOutputField, "elementName", jsonObject.optString( "element" ) );
			
			ReflectUtils.getArray(stepMetaInterface, "outputFields")[i] = jsonOutputField;
			
		}
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		
		e.setAttribute("outputValue", ReflectUtils.getFieldString(stepMetaInterface, "outputValue"));
		e.setAttribute("jsonBloc", ReflectUtils.getFieldString(stepMetaInterface, "jsonBloc"));
		e.setAttribute("nrRowsInBloc", ReflectUtils.getFieldString(stepMetaInterface, "nrRowsInBloc"));
		e.setAttribute("operation_type", String.valueOf(ReflectUtils.getFieldValue(stepMetaInterface, "operationType")));
		e.setAttribute("compatibility_mode", ReflectUtils.getFieldBoolean(stepMetaInterface, "compatibilityMode") ? "Y" : "N");
		e.setAttribute("encoding", ReflectUtils.getFieldString(stepMetaInterface, "encoding"));
		e.setAttribute("addtoresult", ReflectUtils.getFieldBoolean(stepMetaInterface, "AddToResult") ? "Y" : "N");
		
		// file
		e.setAttribute("name", ReflectUtils.getString(stepMetaInterface, "fileName"));
		e.setAttribute("extention", ReflectUtils.getString(stepMetaInterface, "extension"));
		e.setAttribute("append", ReflectUtils.getFieldBoolean(stepMetaInterface, "fileAppended") ? "Y" : "N");
		e.setAttribute("split", ReflectUtils.getBoolean(stepMetaInterface, "stepNrInFilename") ? "Y" : "N");
		e.setAttribute("haspartno", ReflectUtils.getBoolean(stepMetaInterface, "partNrInFilename") ? "Y" : "N");
		e.setAttribute("add_date", ReflectUtils.getBoolean(stepMetaInterface, "dateInFilename") ? "Y" : "N");
		e.setAttribute("add_time", ReflectUtils.getBoolean(stepMetaInterface, "timeInFilename") ? "Y" : "N");
		e.setAttribute("create_parent_folder", ReflectUtils.getFieldBoolean(stepMetaInterface, "createparentfolder") ? "Y" : "N");
		e.setAttribute("DoNotOpenNewFileInit", ReflectUtils.getFieldBoolean(stepMetaInterface, "DoNotOpenNewFileInit") ? "Y" : "N");
		e.setAttribute("servlet_output", ReflectUtils.getBoolean(stepMetaInterface, "servletOutput") ? "Y" : "N");
		
		Object[] outputFields = (Object[]) ReflectUtils.get(stepMetaInterface, "outputFields");
		JSONArray jsonArray = new JSONArray();
	    if(outputFields != null) {
	    	for(Object outputField: outputFields) {
	    		JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", ReflectUtils.get(outputField, "fieldName"));
				jsonObject.put("element", ReflectUtils.get(outputField, "elementName"));
				jsonArray.add(jsonObject);
	    	}
	    }
	    e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}
