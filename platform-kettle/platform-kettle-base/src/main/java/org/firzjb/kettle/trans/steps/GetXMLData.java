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
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("getXMLData")
@Scope("prototype")
public class GetXMLData extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {

	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		e.setAttribute("include", ReflectUtils.getFieldBoolean(stepMetaInterface, "includeFilename") ? "Y" : "N");
		e.setAttribute("include_field", ReflectUtils.getString(stepMetaInterface, "filenameField"));
		e.setAttribute("rownum", ReflectUtils.getFieldBoolean(stepMetaInterface, "includeRowNumber") ? "Y" : "N");
		e.setAttribute("addresultfile", ReflectUtils.getFieldBoolean(stepMetaInterface, "addResultFile") ? "Y" : "N");
		e.setAttribute("readurl", ReflectUtils.getFieldBoolean(stepMetaInterface, "readurl") ? "Y" : "N");

		String[] fileName = (String[]) ReflectUtils.get(stepMetaInterface, "fileName");
		String[] fileMask = (String[]) ReflectUtils.get(stepMetaInterface, "fileMask");
		String[] exludeFileMask = (String[]) ReflectUtils.get(stepMetaInterface, "exludeFileMask");
		String[] fileRequired = (String[]) ReflectUtils.get(stepMetaInterface, "fileRequired");
		String[] includeSubFolders = (String[]) ReflectUtils.get(stepMetaInterface, "includeSubFolders");

		JSONArray jsonArray = new JSONArray();
		if(fileName != null) {
			for ( int i = 0; i < fileName.length; i++ ) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", fileName[i]);
				jsonObject.put("filemask", fileMask[i]);
				jsonObject.put("exclude_filemask", exludeFileMask[i]);
				jsonObject.put("file_required", fileRequired[i]);
				jsonObject.put("include_subfolders", includeSubFolders[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("file", jsonArray.toString());

		return e;
	}

}
