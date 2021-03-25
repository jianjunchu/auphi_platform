package org.firzjb.kettle.trans.steps;

import java.util.List;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.stringoperations.StringOperationsMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("StringOperations")
@Scope("prototype")
public class StringOperations extends AbstractStep {
	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases,
			IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		StringOperationsMeta stringOperationsMeta = (StringOperationsMeta) stepMetaInterface;
		JSONArray jsonArray = JSONArray.fromObject(cell.getAttribute("fields"));

		String[] fieldInStream = new String[jsonArray.size()];
		String[] fieldOutStream = new String[jsonArray.size()];
		int[] trimType = new int[jsonArray.size()];
		int[] lowerUpper = new int[jsonArray.size()];
		int[] padding_type = new int[jsonArray.size()];
		String[] padChar = new String[jsonArray.size()];
		String[] padLen = new String[jsonArray.size()];
		int[] initCap = new int[jsonArray.size()];
		int[] maskXML = new int[jsonArray.size()];
		int[] digits = new int[jsonArray.size()];
		int[] removeSpecialCharacters = new int[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			fieldInStream[i] = jsonObject.optString("in_stream_name");
			fieldOutStream[i] = jsonObject.optString("out_stream_name");
			trimType[i] = ValueMeta.getTrimTypeByCode(jsonObject.optString("trim_type"));
			lowerUpper[i] = StringOperationsMeta.getLowerUpperByDesc(jsonObject.optString("lower_upper"));
			padding_type[i] = StringOperationsMeta.getPaddingByDesc(jsonObject.optString("padding_type"));
			padChar[i] = jsonObject.optString("pad_char");
			padLen[i] = jsonObject.optString("pad_len");
			initCap[i] = StringOperationsMeta.getInitCapByDesc(jsonObject.optString("init_cap"));
			maskXML[i] = StringOperationsMeta.getMaskXMLByDesc(jsonObject.optString("mask_xml"));
			digits[i] = StringOperationsMeta.getDigitsByDesc(jsonObject.optString("digits"));
			removeSpecialCharacters[i] = StringOperationsMeta.getRemoveSpecialCharactersByDesc(jsonObject.optString("remove_special_characters"));
		}
		stringOperationsMeta.setFieldInStream(fieldInStream);
		stringOperationsMeta.setFieldOutStream(fieldOutStream);
		stringOperationsMeta.setTrimType(trimType);
		stringOperationsMeta.setLowerUpper(lowerUpper);
		stringOperationsMeta.setPaddingType(padding_type);
		stringOperationsMeta.setPadChar(padChar);
		stringOperationsMeta.setPadLen(padLen);
		stringOperationsMeta.setInitCap(initCap);
		stringOperationsMeta.setMaskXML(maskXML);
		stringOperationsMeta.setDigits(digits);
		stringOperationsMeta.setRemoveSpecialCharacters(removeSpecialCharacters);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		StringOperationsMeta stringOperationsMeta = (StringOperationsMeta) stepMetaInterface;

		JSONArray jsonArray = new JSONArray();

		String[] fieldInStream = stringOperationsMeta.getFieldInStream();
		String[] fieldOutStream = stringOperationsMeta.getFieldOutStream();
		int[] trimType = stringOperationsMeta.getTrimType();
		int[] lowerUpper = stringOperationsMeta.getLowerUpper();
		int[] padding_type = stringOperationsMeta.getPaddingType();
		String[] padChar = stringOperationsMeta.getPadChar();
		String[] padLen = stringOperationsMeta.getPadLen();
		int[] initCap = stringOperationsMeta.getInitCap();
		int[] maskXML = stringOperationsMeta.getMaskXML();
		int[] digits = stringOperationsMeta.getDigits();
		int[] removeSpecialCharacters = stringOperationsMeta.getRemoveSpecialCharacters();

		for (int i = 0; i < fieldInStream.length; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("in_stream_name", fieldInStream[i]);
			jsonObject.put("out_stream_name", fieldOutStream[i]);
			jsonObject.put("trim_type", ValueMeta.getTrimTypeCode(trimType[i]));
			jsonObject.put("lower_upper", StringOperationsMeta.lowerUpperCode[lowerUpper[i]]);
			jsonObject.put("padding_type", StringOperationsMeta.paddingCode[padding_type[i]]);
			jsonObject.put("pad_char", padChar[i]);
			jsonObject.put("pad_len", padLen[i]);
			jsonObject.put("init_cap", StringOperationsMeta.initCapCode[initCap[i]]);
			jsonObject.put("mask_xml", StringOperationsMeta.maskXMLCode[maskXML[i]]);
			jsonObject.put("digits", StringOperationsMeta.digitsCode[digits[i]]);
			jsonObject.put("remove_special_characters", StringOperationsMeta.removeSpecialCharactersCode[removeSpecialCharacters[i]]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("fields", jsonArray.toString());
		return e;
	}
}
