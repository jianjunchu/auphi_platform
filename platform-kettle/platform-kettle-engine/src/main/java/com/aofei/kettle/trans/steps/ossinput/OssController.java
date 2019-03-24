package com.aofei.kettle.trans.steps.ossinput;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.mergejoin.MergeJoinMeta;
import org.pentaho.di.trans.steps.textfileinput.EncodingType;
import org.pentaho.di.trans.steps.textfileinput.TextFileInput;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.PluginFactory;
import com.aofei.kettle.base.GraphCodec;
import com.aofei.kettle.utils.JsonUtils;
import com.aofei.kettle.utils.ReflectUtils;

@RestController
@RequestMapping(value="/oss")
public class OssController {

	@RequestMapping("/test")
	protected @ResponseBody void test(@RequestParam String graphXml, @RequestParam String stepName, @CurrentUser CurrentUserResponse user) throws Exception{
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class<? extends StepMetaInterface> PKG = stepMetaInterface.getClass();
		
		String endpoint = ReflectUtils.getFieldString(stepMetaInterface, "endpoint");
		String accessKeyId = ReflectUtils.getFieldString(stepMetaInterface, "accessKeyId");
		String accessKeySecret = ReflectUtils.getFieldString(stepMetaInterface, "accessKeySecret");
		String bucket = ReflectUtils.getFieldString(stepMetaInterface, "bucket");
		
	    StringBuffer message = new StringBuffer();

	    endpoint = transMeta.environmentSubstitute(endpoint);
	    accessKeyId = transMeta.environmentSubstitute(accessKeyId);
	    accessKeySecret = Utils.resolvePassword(transMeta, accessKeySecret);

	    bucket = transMeta.environmentSubstitute(bucket);

	    
	    OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	    try
	    {
	      if (!client.doesBucketExist(bucket))
	      {
	          message.append("No such Bucket.");

	        
	          String msg = BaseMessages.getString(PKG, "OSSInputDialog.Connected.Warning", new String[0]) + Const.CR + Const.NVL(message.toString(), "");
		      String title = BaseMessages.getString(PKG, "OSSInputDialog.Connected.Title.Warning", new String[0]);
		      
		      JsonUtils.success(title, msg);
		      return;
	      } else {
	    	  String msg = BaseMessages.getString(PKG, "OSSInputDialog.Connected.OK", new String[0]);
	    	  String title = BaseMessages.getString(PKG, "OSSInputDialog.Connected.Title.OK", new String[0]);
	    	  
	    	  JsonUtils.success(title, msg);
			  return;
	      }
	    }
	    catch (OSSException oe) {
	      message.append("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.")
	        .append(Const.CR);

	      message.append("Error Code:").append(oe.getErrorCode()).append(Const.CR);
	      message.append("Request ID:").append(oe.getRequestId()).append(Const.CR);
	      message.append("Host ID:").append(oe.getHostId());
	    }
	    catch (ClientException ce)
	    {
	      message.append("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.")
	        .append(Const.CR);

	      message.append("Error Message:").append(ce.getMessage());
	    }
	    finally
	    {
	      client.shutdown();
	    }

	    String msg = BaseMessages.getString(PKG, "OSSInputDialog.Connected.No", new String[0]) + Const.CR + Const.NVL(message.toString(), "");
  	  	String title = BaseMessages.getString(PKG, "OSSInputDialog.Connected.Title.No", new String[0]);
  	  
  	  	JsonUtils.success(title, msg);
	}
	
