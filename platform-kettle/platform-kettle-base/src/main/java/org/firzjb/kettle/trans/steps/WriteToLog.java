package org.firzjb.kettle.trans.steps;

import org.firzjb.base.annotation.CurrentUser;
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
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.writetolog.WriteToLogMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("WriteToLog")
@Scope("prototype")
public class WriteToLog extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		WriteToLogMeta writeToLogMeta = (WriteToLogMeta) stepMetaInterface;

		writeToLogMeta.setLogLevel(LogLevel.getLogLevelForCode(cell.getAttribute("loglevel")).getLevel());
		writeToLogMeta.setdisplayHeader("Y".equalsIgnoreCase(cell.getAttribute("displayHeader")));
		writeToLogMeta.setLimitRows("Y".equalsIgnoreCase(cell.getAttribute("limitRows")));
		writeToLogMeta.setLimitRowsNumber(Const.toInt(cell.getAttribute("limitRowsNumber"), 5));
		writeToLogMeta.setLogMessage(StringEscapeHelper.decode(cell.getAttribute("logmessage")));

		String fields = cell.getAttribute("fields");
		JSONArray jsonArray = JSONArray.fromObject(fields);
		String[] fieldName = new String[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			fieldName[i] = jsonObject.optString("name");
		}
		writeToLogMeta.setFieldName(fieldName);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		WriteToLogMeta writeToLogMeta = (WriteToLogMeta) stepMetaInterface;

		e.setAttribute("loglevel", writeToLogMeta.getLogLevelByDesc().getCode());
		e.setAttribute("displayHeader", writeToLogMeta.isdisplayHeader() ? "Y" : "N");
		e.setAttribute("limitRows", writeToLogMeta.isLimitRows() ? "Y" : "N");
		e.setAttribute("limitRowsNumber", writeToLogMeta.getLimitRowsNumber() + "");
		e.setAttribute("logmessage", StringEscapeHelper.encode(writeToLogMeta.getLogMessage()));

		JSONArray jsonArray = new JSONArray();
		String[] fieldName = writeToLogMeta.getFieldName();
		for(int j=0; j<fieldName.length; j++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", fieldName[j]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("fields", jsonArray.toString());

		return e;
	}

}
