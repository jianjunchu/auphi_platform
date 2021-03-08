package com.aofei.schedule.util;

import com.alibaba.fastjson.JSON;
import com.aofei.base.common.Const;
import com.aofei.kettle.App;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.quartz.JobDetail;

import java.util.HashMap;
import java.util.Map;

public class RunnerUtil {


    public static TransMeta getTransMeta(JobDetail jobDetail) throws  KettleException {

        Repository repository = App.getInstance().getRepository();
        try {
            String json = (String) jobDetail.getJobDataMap().get(Const.GENERAL_SCHEDULE_KEY);

            GeneralScheduleRequest request = JSON.parseObject(json,GeneralScheduleRequest.class);

            String dir = request.getFilePath();
            String name = request.getFile();


            System.out.println("Trans path ==> " + dir);
            System.out.println("Trans name ==> " + name);
            RepositoryDirectoryInterface directory = repository.findDirectory(dir);
            if(directory == null)
                directory = repository.getUserHomeDirectory();

            TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);



            return transMeta;

        } catch(Exception e) {
            throw e;
        }finally {
            repository.disconnect();
        }
    }

    public static TransExecutionConfiguration getTransExecutionConfiguration(TransMeta transMeta){
        TransExecutionConfiguration executionConfiguration = App.getInstance().getTransExecutionConfiguration();

        if (transMeta.findFirstUsedClusterSchema() != null) {
            executionConfiguration.setExecutingLocally(false);
            executionConfiguration.setExecutingRemotely(false);
            executionConfiguration.setExecutingClustered(true);
        } else {
            executionConfiguration.setExecutingLocally(true);
            executionConfiguration.setExecutingRemotely(false);
            executionConfiguration.setExecutingClustered(false);
        }

        // Remember the variables set previously
        //
        RowMetaAndData variables = App.getInstance().getVariables();
        Object[] data = variables.getData();
        String[] fields = variables.getRowMeta().getFieldNames();
        Map<String, String> variableMap = new HashMap<String, String>();
        for ( int idx = 0; idx < fields.length; idx++ ) {
            variableMap.put( fields[idx], data[idx].toString() );
        }

        executionConfiguration.setVariables( variableMap );
        executionConfiguration.getUsedVariables( transMeta );
        executionConfiguration.getUsedArguments(transMeta, App.getInstance().getArguments());
        executionConfiguration.setReplayDate( null );
        executionConfiguration.setRepository( App.getInstance().getRepository() );
        executionConfiguration.setSafeModeEnabled( false );

        executionConfiguration.setLogLevel(LogLevel.MINIMAL );

        // Fill the parameters, maybe do this in another place?
        Map<String, String> params = executionConfiguration.getParams();
        params.clear();
        String[] paramNames = transMeta.listParameters();
        for (String key : paramNames) {
            params.put(key, "");
        }

        return executionConfiguration;
    }

}
