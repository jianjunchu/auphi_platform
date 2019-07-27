package com.aofei.kettle.trans.steps;

import java.util.List;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.httppost.HTTPPOSTMeta;
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
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("HTTPPOST")
@Scope("prototype")
public class HTTPPOST extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		HTTPPOSTMeta httppostMeta = (HTTPPOSTMeta) stepMetaInterface;
		
		httppostMeta.setPostAFile("Y".equalsIgnoreCase(cell.getAttribute("postafile")));
		httppostMeta.setEncoding(cell.getAttribute("encoding"));
		httppostMeta.setUrl(cell.getAttribute("url"));
		httppostMeta.setUrlInField("Y".equalsIgnoreCase(cell.getAttribute("urlInField")));
		httppostMeta.setUrlField(cell.getAttribute("urlField"));
		httppostMeta.setRequestEntity(cell.getAttribute("requestEntity"));
		httppostMeta.setHttpLogin(cell.getAttribute("httpLogin"));
		httppostMeta.setHttpPassword(cell.getAttribute("httpPassword"));
		httppostMeta.setProxyHost(cell.getAttribute("proxyHost"));
		httppostMeta.setProxyPort(cell.getAttribute("proxyPort"));
		httppostMeta.setSocketTimeout(cell.getAttribute("socketTimeout"));
		httppostMeta.setConnectionTimeout(cell.getAttribute("connectionTimeout"));
		httppostMeta.setCloseIdleConnectionsTime(cell.getAttribute("closeIdleConnectionsTime"));
		
		httppostMeta.setFieldName(cell.getAttribute("name"));
		httppostMeta.setResultCodeFieldName(cell.getAttribute("code"));
		httppostMeta.setResponseTimeFieldName(cell.getAttribute("response_time"));
		httppostMeta.setResponseHeaderFieldName(cell.getAttribute("response_header"));
		
		String arg = cell.getAttribute("arg");
		if(StringUtils.hasText(arg)) {
			JSONArray jsonArray = JSONArray.fromObject(arg);
			httppostMeta.allocate(jsonArray.size());
			
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				httppostMeta.getArgumentField()[i] = jsonObject.optString("name");
				httppostMeta.getArgumentHeader()[i] = "Y".equalsIgnoreCase(jsonObject.optString("header"));
				httppostMeta.getArgumentParameter()[i] = jsonObject.optString("parameter");
			}
		}
		
		String query = cell.getAttribute("query");
		if(StringUtils.hasText(query)) {
			JSONArray jsonArray = JSONArray.fromObject(query);
			httppostMeta.allocateQuery(jsonArray.size());
			
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				httppostMeta.getQueryField()[i] = jsonObject.optString("name");
				httppostMeta.getQueryParameter()[i] = jsonObject.optString("parameter");
			}
		}
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		HTTPPOSTMeta httppostMeta = (HTTPPOSTMeta) stepMetaInterface;
		
		e.setAttribute("postafile", httppostMeta.isPostAFile() ? "Y" : "N");
		e.setAttribute("encoding", httppostMeta.getEncoding());
		e.setAttribute("url", httppostMeta.getUrl());
		e.setAttribute("urlInField", httppostMeta.isUrlInField() ? "Y" : "N");
		e.setAttribute("urlField", httppostMeta.getUrlField());
		e.setAttribute("requestEntity", httppostMeta.getRequestEntity());
		e.setAttribute("httpLogin", httppostMeta.getHttpLogin());
		e.setAttribute("httpPassword", httppostMeta.getHttpPassword() );
		e.setAttribute("proxyHost", httppostMeta.getProxyHost());
		e.setAttribute("proxyPort", httppostMeta.getProxyPort());
		e.setAttribute("socketTimeout", httppostMeta.getSocketTimeout());
		e.setAttribute("connectionTimeout", httppostMeta.getConnectionTimeout());
		e.setAttribute("closeIdleConnectionsTime", httppostMeta.getCloseIdleConnectionsTime());
		
		e.setAttribute("name", httppostMeta.getFieldName());
		e.setAttribute("code", httppostMeta.getResultCodeFieldName());
		e.setAttribute("response_time", httppostMeta.getResponseTimeFieldName());
		e.setAttribute("response_header", httppostMeta.getResponseHeaderFieldName());
		
		String[] argumentField = httppostMeta.getArgumentField();
		boolean[] argumentHeader = httppostMeta.getArgumentHeader();
		String[] argumentParameter = httppostMeta.getArgumentParameter();
		JSONArray jsonArray = new JSONArray();
		if(argumentField != null) {
			for (int i = 0; i < argumentField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", argumentField[i]);
				jsonObject.put("header", argumentHeader[i] ? "Y" : "N");
				jsonObject.put("parameter", argumentParameter[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("arg", jsonArray.toString());
		
		jsonArray = new JSONArray();
		String[] queryField = httppostMeta.getQueryField();
		String[] queryParameter = httppostMeta.getQueryParameter();
		if(queryField != null) {
			for (int i = 0; i < queryField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", queryField[i]);
				jsonObject.put("parameter", queryParameter[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("query", jsonArray.toString());
		
		return e;
	}

}
