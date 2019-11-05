/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2017 by Auphi BI : http://www.doetl.com 

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0 

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package com.auphi.ktrl.schedule.template;

import com.alibaba.fastjson.JSON;
import com.auphi.data.hub.domain.Hadoop;
import com.auphi.ktrl.engine.impl.KettleEngineImpl4_3;
import com.auphi.ktrl.monitor.domain.MonitorScheduleBean;
import com.auphi.ktrl.schedule.util.ScheduleUtil;
import com.auphi.ktrl.schedule.view.FastConfigView;
import com.auphi.ktrl.schedule.view.FieldMappingView;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.sql.JobEntrySQL;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;

import java.util.Date;
import java.util.List;

/**
 * Template131, 数据库到hadoop
 *
 */
public class Template131 implements Template {

	private String middlePath;
	private JobMeta jobMeta;
	private Repository rep;
	private Date date;
	private boolean isReload;
	
	private static final String sepChar = "|";
	
	/**
	 * init repository and jobMeta from repName and middlePath(same to templateClassName)
	 * @param middlePath
	 * @param repName
	 * @param date
	 * @param isReload
	 */
	public Template131(String repName, String middlePath, Date date, boolean isReload) throws Exception{
		this.middlePath = middlePath;
		this.date = date;
		this.isReload = isReload;
		//instantiation the repository and jobMeta by jobName and repName
		KettleEngineImpl4_3 kettleEngine = new KettleEngineImpl4_3();
		this.rep = (Repository)kettleEngine.getRep(repName);
		RepositoryDirectoryInterface directory = (RepositoryDirectoryInterface)kettleEngine.getDirectory(rep, TEMPLATE_PATH + "/" + middlePath);
		this.jobMeta = rep.loadJob(JOB_NAME, directory, null, null);
	}
	
	public String getTemplateClassName(){
		return middlePath;
	}
	
	@Override
	public void bind(String fastConfigJson, String fieldMappingJson)  throws Exception{
		FastConfigView fastConfigView = JSON.parseObject(fastConfigJson, FastConfigView.class);
		List<FieldMappingView> fieldMappingList = JSON.parseArray(fieldMappingJson, FieldMappingView.class);
		int idHadoop = fastConfigView.getIdDestHadoop();
		Hadoop hadoopConfig = ScheduleUtil.getHadoopConfig(idHadoop);
		
		String destFilePath = fastConfigView.getDestFilePath();
		String path =null;
		String datasourceName = null;
		if(destFilePath.indexOf(":")>-1)
		{
			datasourceName = destFilePath.substring(0,destFilePath.indexOf(":"));
			path = destFilePath.substring(destFilePath.indexOf(":")+1,destFilePath.length());
		}
		else
		{
			path = destFilePath;
		}
		String destFile = "hdfs://" + hadoopConfig.getServer() + ":" + hadoopConfig.getPort() + 
						    TemplateUtil.replaceVariable(path, date, isReload)+ "/" + TemplateUtil.replaceVariable(fastConfigView.getDestFileName(), date, isReload);
//		String destFile = "hdfs://" + hadoopConfig.getServer() + ":" + hadoopConfig.getPort() + 
//						  "/" + fastConfigView.getDestFilePath() + "/" + fastConfigView.getDestFileName();
		
		if(datasourceName!=null)
		{
			DatabaseMeta databaseMeta= DatabaseMeta.findDatabase(jobMeta.getDatabases(), datasourceName);
			if(databaseMeta!=null)
			{
				List<JobEntryCopy> entries = jobMeta.getJobCopies();
				for (int i=0;i<entries.size();i++)
				{
					JobEntryCopy entry = entries.get(i);
					if(entry.getName().equalsIgnoreCase("\u5efa\u8868SQL"))           //建表SQL
					{
						((JobEntrySQL)entry.getEntry()).setDatabase(databaseMeta);
					}
				}
			}else
			{
				throw new Exception("can't find the hive database:"+datasourceName);
			}
		}
		String[] arguments = new String[6];
		arguments[0] = String.valueOf(fastConfigView.getIdSourceDatabase());
		arguments[1] = TemplateUtil.replaceVariable(generateSourceSQL(fastConfigView.getSourceTableName(), fieldMappingList, fastConfigView.getSourceCondition()), date, isReload);
		arguments[2] = destFile;
		arguments[3] = fieldMappingJson;
//		Properties params = ScheduleUtil.getParameter();
//		String middleFile = params.getProperty("RECEIVE_FILE_PATH") + File.separator + "131_" + 
//							StringUtil.createNumberString(9) + File.separator + "data.txt";
//		arguments[4] = middleFile;
		arguments[4] = TemplateUtil.replaceVariable(generateCreateHiveTableSQL(fastConfigView.getDestFileName(), fieldMappingList), date, isReload);
		arguments[5] = sepChar;
		jobMeta.setArguments(arguments);		
		for(int i=0;i<arguments.length;i++)
			System.out.println("arguments["+i+"]="+arguments[i]);			
	}

	@Override
	public boolean execute(int execType, MonitorScheduleBean monitorSchedule) throws Exception{
		boolean success = false;

		KettleEngineImpl4_3 kettleEngine = new KettleEngineImpl4_3();
		kettleEngine.executeJob(jobMeta, rep, null, null, execType, monitorSchedule);
		
		return success;
	}
	
	private String generateSourceSQL(String sourceTableName, List<FieldMappingView> fieldMappingList, String conditions) {
		StringBuffer bf = new StringBuffer();
		bf.append("SELECT ");
		for(int i = 0; i<fieldMappingList.size();i++)
		{
			bf.append(fieldMappingList.get(i).getSourceColumnName());    
			if(i != fieldMappingList.size()-1)
				bf.append(",");
		}
		bf.append(" FROM ")
		  .append(sourceTableName);
		if(conditions!=null && conditions.length()>0)
		{
			bf.append(" WHERE ");
			bf.append(conditions);
		}
		return bf.toString();
	}
	
	private String generateCreateHiveTableSQL(String destTableName,
			List<FieldMappingView> fieldMappingList) {
		StringBuffer bf = new StringBuffer();
		bf.append("CREATE TABLE  IF NOT EXISTS ").append(destTableName);
		bf.append("(");
		for(int i = 0; i<fieldMappingList.size();i++)
		{
			String fieldName= fieldMappingList.get(i).getDestColumuName();			
			bf.append(fieldName);    

			int type = ValueMetaInterface.TYPE_STRING;
			int length=0;
			int scale = 0;
			try{
				length = new Integer(fieldMappingList.get(i).getDestLength()).intValue();
				scale = new Integer(fieldMappingList.get(i).getDestScale()).intValue();
				type = ValueMeta.getType(fieldMappingList.get(i).getDestColumnType());
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			bf.append(" "+ TemplateUtil.getFieldDefinition(new ValueMeta(fieldName,type,length,scale)));		// public ValueMeta(String name, int type, int length, int precision)
			if(i!=fieldMappingList.size()-1)
				bf.append(",");
		}
		bf.append(") row format delimited fields terminated by '")
		  .append(sepChar)
		  .append("';");	
		return bf.toString();
	}
}
