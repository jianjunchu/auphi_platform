package com.aofei.kettle.trans.steps.tableoutput;

import java.net.URLDecoder;

import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaInteger;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.PluginFactory;
import com.aofei.kettle.base.GraphCodec;
import com.aofei.kettle.utils.JsonUtils;
import com.aofei.kettle.utils.StringEscapeHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value="/tableoutput")
@Api(tags = "Transformation转换 - 表输出 - 接口api")
public class TableOutputController {

	private static final Class PKG = TableOutputMeta.class;

	@ApiOperation(value = "生成表输出组件的SQL脚本")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "graphXml", value = "图形信息", paramType="query", dataType = "string"),
        @ApiImplicitParam(name = "stepName", value = "环节名称", paramType="query", dataType = "string")
	})
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST, value="/generateSQL")
	protected void generateSQL(@RequestParam String graphXml, @RequestParam String stepName, @CurrentUser CurrentUserResponse user) throws Exception {
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);

		StepMeta stepMeta = transMeta.findStep( URLDecoder.decode(stepName, "utf-8") );
		TableOutputMeta info = (TableOutputMeta) stepMeta.getStepMetaInterface();

		RowMetaInterface prev = transMeta.getPrevStepFields(stepName);
		if (info.isTableNameInField() && !info.isTableNameInTable() && info.getTableNameField().length() > 0) {
			int idx = prev.indexOfValue(info.getTableNameField());
			if (idx >= 0) {
				prev.removeValueMeta(idx);
			}
		}

		if (info.specifyFields()) {
			// Only use the fields that were specified.
			RowMetaInterface prevNew = new RowMeta();

			for (int i = 0; i < info.getFieldDatabase().length; i++) {
				ValueMetaInterface insValue = prev.searchValueMeta(info.getFieldStream()[i]);
				if (insValue != null) {
					ValueMetaInterface insertValue = insValue.clone();
					insertValue.setName(info.getFieldDatabase()[i]);
					prevNew.addValueMeta(insertValue);
				} else {
					throw new KettleStepException(BaseMessages.getString(PKG,
							"TableOutputDialog.FailedToFindField.Message", info.getFieldStream()[i]));
				}
			}
			prev = prevNew;
		}

		boolean autoInc = false;
		String pk = null;

		if (info.isReturningGeneratedKeys() && !Utils.isEmpty(info.getGeneratedKeyField())) {
			ValueMetaInterface valueMeta = new ValueMetaInteger(info.getGeneratedKeyField());
			valueMeta.setLength(15);
			prev.addValueMeta(0, valueMeta);
			autoInc = true;
			pk = info.getGeneratedKeyField();
		}

		if (isValidRowMeta(prev)) {
			SQLStatement sql = info.getSQLStatements(transMeta, stepMeta, prev, pk, autoInc, pk);
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

	private static boolean isValidRowMeta(RowMetaInterface rowMeta) {
		for (ValueMetaInterface value : rowMeta.getValueMetaList()) {
			String name = value.getName();
			if (name == null || name.isEmpty()) {
				return false;
			}
		}
		return true;
	}

}
