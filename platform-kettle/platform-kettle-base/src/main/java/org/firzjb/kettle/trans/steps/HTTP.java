package org.firzjb.kettle.trans.steps;

import java.util.List;

import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.http.HTTPMeta;
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
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("HTTP")
@Scope("prototype")
public class HTTP extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		HTTPMeta httpMeta = (HTTPMeta) stepMetaInterface;

		httpMeta.setUrl(cell.getAttribute("url"));
		httpMeta.setUrlInField("Y".equalsIgnoreCase(cell.getAttribute("urlInField")));
		httpMeta.setUrlField(cell.getAttribute("urlField"));
		httpMeta.setEncoding(cell.getAttribute("encoding"));
		httpMeta.setSocketTimeout(cell.getAttribute("socketTimeout"));
		httpMeta.setConnectionTimeout(cell.getAttribute("connectionTimeout"));
		httpMeta.setCloseIdleConnectionsTime(cell.getAttribute("closeIdleConnectionsTime"));

		httpMeta.setHttpLogin(cell.getAttribute("httpLogin"));
		httpMeta.setHttpPassword(cell.getAttribute("httpPassword"));
		httpMeta.setProxyHost(cell.getAttribute("proxyHost"));
		httpMeta.setProxyPort(cell.getAttribute("proxyPort"));

		httpMeta.setFieldName(cell.getAttribute("name"));
		httpMeta.setResultCodeFieldName(cell.getAttribute("code"));
		httpMeta.setResponseTimeFieldName(cell.getAttribute("response_time"));
		httpMeta.setResponseHeaderFieldName(cell.getAttribute("response_header"));

		String arg = cell.getAttribute("arg");
		JSONArray argArray = JSONArray.fromObject(arg);
		String query = cell.getAttribute("header");
		JSONArray headerArray = JSONArray.fromObject(query);
		httpMeta.allocate(argArray.size(), headerArray.size());
		for(int i=0; i<argArray.size(); i++) {
			JSONObject jsonObject = argArray.getJSONObject(i);
			httpMeta.getArgumentField()[i] = jsonObject.optString("name");
			httpMeta.getArgumentParameter()[i] = jsonObject.optString("parameter");
		}

		for(int i=0; i<headerArray.size(); i++) {
			JSONObject jsonObject = headerArray.getJSONObject(i);
			httpMeta.getHeaderField()[i] = jsonObject.optString("name");
			httpMeta.getHeaderParameter()[i] = jsonObject.optString("parameter");
		}
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		HTTPMeta httpMeta = (HTTPMeta) stepMetaInterface;

		e.setAttribute("url", httpMeta.getUrl());
		e.setAttribute("urlInField", httpMeta.isUrlInField() ? "Y" : "N");
		e.setAttribute("urlField", httpMeta.getUrlField());
		e.setAttribute("encoding", httpMeta.getEncoding());
		e.setAttribute("socketTimeout", httpMeta.getSocketTimeout());
		e.setAttribute("connectionTimeout", httpMeta.getConnectionTimeout());
		e.setAttribute("closeIdleConnectionsTime", httpMeta.getCloseIdleConnectionsTime());

		e.setAttribute("name", httpMeta.getFieldName());
		e.setAttribute("code", httpMeta.getResultCodeFieldName());
		e.setAttribute("response_time", httpMeta.getResponseTimeFieldName());
		e.setAttribute("response_header", httpMeta.getResponseHeaderFieldName());

		e.setAttribute("httpLogin", httpMeta.getHttpLogin());
		e.setAttribute("httpPassword", httpMeta.getHttpPassword() );
		e.setAttribute("proxyHost", httpMeta.getProxyHost());
		e.setAttribute("proxyPort", httpMeta.getProxyPort());


		String[] argumentField = httpMeta.getArgumentField();
		String[] argumentParameter = httpMeta.getArgumentParameter();
		JSONArray jsonArray = new JSONArray();
		if(argumentField != null) {
			for (int i = 0; i < argumentField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", argumentField[i]);
				jsonObject.put("parameter", argumentParameter[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("arg", jsonArray.toString());

		jsonArray = new JSONArray();
		String[] headerField = httpMeta.getHeaderField();
		String[] headerParameter = httpMeta.getHeaderParameter();
		if(headerField != null) {
			for (int i = 0; i < headerField.length; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", headerField[i]);
				jsonObject.put("parameter", headerParameter[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("header", jsonArray.toString());

		return e;
	}

}
