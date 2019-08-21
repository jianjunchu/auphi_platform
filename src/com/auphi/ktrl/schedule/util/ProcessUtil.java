package com.auphi.ktrl.schedule.util;

import com.auphi.ktrl.monitor.util.MonitorUtil;
import org.apache.log4j.Logger;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessUtil {

    private static Logger logger = Logger.getLogger(ProcessUtil.class);

    public static boolean runProcess(int monitorId, String command) throws KettleException {
        String result = "";

        List<String> commands = new ArrayList<String>();
        String parameterLine = null;


        if (Const.getOS().equals("Windows 95")) {
            commands.add("command.com");
            commands.add("/C");
            commands.add(command);
            if (parameterLine != null)
                commands.add(parameterLine);

        } else if (Const.getOS().startsWith("Windows")) {
            commands.add("cmd");
            commands.add("/C");
            commands.add(command);
            if (parameterLine != null)
                commands.add(parameterLine);
        } else {
            commands.add("sh");
            commands.add("-c");
            commands.add(command);

        }
        ProcessBuilder procBuilder = new ProcessBuilder(commands);
        try {
            Process process = procBuilder.start();

            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                result += lineStr;
            }
            br.close();
            in.close();

            process.waitFor();
            int exitValue = process.exitValue();
            if(exitValue == 0){
                logger.info(result);
                return true;
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Execute Before Sell Error:"+e.getMessage());
            result = e.getMessage();
        }



        MonitorUtil.updateMonitorAfterError(monitorId,"Execute Before Sell Error:"+result);
        return false;

    }


}
