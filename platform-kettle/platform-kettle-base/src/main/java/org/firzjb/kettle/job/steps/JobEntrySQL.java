package org.firzjb.kettle.job.steps;

import java.util.List;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.job.step.AbstractJobEntry;
import org.firzjb.kettle.utils.StringEscapeHelper;
import org.firzjb.kettle.job.step.AbstractJobEntry;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("SQL")
@Scope("prototype")
public class JobEntrySQL extends AbstractJobEntry {
	@Override
	public void decode(JobEntryInterface jobEntry, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		org.pentaho.di.job.entries.sql.JobEntrySQL jobEntrySQL= (org.pentaho.di.job.entries.sql.JobEntrySQL) jobEntry;
		//一般---服务器设置
		jobEntrySQL.setSQL(StringEscapeHelper.decode(cell.getAttribute("sql")));
		String con = cell.getAttribute( "connection" );
		jobEntrySQL.setDatabase(DatabaseMeta.findDatabase( databases, con ));
		jobEntrySQL.setSQLFromFile("Y".equalsIgnoreCase(cell.getAttribute("sqlfromfile")));
		jobEntrySQL.setSQLFilename(cell.getAttribute("sqlfilename"));
		jobEntrySQL.setSendOneStatement("Y".equalsIgnoreCase(cell.getAttribute("sendOneStatement")));
		jobEntrySQL.setUseVariableSubstitution("Y".equalsIgnoreCase(cell.getAttribute("useVariableSubstitution")));

	}

	@Override
	public Element encode(JobEntryInterface jobEntry) throws Exception    {
		org.pentaho.di.job.entries.sql.JobEntrySQL jobEntrySQL = (org.pentaho.di.job.entries.sql.JobEntrySQL) jobEntry;

		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.JOB_JOBENTRY_NAME);
		//一般---服务器设置
		e.setAttribute("sql", StringEscapeHelper.encode(jobEntrySQL.getSQL()));
		e.setAttribute("connection",  jobEntrySQL.getDatabase() == null ? "" : jobEntrySQL.getDatabase().getName());
		e.setAttribute("sqlfromfile", jobEntrySQL.getSQLFromFile()  ? "Y" : "N");
		e.setAttribute("sqlfilename", jobEntrySQL.getSQLFilename());
		e.setAttribute("sendOneStatement", jobEntrySQL.isSendOneStatement() ? "Y" : "N");
		e.setAttribute("useVariableSubstitution", jobEntrySQL.getUseVariableSubstitution()  ? "Y" : "N");
		return e;
	}



}