	@RequestMapping("/fields")
	protected @ResponseBody List fields(@RequestParam String graphXml, @RequestParam String stepName, @CurrentUser CurrentUserResponse user) throws Exception{
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class<? extends StepMetaInterface> PKG = stepMetaInterface.getClass();
		
		String endpoint = ReflectUtils.getFieldString(stepMetaInterface, "endpoint");
		String accessKeyId = ReflectUtils.getFieldString(stepMetaInterface, "accessKeyId");
		String accessKeySecret = ReflectUtils.getFieldString(stepMetaInterface, "accessKeySecret");
		String bucket = ReflectUtils.getFieldString(stepMetaInterface, "bucket");
		String prefix = ReflectUtils.getFieldString(stepMetaInterface, "prefix");
		String delimiter = ReflectUtils.getFieldString(stepMetaInterface, "delimiter");
		String enclosure = ReflectUtils.getFieldString(stepMetaInterface, "enclosure");
		String encoding = ReflectUtils.getFieldString(stepMetaInterface, "encoding");
		boolean header = ReflectUtils.getFieldBoolean(stepMetaInterface, "header");
		
		ArrayList list = new ArrayList();
		
		OSSClient client = null;
	    InputStream inputStream = null;
	    try
	    {
	    	
	    	endpoint = transMeta.environmentSubstitute(endpoint);
		    accessKeyId = transMeta.environmentSubstitute(accessKeyId);
		    accessKeySecret = Utils.resolvePassword(transMeta, accessKeySecret);

		    bucket = transMeta.environmentSubstitute(bucket);
		    prefix = transMeta.environmentSubstitute(prefix);
		    delimiter = transMeta.environmentSubstitute(delimiter);
		    enclosure = transMeta.environmentSubstitute(enclosure);
		    encoding = transMeta.environmentSubstitute(encoding);

		    client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		    if (!client.doesBucketExist(bucket))
		    {
		    	return list;
		    }
		    ObjectListing objects = client.listObjects(bucket, prefix);
		    List summaries = objects.getObjectSummaries();
		    if ((summaries == null) || (summaries.size() == 0))
		    {
		    	return list;
		    }

		    OSSObject ossObject = client.getObject(bucket, ((OSSObjectSummary)summaries.get(0)).getKey());
		    inputStream = ossObject.getObjectContent();

		    InputStreamReader reader;
		    if (Utils.isEmpty(encoding))
		    	reader = new InputStreamReader(inputStream);
		    else {
		    	reader = new InputStreamReader(inputStream, encoding);
		    }

	      EncodingType encodingType = EncodingType.guessEncodingType(reader.getEncoding());
	      
	      Class clazz = stepMetaInterface.getClass().getClassLoader().loadClass("org.pentaho.di.trans.steps.ossinput.OSSInput");

	      String line = TextFileInput.getLine(loggingObject, reader, encodingType, 1, new StringBuilder(1000));
	      String[] fieldNames = (String[]) ReflectUtils.callStatic(clazz, "guessStringsFromLine", loggingObject, line, delimiter, enclosure, null);
	      if (!header)
	      {
	        DecimalFormat df = new DecimalFormat("000");
	        for (int i = 0; i < fieldNames.length; i++) {
	          fieldNames[i] = ("Field_" + df.format(i));
	        }
	      }
	      else if (!Utils.isEmpty(enclosure)) {
	        for (int i = 0; i < fieldNames.length; i++) {
	          if ((fieldNames[i].startsWith(enclosure)) && (fieldNames[i].endsWith(enclosure)) && (fieldNames[i].length() > 1)) {
	            fieldNames[i] = fieldNames[i].substring(1, fieldNames[i].length() - 1);
	          }

	        }

	      }

	      for (int i = 0; i < fieldNames.length; i++) {
	        fieldNames[i] = Const.trim(fieldNames[i]);
	      }

	      for (int i = 0; i < fieldNames.length; i++) {
	        LinkedHashMap rec = new LinkedHashMap();
			rec.put("name", fieldNames[i]);
			rec.put("type", "String");
			list.add(rec);
	        
	      }

	      
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    } finally {
	      try {
	        inputStream.close();
	      }
	      catch (Exception localException5) {
	      }
	      if (client != null)
	        client.shutdown();
	    }
	    
	    return list;
	}
	
	public static final LogChannelInterface loggingObject = new LogChannel("OssController");
	
}
