package org.firzjb.kettle.trans.steps.word;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.util.StringEvaluationResult;
import org.pentaho.di.core.util.StringEvaluator;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.firzjb.base.annotation.CurrentUser;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.PluginFactory;
import org.firzjb.kettle.base.GraphCodec;
import org.firzjb.kettle.utils.ReflectUtils;
import com.aspose.words.Cell;
import com.aspose.words.CellCollection;
import com.aspose.words.Document;
import com.aspose.words.Row;
import com.aspose.words.Table;

@RestController
@RequestMapping(value="/wordinput")
public class WordInputController {

	@RequestMapping("/fields")
	protected @ResponseBody List fields(@RequestParam String graphXml, @RequestParam String stepName, @CurrentUser CurrentUserResponse user) throws Exception{
		GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
		TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
		StepMeta stepMeta = transMeta.findStep(stepName);
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		Class<? extends StepMetaInterface> PKG = stepMetaInterface.getClass();

		String filename = ReflectUtils.getFieldString(stepMetaInterface, "filename");
		Document document = new Document(filename);
		Table table = (Table) document.getChild(5, 0, true);
		int startRowIndex = new Integer(ReflectUtils.getString(stepMetaInterface, "startRowIndex")).intValue();
		String[] fieldNames = getOneRow(table, startRowIndex);


		Integer nrHeaderLines = (Integer) ReflectUtils.get(stepMetaInterface, "nrHeaderLines");

		if (!ReflectUtils.getBoolean(stepMetaInterface, "headerPresent")) {
			DecimalFormat df = new DecimalFormat("000");
			for (int i = 0; i < fieldNames.length; i++) {
				fieldNames[i] = ("Field_" + df.format(i));
			}
		}

		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = Const.trim(fieldNames[i]);
		}

		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (int i = 0; i < fieldNames.length; i++) {
			HashMap<String, String> rec = new HashMap<String, String>();
			rec.put("name", fieldNames[i]);
			rec.put("type", ValueMeta.getTypeDesc(2));
			rec.put("length", "-1");
			result.add(rec);
		}

		for (int rowIndex = 0; rowIndex < 1000; rowIndex++) {
			Object[] r;
			if (ReflectUtils.getBoolean(stepMetaInterface, "headerPresent"))
				r = getOneRow(table, startRowIndex + nrHeaderLines + rowIndex);
			else
				r = getOneRow(table, startRowIndex + rowIndex);
			if (r == null)
				continue;
			if (rowIndex == 0) {
				List evaluators = new ArrayList();
				int nrfields = fieldNames.length;

				for (int i = 0; (i < nrfields) && (i < r.length); i++) {
					Map<String, String> field = result.get(i);
					StringEvaluator evaluator;
					if (i >= evaluators.size()) {
						evaluator = new StringEvaluator(true);
						evaluators.add(evaluator);
					} else {
						evaluator = (StringEvaluator) evaluators.get(i);
					}

					evaluator.evaluateString(r[i] == null ? null : r[i].toString());
					StringEvaluationResult evaluationResult = evaluator.getAdvicedResult();
					if ((evaluationResult == null) || (evaluationResult == null)) {
						continue;
					}
					ValueMetaInterface conversionMeta = evaluationResult.getConversionMeta();
					field.put("length", String.valueOf(conversionMeta.getLength()));
					field.put("type", ValueMeta.getTypeDesc(conversionMeta.getType()));
					field.put("format", conversionMeta.getConversionMask());
					field.put("currencySymbol", conversionMeta.getCurrencySymbol());
					field.put("decimalSymbol", conversionMeta.getDecimalSymbol());
					field.put("groupSymbol", conversionMeta.getGroupingSymbol());
					field.put("trimtype", ValueMetaString.getTrimTypeDesc(conversionMeta.getTrimType()));

				}

			} else {
				int nrfields = result.size();
				for (int i = 0; (i < nrfields) && (i < r.length); i++) {
					Map<String, String> field = result.get(i);
					int type = ValueMetaFactory.getIdForValueMeta(field.get("type"));
					int length = Integer.parseInt(field.get("length"));

					if ((type == 2) && (r[i] != null) && (r[i].toString().length() > length)) {
						field.put("length", String.valueOf(r[i].toString().length()));
					}
				}
			}
		}


	    return result;
	}

	private String[] getOneRow(com.aspose.words.Table table, int rowIndex) {
	    Row row = table.getRows().get(rowIndex);
	    if (row == null)
	      return null;
	    CellCollection cellc = row.getCells();

	    final ArrayList<String> list = new ArrayList<String>();
	    cellc.forEach(new Consumer<Cell>() {
	      public void accept(Cell tt)
	      {
	        try {
	          String cellText = tt.toString(70).trim().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
	          list.add(cellText);
	          System.out.println(cellText);
	        }
	        catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	    });
	    String[] fieldNames = new String[list.size()];
	    for (int i = 0; i < list.toArray().length; i++)
	    {
	      fieldNames[i] = ((String)list.toArray()[i]);
	    }
	    return fieldNames;
	  }

}
