package org.firzjb.kettle.trans.steps;

import java.util.List;

import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.value.ValueMetaBase;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.textfileinput.TextFileInputField;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import org.firzjb.kettle.utils.ReflectUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("OSSInput")
@Scope("prototype")
public class OSSInput extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		ReflectUtils.setFieldValue(stepMetaInterface, "endpoint", cell.getAttribute("endpoint"));
		ReflectUtils.setFieldValue(stepMetaInterface, "bucket", cell.getAttribute("bucket"));
		ReflectUtils.setFieldValue(stepMetaInterface, "accessKeyId", cell.getAttribute("accessKeyId"));
		ReflectUtils.setFieldValue(stepMetaInterface, "accessKeySecret", cell.getAttribute("accessKeySecret"));

		ReflectUtils.setFieldValue(stepMetaInterface, "prefix", cell.getAttribute("prefix"));
		ReflectUtils.setFieldValue(stepMetaInterface, "delimiter", cell.getAttribute("delimiter"));
		ReflectUtils.setFieldValue(stepMetaInterface, "enclosure", cell.getAttribute("enclosure"));
		ReflectUtils.setFieldValue(stepMetaInterface, "bufferSize", cell.getAttribute("bufferSize"));

		ReflectUtils.setFieldValue(stepMetaInterface, "lazyConversion", "Y".equalsIgnoreCase(cell.getAttribute("lazyConversion")));
		ReflectUtils.setFieldValue(stepMetaInterface, "header", "Y".equalsIgnoreCase(cell.getAttribute("header")));
		ReflectUtils.setFieldValue(stepMetaInterface, "includingFilename", "Y".equalsIgnoreCase(cell.getAttribute("includingFilename")));
		ReflectUtils.setFieldValue(stepMetaInterface, "rowNumField", cell.getAttribute("rowNumField"));
		ReflectUtils.setFieldValue(stepMetaInterface, "runningInParallel", "Y".equalsIgnoreCase(cell.getAttribute("runningInParallel")));
		ReflectUtils.setFieldValue(stepMetaInterface, "encoding", cell.getAttribute("encoding"));

		String fields = cell.getAttribute("inputFields");
		JSONArray fieldsArray = StringUtils.hasText(fields) ? JSONArray.fromObject(fields) : new JSONArray();
		ReflectUtils.call(stepMetaInterface, "allocate", fieldsArray.size());

		for(int i=0; i<fieldsArray.size(); i++) {
			JSONObject jsonObject = fieldsArray.getJSONObject(i);

			TextFileInputField inputField = new TextFileInputField();
			inputField.setName(jsonObject.optString( "name" ));
			inputField.setType(ValueMeta.getType(jsonObject.optString( "type" )));
			inputField.setFormat(jsonObject.optString( "format" ));
			inputField.setCurrencySymbol(jsonObject.optString("currency"));
			inputField.setDecimalSymbol(jsonObject.optString("decimal"));
			inputField.setGroupSymbol(jsonObject.optString("group"));
			inputField.setLength(jsonObject.optInt("length", -1));
			inputField.setPrecision(jsonObject.optInt("precision", -1));
			inputField.setTrimType(ValueMetaBase.getTrimTypeByCode(jsonObject.optString( "trim_type" )));

			ReflectUtils.getArray(stepMetaInterface, "inputFields")[i] = inputField;

		}

	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		e.setAttribute("endpoint", ReflectUtils.getFieldString(stepMetaInterface, "endpoint"));
		e.setAttribute("bucket", ReflectUtils.getFieldString(stepMetaInterface, "bucket"));
		e.setAttribute("accessKeyId", ReflectUtils.getFieldString(stepMetaInterface, "accessKeyId"));
		e.setAttribute("accessKeySecret", ReflectUtils.getFieldString(stepMetaInterface, "accessKeySecret"));

		e.setAttribute("prefix", ReflectUtils.getFieldString(stepMetaInterface, "prefix"));
		e.setAttribute("delimiter", ReflectUtils.getFieldString(stepMetaInterface, "delimiter"));
		e.setAttribute("enclosure", ReflectUtils.getFieldString(stepMetaInterface, "enclosure"));
		e.setAttribute("bufferSize", ReflectUtils.getFieldString(stepMetaInterface, "bufferSize"));

		e.setAttribute("lazyConversion", ReflectUtils.getFieldBoolean(stepMetaInterface, "lazyConversion") ? "Y" : "N");
		e.setAttribute("header", ReflectUtils.getFieldBoolean(stepMetaInterface, "header") ? "Y" : "N");
		e.setAttribute("includingFilename", ReflectUtils.getFieldBoolean(stepMetaInterface, "includingFilename") ? "Y" : "N");
		e.setAttribute("rowNumField", ReflectUtils.getFieldString(stepMetaInterface, "rowNumField"));
		e.setAttribute("runningInParallel", ReflectUtils.getFieldBoolean(stepMetaInterface, "runningInParallel") ? "Y" : "N");
		e.setAttribute("encoding", ReflectUtils.getFieldString(stepMetaInterface, "encoding"));

		Object[] inputFields = (Object[]) ReflectUtils.get(stepMetaInterface, "inputFields");
		JSONArray jsonArray = new JSONArray();
		if(inputFields != null) {
	    	for(Object inputField: inputFields) {
	    		JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", ReflectUtils.get(inputField, "name"));
				jsonObject.put("type", ReflectUtils.get(inputField, "typeDesc"));
				jsonObject.put("format", ReflectUtils.get(inputField, "format"));
				jsonObject.put("currency", ReflectUtils.get(inputField, "currencySymbol"));

				jsonObject.put("decimal", ReflectUtils.get(inputField, "decimalSymbol"));
				jsonObject.put("group", ReflectUtils.get(inputField, "groupSymbol"));
				jsonObject.put("length", ReflectUtils.get(inputField, "length"));
				jsonObject.put("precision", ReflectUtils.get(inputField, "precision"));
				jsonObject.put("trim_type", ReflectUtils.getString(inputField, "trimTypeCode"));
				jsonArray.add(jsonObject);
	    	}
	    }
	    e.setAttribute("inputFields", jsonArray.toString());

		return e;
	}

}
