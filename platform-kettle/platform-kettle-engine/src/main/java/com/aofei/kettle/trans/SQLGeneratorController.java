package com.aofei.kettle.trans;

import java.net.URLDecoder;

import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.pgbulkloader.PGBulkLoaderMeta;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.App;
import com.aofei.kettle.PluginFactory;
import com.aofei.kettle.base.GraphCodec;
import com.aofei.kettle.utils.JsonUtils;
import com.aofei.kettle.utils.StringEscapeHelper;

@Controller
@RequestMapping(value="/sqlgen")
public class SQLGeneratorController {

	@ResponseBody
	@RequestMapping(method=RequestMethod.POST, value="/pgBulkLoader")
	protected void pgBulkLoader(@RequestParam String graphXml, @RequestParam String stepName, @CurrentUser CurrentUserResponse user) throws Exception {
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		
		StepMeta stepMeta = transMeta.findStep( URLDecoder.decode(stepName, "utf-8") );
		PGBulkLoaderMeta info = (PGBulkLoaderMeta) stepMeta.getStepMetaInterface();
		
		RowMetaInterface prev = transMeta.getPrevStepFields( stepName );
		SQLStatement sql = info.getSQLStatements( transMeta, stepMeta, prev, App.getInstance().getRepository(), App.getInstance().getMetaStore() );
		
		if (!sql.hasError()) {
			if (sql.hasSQL()) {
				JsonUtils.success(StringEscapeHelper.encode(sql.getSQL()));
			} else {
				JsonUtils.fail("对不起，没有SQL生成！");
			}
		} else {
			JsonUtils.fail(sql.getError());
		}
	}
	
}
