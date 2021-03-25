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
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.selectvalues.SelectMetadataChange;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta.SelectField;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("SelectValues")
@Scope("prototype")
public class SelectValues extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		SelectValuesMeta selectValuesMeta = (SelectValuesMeta) stepMetaInterface;

		JSONArray fields = JSONArray.fromObject(cell.getAttribute("fields"));
		JSONArray remove = JSONArray.fromObject(cell.getAttribute("remove"));
		JSONArray meta = JSONArray.fromObject(cell.getAttribute("meta"));

		selectValuesMeta.allocate(fields.size(), remove.size(), meta.size());


		for(int i=0; i<fields.size(); i++) {
			SelectField selectField = new SelectField();
			JSONObject jsonObject = fields.getJSONObject(i);

			selectField.setName(jsonObject.optString( "name" ));
			selectField.setRename(jsonObject.optString( "rename" ));
			selectField.setLength(Const.toInt( jsonObject.optString( "length" ), -2 ));
			selectField.setPrecision(Const.toInt( jsonObject.optString( "precision" ), -2 ));

			try {
				ReflectUtils.setFieldValue(selectField, "functionExpression", jsonObject.optString( "func" ));
				ReflectUtils.set(selectField, "padChar", jsonObject.optString( "padchar" ));
			} catch(Exception ex) {
				System.out.println("字段选择组件缺少自定义的扩展字段2");
			}
			selectValuesMeta.getSelectFields()[i] = selectField;
		}

		selectValuesMeta.setSelectingAndSortingUnspecifiedFields( "Y".equalsIgnoreCase( cell.getAttribute( "select_unspecified" )) );

		for(int i=0; i<remove.size(); i++) {
			JSONObject jsonObject = remove.getJSONObject(i);
			selectValuesMeta.getDeleteName()[i] = jsonObject.optString("name");
		}


//		SelectMetadataChange[] metadata = new SelectMetadataChange[jsonArray.size()];
		for(int i=0; i<meta.size(); i++) {
			JSONObject jsonObject = meta.getJSONObject(i);
			SelectMetadataChange selectMetadataChange = new SelectMetadataChange(selectValuesMeta);

			selectMetadataChange.setName( jsonObject.optString("name") );
			selectMetadataChange.setRename( jsonObject.optString("rename") );
			selectMetadataChange.setType(ValueMeta.getType( jsonObject.optString("type") ));
			selectMetadataChange.setLength(Const.toInt( jsonObject.optString("length"), -2 ));
			selectMetadataChange.setPrecision(Const.toInt( jsonObject.optString("precision"), -2 ));
			if("Y".equalsIgnoreCase(jsonObject.optString("storage_type")))
				selectMetadataChange.setStorageType(ValueMetaInterface.STORAGE_TYPE_NORMAL);
			selectMetadataChange.setConversionMask( jsonObject.optString("conversion_mask") );

			selectMetadataChange.setDateFormatLenient( "Y".equalsIgnoreCase(jsonObject.optString("date_format_lenient")) );
			selectMetadataChange.setDateFormatLocale(jsonObject.optString("date_format_locale"));
			selectMetadataChange.setDateFormatTimeZone(jsonObject.optString("date_format_timezone"));
			selectMetadataChange.setEncoding(jsonObject.optString("encoding"));

			selectMetadataChange.setLenientStringToNumber( "Y".equalsIgnoreCase(jsonObject.optString("lenient_string_to_number")) );
			selectMetadataChange.setDecimalSymbol(jsonObject.optString("decimal_symbol"));
			selectMetadataChange.setGroupingSymbol(jsonObject.optString("grouping_symbol"));
			selectMetadataChange.setCurrencySymbol(jsonObject.optString("currency_symbol"));

			selectValuesMeta.getMeta()[i] = selectMetadataChange;
		}

	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		SelectValuesMeta selectValuesMeta = (SelectValuesMeta) stepMetaInterface;
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		JSONArray jsonArray = new JSONArray();

		SelectField[] selectFields = selectValuesMeta.getSelectFields();
		if(selectFields != null) {
			for (SelectField selectField : selectFields) {
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("name", selectField.getName());
				jsonObject.put("rename", selectField.getRename());
				jsonObject.put("length", selectField.getLength());
				jsonObject.put("precision", selectField.getPrecision());

				try {
					jsonObject.put("func", ReflectUtils.getFieldString(selectField, "functionExpression"));
					jsonObject.put("padchar", ReflectUtils.getFieldString(selectField, "padChar"));
				} catch(Exception ex) {
					System.out.println("字段选择组件缺少自定义的扩展字段");
				}

				jsonArray.add(jsonObject);
			}
		}
	    e.setAttribute("fields", jsonArray.toString());

	    e.setAttribute("select_unspecified", selectValuesMeta.isSelectingAndSortingUnspecifiedFields() ? "Y" : "N");

	    jsonArray = new JSONArray();
		if(selectValuesMeta.getDeleteName() != null) {
			for ( int i = 0; i < selectValuesMeta.getDeleteName().length; i++ ) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", selectValuesMeta.getDeleteName()[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("remove", jsonArray.toString());

		jsonArray = new JSONArray();
		if(selectValuesMeta.getMeta() != null) {
			for ( int i = 0; i < selectValuesMeta.getMeta().length; i++ ) {
				JSONObject jsonObject = new JSONObject();
				SelectMetadataChange selectMetadataChange = selectValuesMeta.getMeta()[i];

				jsonObject.put("name", selectMetadataChange.getName());
				jsonObject.put("rename", selectMetadataChange.getRename());
				jsonObject.put("type", ValueMeta.getTypeDesc( selectMetadataChange.getType()));
				jsonObject.put("length", selectMetadataChange.getLength());
				jsonObject.put("precision", selectMetadataChange.getPrecision());
				jsonObject.put("storage_type", selectMetadataChange.getStorageType() == ValueMetaInterface.STORAGE_TYPE_NORMAL ? "Y" : "N");
				jsonObject.put("conversion_mask", selectMetadataChange.getConversionMask());

				jsonObject.put("date_format_lenient", selectMetadataChange.isDateFormatLenient() ? "Y" : "N");
				jsonObject.put("date_format_locale", selectMetadataChange.getDateFormatLocale());
				jsonObject.put("date_format_timezone", selectMetadataChange.getDateFormatTimeZone());

				jsonObject.put("lenient_string_to_number", selectMetadataChange.isLenientStringToNumber() ? "Y" : "N");

				jsonObject.put("encoding", selectMetadataChange.getEncoding());
				jsonObject.put("decimal_symbol", selectMetadataChange.getDecimalSymbol());
				jsonObject.put("grouping_symbol", selectMetadataChange.getGroupingSymbol());
				jsonObject.put("currency_symbol", selectMetadataChange.getCurrencySymbol());



				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("meta", jsonArray.toString());

		return e;
	}

}
