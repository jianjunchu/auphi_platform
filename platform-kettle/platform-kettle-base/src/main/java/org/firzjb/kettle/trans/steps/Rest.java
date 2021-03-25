package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import org.firzjb.kettle.utils.StringEscapeHelper;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.rest.RestMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("Rest")
@Scope("prototype")
public class Rest extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		RestMeta restMeta = (RestMeta) stepMetaInterface;
		restMeta.setUrl(cell.getAttribute("url"));
		restMeta.setUrlInField("Y".equalsIgnoreCase(cell.getAttribute("urlInField")));
		restMeta.setUrlField(cell.getAttribute("urlField"));
		restMeta.setMethod(cell.getAttribute("method"));
		restMeta.setDynamicMethod("Y".equalsIgnoreCase(cell.getAttribute("dynamicMethod")));
		restMeta.setMethodFieldName(cell.getAttribute("methodFieldName"));
		restMeta.setBodyField(cell.getAttribute("bodyField"));
		restMeta.setApplicationType(cell.getAttribute("applicationType"));

		restMeta.setFieldName(cell.getAttribute("resultName"));
		restMeta.setResultCodeFieldName(cell.getAttribute("resultCodeFieldName"));
		restMeta.setResponseTimeFieldName(cell.getAttribute("resultResponseTimeField"));
		restMeta.setResponseHeaderFieldName(cell.getAttribute("resultResponseHeaderField"));

		restMeta.setHttpLogin(cell.getAttribute("httpLogin"));
		restMeta.setHttpPassword(cell.getAttribute("httpPassword"));
		restMeta.setPreemptive("Y".equalsIgnoreCase(cell.getAttribute("preemptive")));

		restMeta.setProxyHost(cell.getAttribute("proxyHost"));
		restMeta.setProxyPort(cell.getAttribute("proxyPort"));

		restMeta.setTrustStoreFile(cell.getAttribute("trustStoreFile"));
		restMeta.setTrustStorePassword(cell.getAttribute("trustStorePassword"));


		JSONArray jsonArray = JSONArray.fromObject(cell.getAttribute("headers"));
		String[] headerField = new String[jsonArray.size()];
		String[] headerName = new String[jsonArray.size()];
		for( int i=0; i<jsonArray.size(); i++ ) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			headerField[i] = jsonObject.optString("field");
			headerName[i] = jsonObject.optString("name");
		}
		restMeta.setHeaderField(headerField);
		restMeta.setHeaderName(headerName);

		jsonArray = JSONArray.fromObject(cell.getAttribute("parameters"));
		String[] parameterField = new String[jsonArray.size()];
		String[] parameterName = new String[jsonArray.size()];
		for( int i=0; i<jsonArray.size(); i++ ) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			parameterField[i] = jsonObject.optString("field");
			parameterName[i] = jsonObject.optString("name");
		}
		restMeta.setParameterField(parameterField);
		restMeta.setParameterName(parameterName);

		jsonArray = JSONArray.fromObject(cell.getAttribute("matrixParameters"));
		String[] matrixParameterField = new String[jsonArray.size()];
		String[] matrixParameterName = new String[jsonArray.size()];
		for( int i=0; i<jsonArray.size(); i++ ) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			matrixParameterField[i] = jsonObject.optString("field");
			matrixParameterName[i] = jsonObject.optString("name");
		}
		restMeta.setMatrixParameterField(matrixParameterField);
		restMeta.setMatrixParameterName(matrixParameterName);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		RestMeta restMeta = (RestMeta) stepMetaInterface;


		e.setAttribute("url", restMeta.getUrl());
		e.setAttribute("urlInField", restMeta.isUrlInField() ? "Y" : "N");
		e.setAttribute("urlField", restMeta.getUrlField());
		e.setAttribute("method", restMeta.getMethod());
		e.setAttribute("dynamicMethod", restMeta.isDynamicMethod() ? "Y" : "N");
		e.setAttribute("methodFieldName", restMeta.getMethodFieldName());
		e.setAttribute("bodyField", restMeta.getBodyField());
		e.setAttribute("applicationType", restMeta.getApplicationType());

		e.setAttribute("resultName", restMeta.getFieldName());
		e.setAttribute("resultCodeFieldName", restMeta.getResultCodeFieldName());
		e.setAttribute("resultResponseTimeField", restMeta.getResponseTimeFieldName());
		e.setAttribute("resultResponseHeaderField", restMeta.getResponseHeaderFieldName());

		e.setAttribute("httpLogin", restMeta.getHttpLogin());
		e.setAttribute("httpPassword", restMeta.getHttpPassword());
		e.setAttribute("preemptive", restMeta.isPreemptive() ? "Y" : "N");

		e.setAttribute("proxyHost", restMeta.getProxyHost());
		e.setAttribute("proxyPort", restMeta.getProxyPort());

		e.setAttribute("trustStoreFile", StringEscapeHelper.encode(restMeta.getTrustStoreFile()));
		e.setAttribute("trustStorePassword", restMeta.getTrustStorePassword());

		String[] headerField = restMeta.getHeaderField();
		String[] headerName = restMeta.getHeaderName();
		JSONArray jsonArray = new JSONArray();
		if(headerField != null) {
			for (int i = 0; i < headerField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("field", headerField[i]);
				jsonObject.put("name", headerName[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("headers", jsonArray.toString());

		String[] parameterField = restMeta.getParameterField();
		String[] parameterName = restMeta.getParameterName();
		jsonArray = new JSONArray();
		if(parameterField != null) {
			for (int i = 0; i < parameterField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("field", parameterField[i]);
				jsonObject.put("name", parameterName[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("parameters", jsonArray.toString());

		String[] matrixParameterField = restMeta.getMatrixParameterField();
		String[] matrixParameterName = restMeta.getMatrixParameterName();
		jsonArray = new JSONArray();
		if(matrixParameterField != null) {
			for (int i = 0; i < matrixParameterField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("field", matrixParameterField[i]);
				jsonObject.put("name", matrixParameterName[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("matrixParameters", jsonArray.toString());

		return e;
	}

}
