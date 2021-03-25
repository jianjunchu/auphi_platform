package org.firzjb.kettle.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import org.firzjb.kettle.utils.StringEscapeHelper;
import org.firzjb.kettle.PluginFactory;
import org.firzjb.kettle.job.step.JobEntryDecoder;
import org.firzjb.kettle.job.step.JobEntryEncoder;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.gui.Point;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.core.plugins.JobEntryPluginType;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.missing.MissingEntry;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.laf.BasePropertyHandler;
import org.pentaho.di.trans.step.StepMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.firzjb.kettle.PluginFactory;
import org.firzjb.kettle.base.BaseGraphCodec;
import org.firzjb.kettle.base.GraphCodec;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.job.step.JobEntryDecoder;
import org.firzjb.kettle.job.step.JobEntryEncoder;
import org.firzjb.kettle.utils.SvgImageUrl;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

@Component(GraphCodec.JOB_CODEC)
@Scope("prototype")
public class JobMetaCodec extends BaseGraphCodec {

	@Override
	public String encode(AbstractMeta meta, CurrentUserResponse user) throws Exception {
		JobMeta jobMeta = (JobMeta) meta;

		mxGraph graph = new mxGraph();
		graph.getModel().beginUpdate();
		mxCell parent = (mxCell) graph.getDefaultParent();

		try {
			Document doc = mxUtils.createDocument();
			Element e = super.encodeCommRootAttr(jobMeta, doc);
			e.setAttribute("job_version", jobMeta.getJobversion());
			e.setAttribute("job_status", String.valueOf(jobMeta.getJobstatus()));
			e.setAttribute("pass_batchid", jobMeta.isBatchIdPassed() ? "Y" : "N");

		    encodeDatabases(e, jobMeta);
		    encodeSlaveServers(e, jobMeta);
		    parent.setValue(e);

		    encodeNote(doc, graph, jobMeta);

		    // encode steps and hops
			HashMap<JobEntryCopy, Object> cells = new HashMap<JobEntryCopy, Object>();
			for(int i=0; i<jobMeta.nrJobEntries(); i++) {
				JobEntryCopy jge = jobMeta.getJobEntry( i );
				Point p = jge.getLocation();
				String pluginId = jge.getEntry().getPluginId();
				JobEntryEncoder stepEncoder = (JobEntryEncoder) PluginFactory.getBean(jge.getEntry().getPluginId());

				PluginInterface plugin = PluginRegistry.getInstance().getPlugin(JobEntryPluginType.class, pluginId);
				String image = SvgImageUrl.getUrl(plugin);
				if(jge.isDummy())
					image = SvgImageUrl.getUrl(BasePropertyHandler.getProperty( "DUM_image" ));
				if(jge.isStart())
					image = SvgImageUrl.getUrl(BasePropertyHandler.getProperty( "STR_image" ));

				Object cell = graph.insertVertex(parent, null, stepEncoder.encodeStep(jge), p.x, p.y, PropsUI.STEP_SIZE, PropsUI.STEP_SIZE, "icon;image=" + image);
				cells.put(jge, cell);
			}

			for(int i=0; i<jobMeta.nrJobHops(); i++) {
				JobHopMeta jobHopMeta = jobMeta.getJobHop(i);

				mxCell source = (mxCell) cells.get(jobHopMeta.getFromEntry());
				mxCell target = (mxCell) cells.get(jobHopMeta.getToEntry());

				String style = null;
				if(!jobHopMeta.isUnconditional() && !jobHopMeta.getEvaluation())
					style = "error";
				graph.insertEdge(parent, null, JobHopMetaCodec.encode(jobHopMeta), source, target, style);
			}

			JobLogTable jobLogTable = jobMeta.getJobLogTable();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put( "connection", jobLogTable.getConnectionName() );
			jsonObject.put( "schema", jobLogTable.getSchemaName() );
			jsonObject.put( "table", jobLogTable.getTableName() );
			jsonObject.put( "size_limit_lines", jobLogTable.getLogSizeLimit() );
			jsonObject.put( "interval", jobLogTable.getLogInterval() );
			jsonObject.put( "timeout_days", jobLogTable.getTimeoutInDays() );
			JSONArray fields = new JSONArray();
			for ( LogTableField field : jobLogTable.getFields() ) {
				JSONObject jsonField = new JSONObject();
				jsonField.put("id", field.getId());
				jsonField.put("enabled", field.isEnabled());
				jsonField.put("name", field.getFieldName());
				jsonField.put("description", StringEscapeHelper.encode(field.getDescription()));
				fields.add(jsonField);
			}
			jsonObject.put("fields", fields);
			e.setAttribute("jobLogTable", jsonObject.toString());


			JobEntryLogTable jobEntryLogTable = jobMeta.getJobEntryLogTable();
			jsonObject = new JSONObject();
			jsonObject.put( "connection", jobEntryLogTable.getConnectionName() );
			jsonObject.put( "schema", jobEntryLogTable.getSchemaName() );
			jsonObject.put( "table", jobEntryLogTable.getTableName() );
			jsonObject.put( "timeout_days", jobEntryLogTable.getTimeoutInDays() );
			fields = new JSONArray();
			for ( LogTableField field : jobEntryLogTable.getFields() ) {
				JSONObject jsonField = new JSONObject();
				jsonField.put("id", field.getId());
				jsonField.put("enabled", field.isEnabled());
				jsonField.put("name", field.getFieldName());
				jsonField.put("description", StringEscapeHelper.encode(field.getDescription()));
				fields.add(jsonField);
			}
			jsonObject.put("fields", fields);
			e.setAttribute("jobEntryLogTable", jsonObject.toString());


			ChannelLogTable channelLogTable = jobMeta.getChannelLogTable();
			jsonObject = new JSONObject();
			jsonObject.put( "connection", channelLogTable.getConnectionName() );
			jsonObject.put( "schema", channelLogTable.getSchemaName() );
			jsonObject.put( "table", channelLogTable.getTableName() );
			jsonObject.put( "timeout_days", channelLogTable.getTimeoutInDays() );
			fields = new JSONArray();
			for ( LogTableField field : channelLogTable.getFields() ) {
				JSONObject jsonField = new JSONObject();
				jsonField.put("id", field.getId());
				jsonField.put("enabled", field.isEnabled());
				jsonField.put("name", field.getFieldName());
				jsonField.put("description", StringEscapeHelper.encode(field.getDescription()));
				fields.add(jsonField);
			}
			jsonObject.put("fields", fields);
			e.setAttribute("channelLogTable", jsonObject.toString());
		} finally {
			graph.getModel().endUpdate();
		}

		mxCodec codec = new mxCodec();
		return mxUtils.getPrettyXml(codec.encode(graph.getModel()));
	}

