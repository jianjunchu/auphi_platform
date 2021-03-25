package org.firzjb.kettle.trans.steps;

import java.util.List;

import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.textfileinput.TextFileInputField;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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

@Component("WORDINPUT")
@Scope("prototype")
public class WORDINPUT extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		ReflectUtils.set(stepMetaInterface, "filename", cell.getAttribute("filename"));
		ReflectUtils.set(stepMetaInterface, "filenameField", cell.getAttribute("filenameField"));
		ReflectUtils.set(stepMetaInterface, "rowNumField", cell.getAttribute("rowNumField"));
		ReflectUtils.set(stepMetaInterface, "includingFilename", "Y".equalsIgnoreCase(cell.getAttribute("filename")));
		ReflectUtils.set(stepMetaInterface, "startRowIndex", cell.getAttribute("startRowIndex"));
		ReflectUtils.set(stepMetaInterface, "tableNr", cell.getAttribute("tableNr"));
		ReflectUtils.set(stepMetaInterface, "headerPresent", "Y".equalsIgnoreCase(cell.getAttribute("headerPresent")));
		ReflectUtils.set(stepMetaInterface, "addResultFile", "Y".equalsIgnoreCase(cell.getAttribute("addResultFile")));

		JSONArray jsonArray = JSONArray.fromObject(cell.getAttribute("inputFields"));
		ReflectUtils.call(stepMetaInterface, "allocate", jsonArray.size());

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			TextFileInputField field = new TextFileInputField();

			field.setName(jsonObject.optString("name"));
			field.setType( ValueMetaFactory.getIdForValueMeta(jsonObject.optString("type")) );
			field.setFormat(jsonObject.optString("format"));
			field.setPosition(Const.toInt(jsonObject.optString("position"), 0));
			field.setLength(Const.toInt(jsonObject.optString("length"), -1));
			field.setPrecision(Const.toInt(jsonObject.optString("precision"), -1));
			field.setCurrencySymbol(jsonObject.optString("currency"));
			field.setDecimalSymbol(jsonObject.optString("decimal"));
			field.setGroupSymbol(jsonObject.optString("group"));
			field.setTrimType(Const.toInt(jsonObject.optString("trim_type"), 0));
			field.setNullString(jsonObject.optString("nullif"));
			field.setIfNullValue(jsonObject.optString("ifnull"));
			field.setRepeated("Y".equalsIgnoreCase(jsonObject.optString("repeat")));

			ReflectUtils.getArray(stepMetaInterface, "inputFields")[i] = field;
		}

	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		e.setAttribute("filename", ReflectUtils.getString(stepMetaInterface, "filename"));
		e.setAttribute("filenameField", ReflectUtils.getString(stepMetaInterface, "filenameField"));
		e.setAttribute("rowNumField", ReflectUtils.getString(stepMetaInterface, "rowNumField"));
		e.setAttribute("includingFilename", ReflectUtils.getBoolean(stepMetaInterface, "includingFilename") ? "Y" : "N");
		e.setAttribute("startRowIndex", ReflectUtils.getString(stepMetaInterface, "startRowIndex"));
		e.setAttribute("tableNr", ReflectUtils.getString(stepMetaInterface, "tableNr"));
		e.setAttribute("headerPresent", ReflectUtils.getBoolean(stepMetaInterface, "headerPresent") ? "Y" : "N");
		e.setAttribute("addResultFile", ReflectUtils.getBoolean(stepMetaInterface, "addResultFile") ? "Y" : "N");

		TextFileInputField[] inputFields = (TextFileInputField[]) ReflectUtils.get(stepMetaInterface, "inputFields");
		JSONArray jsonArray = new JSONArray();
		if(inputFields != null) {
			for(TextFileInputField inputField : inputFields) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", inputField.getName());
				jsonObject.put("position", inputField.getPosition());
				jsonObject.put("type", inputField.getTypeDesc());
				jsonObject.put("format", inputField.getFormat());
				jsonObject.put("currencySymbol", inputField.getCurrencySymbol());
				jsonObject.put("decimalSymbol", inputField.getDecimalSymbol());
				jsonObject.put("groupSymbol", inputField.getGroupSymbol());
				jsonObject.put("nullString", inputField.getNullString());
				jsonObject.put("ifNullValue", inputField.getIfNullValue());
				jsonObject.put("trimtype", inputField.getTrimTypeDesc());
				jsonObject.put("repeat", inputField.isRepeated() ? "Y" : "N");
				if (inputField.getLength() != -1)
					jsonObject.put("length", inputField.getLength());
				if (inputField.getPrecision() != -1)
					jsonObject.put("precision", inputField.getPrecision());
				jsonArray.add(jsonObject);
			}
		}

		e.setAttribute("inputFields", jsonArray.toString());

		return e;
	}

}