	@Override
	public AbstractMeta decode(String graphXml, CurrentUserResponse user) throws Exception {
		mxGraph graph = new mxGraph();
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.parseXml(graphXml);
		codec.decode(doc.getDocumentElement(), graph.getModel());
		mxCell root = (mxCell) graph.getDefaultParent();

		JobMeta jobMeta = new JobMeta();
		decodeCommRootAttr(root, jobMeta);
		jobMeta.setJobversion(root.getAttribute("job_version"));
		int jobStatus = Const.toInt(root.getAttribute("job_status"), -1);
		if(jobStatus >= 0)
			jobMeta.setJobstatus(jobStatus);
		jobMeta.setBatchIdPassed("Y".equalsIgnoreCase(root.getAttribute("pass_batchid")));

		decodeDatabases(root, jobMeta);
		decodeNote(graph, jobMeta);
		decodeSlaveServers(root, jobMeta);

		int count = graph.getModel().getChildCount(root);
		for(int i=0; i<count; i++) {
			mxCell cell = (mxCell) graph.getModel().getChildAt(root, i);
			if(cell.isVertex()) {
				Element e = (Element) cell.getValue();
				if(PropsUI.JOB_JOBENTRY_NAME.equals(e.getTagName())) {
					JobEntryDecoder jobEntryDecoder = (JobEntryDecoder) PluginFactory.getBean(cell.getAttribute("ctype"));
					JobEntryCopy je = jobEntryDecoder.decodeStep(cell, jobMeta.getDatabases(), jobMeta.getMetaStore(), user);
					if (je.isSpecial() && je.isMissing()) {
						jobMeta.addMissingEntry((MissingEntry) je.getEntry());
					}

					JobEntryCopy prev = jobMeta.findJobEntry(je.getName(), 0, true);
					if (prev != null) {
						if (je.getNr() == 0) {
							int idx = jobMeta.indexOfJobEntry(prev);
							jobMeta.removeJobEntry(idx);
						} else if (je.getNr() > 0) {
							je.setEntry(prev.getEntry());

							// See if entry already exists...
							prev = jobMeta.findJobEntry(je.getName(), je.getNr(), true);
							if (prev != null) {
								int idx = jobMeta.indexOfJobEntry(prev);
								jobMeta.removeJobEntry(idx);
							}
						}
					}
					jobMeta.addJobEntry( je );
				}
			} else if(cell.isEdge()) {
//				mxCell source = (mxCell) cell.getSource();
//				mxCell target = (mxCell) cell.getTarget();

				JobHopMeta hopinf = JobHopMetaCodec.decode(jobMeta, cell);//new JobHopMeta();
//				for (int j = 0; j < jobMeta.nrJobEntries(); j++) {
//					JobEntryCopy jobEntry = jobMeta.getJobEntry(j);
//					if (jobEntry.getName().equalsIgnoreCase(source.getAttribute("label")))
//						hopinf.setFromEntry(jobEntry);
//					if (jobEntry.getName().equalsIgnoreCase(target.getAttribute("label")))
//						hopinf.setToEntry(jobEntry);
//				}
				jobMeta.addJobHop(hopinf);
			}
		}

		JSONObject jsonObject = JSONObject.fromObject(root.getAttribute("jobLogTable"));
		JobLogTable jobLogTable = jobMeta.getJobLogTable();
		jobLogTable.setConnectionName(jsonObject.optString("connection"));
		jobLogTable.setSchemaName(jsonObject.optString("schema"));
		jobLogTable.setTableName(jsonObject.optString("table"));
		jobLogTable.setLogSizeLimit(jsonObject.optString("size_limit_lines"));
		jobLogTable.setLogInterval(jsonObject.optString("interval"));
		jobLogTable.setTimeoutInDays(jsonObject.optString("timeout_days"));
		JSONArray jsonArray = jsonObject.optJSONArray("fields");
		if(jsonArray != null) {
			for ( int i = 0; i < jsonArray.size(); i++ ) {
				JSONObject fieldJson = jsonArray.getJSONObject(i);
				String id = fieldJson.optString("id");
				LogTableField field = jobLogTable.findField( id );
				if ( field == null ) {
					field = jobLogTable.getFields().get(i);
				}
				if (field != null) {
					field.setFieldName(fieldJson.optString("name"));
					field.setEnabled(fieldJson.optBoolean("enabled"));
				}
			}
		}

		jsonObject = JSONObject.fromObject(root.getAttribute("jobEntryLogTable"));
		JobEntryLogTable jobEntryLogTable = jobMeta.getJobEntryLogTable();
		jobEntryLogTable.setConnectionName(jsonObject.optString("connection"));
		jobEntryLogTable.setSchemaName(jsonObject.optString("schema"));
		jobEntryLogTable.setTableName(jsonObject.optString("table"));
		jobEntryLogTable.setTimeoutInDays(jsonObject.optString("timeout_days"));
		jsonArray = jsonObject.optJSONArray("fields");
		if(jsonArray != null) {
			for ( int i = 0; i < jsonArray.size(); i++ ) {
				JSONObject fieldJson = jsonArray.getJSONObject(i);
				String id = fieldJson.optString("id");
				LogTableField field = jobEntryLogTable.findField( id );
				if ( field == null && i<jobEntryLogTable.getFields().size()) {
					field = jobEntryLogTable.getFields().get(i);
				}
				if (field != null) {
					field.setFieldName(fieldJson.optString("name"));
					field.setEnabled(fieldJson.optBoolean("enabled"));
				}
			}
		}

		jsonObject = JSONObject.fromObject(root.getAttribute("channelLogTable"));
		ChannelLogTable channelLogTable = jobMeta.getChannelLogTable();
		channelLogTable.setConnectionName(jsonObject.optString("connection"));
		channelLogTable.setSchemaName(jsonObject.optString("schema"));
		channelLogTable.setTableName(jsonObject.optString("table"));
		channelLogTable.setTimeoutInDays(jsonObject.optString("timeout_days"));
		jsonArray = jsonObject.optJSONArray("fields");
		if(jsonArray != null) {
			for ( int i = 0; i < jsonArray.size(); i++ ) {
				JSONObject fieldJson = jsonArray.getJSONObject(i);
				String id = fieldJson.optString("id");
				LogTableField field = channelLogTable.findField( id );
				if ( field == null && i<channelLogTable.getFields().size()) {
					field = channelLogTable.getFields().get(i);
				}
				if (field != null) {
					field.setFieldName(fieldJson.optString("name"));
					field.setEnabled(fieldJson.optBoolean("enabled"));
				}
			}
		}

		return jobMeta;
	}

	@Override
	public boolean isDatabaseConnectionUsed(AbstractMeta meta, DatabaseMeta databaseMeta) {
		JobMeta jobMeta = (JobMeta) meta;

		Set<DatabaseMeta> databaseMetas = new HashSet<DatabaseMeta>();
		for (JobEntryCopy jobEntryCopy : jobMeta.getJobCopies()) {
			DatabaseMeta[] dbs = jobEntryCopy.getEntry().getUsedDatabaseConnections();
			if (dbs != null) {
				for (DatabaseMeta db : dbs) {
					databaseMetas.add(db);
				}
			}
		}

		JobLogTable jobLogTable = jobMeta.getJobLogTable();
		databaseMetas.add(jobLogTable.getDatabaseMeta());

		for (LogTableInterface logTable : jobMeta.getExtraLogTables()) {
			databaseMetas.add(logTable.getDatabaseMeta());
		}

		return databaseMetas.contains(databaseMeta);
	}

}
