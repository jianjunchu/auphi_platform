package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.JSONArray;
import org.firzjb.kettle.utils.JSONObject;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.calculator.CalculatorMeta;
import org.pentaho.di.trans.steps.calculator.CalculatorMetaFunction;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Contains the meta-data for the Calculator step: calculates predefined formula's
 *  * @auther 制证数据实时汇聚系统
 *  * @create 2018-09-15 20:07
 */
@Component("Calculator")
@Scope("prototype")
public class Calculator extends AbstractStep {

	/**
	 *	XML decode to CalculatorMeta
	 * @param stepMetaInterface
	 * @param cell
	 * @param databases
	 * @param metaStore
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		CalculatorMeta calculatorMeta = (CalculatorMeta) stepMetaInterface;

		/** The calculations to be performed */
		String fields = cell.getAttribute("calculation");
		JSONArray jsonArray = JSONArray.fromObject(fields);

		calculatorMeta.allocate(jsonArray.size());
		for(int i=0; i<jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			CalculatorMetaFunction calculatorMetaFunction = new CalculatorMetaFunction();
			/**The fieldName to set.*/
			calculatorMetaFunction.setFieldName(jsonObject.optString("field_name"));
			/**The calcType to set.*/
			calculatorMetaFunction.setCalcType(CalculatorMetaFunction.getCalcFunctionType(jsonObject.optString("calc_type")));
			/**The fieldA to set.*/
			calculatorMetaFunction.setFieldA(jsonObject.optString("field_a"));
			/**The fieldB to set.*/
			calculatorMetaFunction.setFieldB(jsonObject.optString("field_b"));
			/**The fieldC to set.*/
			calculatorMetaFunction.setFieldC(jsonObject.optString("field_c"));
			/**The valueType to set.*/
			calculatorMetaFunction.setValueType(ValueMetaFactory.getIdForValueMeta(jsonObject.optString("value_type")));
			/**The valueLength to set.*/
			calculatorMetaFunction.setValueLength(Const.toInt(jsonObject.optString("value_length"), -1));
			/**
			 *  The valuePrecision to set.
			 */
			calculatorMetaFunction.setValuePrecision(Const.toInt(jsonObject.optString("value_precision"), -1));
			/**
			 *  The removedFromResult to set.
			 */
			calculatorMetaFunction.setRemovedFromResult("Y".equalsIgnoreCase(jsonObject.optString("remove")));
			/**
			 * the conversionMask to set
			 */
			calculatorMetaFunction.setConversionMask(jsonObject.optString("conversion_mask"));
			/**
			 * the decimalSymbol to set
			 */
			calculatorMetaFunction.setDecimalSymbol(jsonObject.optString("decimal_symbol"));
			/**
			 * the groupingSymbol to set
			 */
			calculatorMetaFunction.setGroupingSymbol(jsonObject.optString("grouping_symbol"));
			/**
			 * the currencySymbol to set
			 */
			calculatorMetaFunction.setCurrencySymbol(jsonObject.optString("currency_symbol"));

			calculatorMeta.getCalculation()[i] = calculatorMetaFunction;
		}
	}

	/**
	 * CalculatorMeta encode to Document
	 * @param stepMetaInterface
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		CalculatorMeta calculatorMeta = (CalculatorMeta) stepMetaInterface;

		JSONArray jsonArray = new JSONArray();
		CalculatorMetaFunction[] calculation = calculatorMeta.getCalculation();
		for (CalculatorMetaFunction calculatorMetaFunction : calculation) {
			JSONObject jsonObject = new JSONObject();

			/**
			 * Returns the fieldName.
			 */
			jsonObject.put("field_name", calculatorMetaFunction.getFieldName());
			/**
			 * The calcType to set.
			 */
			jsonObject.put("calc_type", CalculatorMetaFunction.getCalcFunctionDesc(calculatorMetaFunction.getCalcType()));

			/**
			 * Returns the fieldA.
			 */
			jsonObject.put("field_a", calculatorMetaFunction.getFieldA());

			/**
			 * Returns the fieldB.
			 */
			jsonObject.put("field_b", calculatorMetaFunction.getFieldB());

			/**
			 * Returns the fieldC.
			 */
			jsonObject.put("field_c", calculatorMetaFunction.getFieldC());

			/**
			 * Returns the valueType.
			 */
			jsonObject.put("value_type", ValueMetaFactory.getValueMetaName( calculatorMetaFunction.getValueType() ));
			/**
			 *  Returns the valueLength.
			 */
			jsonObject.put("value_length", calculatorMetaFunction.getValueLength());
			/**
			 * Returns the valuePrecision.
			 */
			jsonObject.put("value_precision", calculatorMetaFunction.getValuePrecision());
			/**
			 * Returns the removedFromResult.
			 */
			jsonObject.put("remove", calculatorMetaFunction.isRemovedFromResult() ? "Y" : "N");
			/**
			 * the conversionMask
			 */
			jsonObject.put("conversion_mask", calculatorMetaFunction.getConversionMask());
			/**
			 * the decimalSymbol
			 */
			jsonObject.put("decimal_symbol", calculatorMetaFunction.getDecimalSymbol());
			/**
			 * the groupingSymbol
			 */
			jsonObject.put("grouping_symbol", calculatorMetaFunction.getGroupingSymbol());
			/**
			 * the currencySymbol
			 */
			jsonObject.put("currency_symbol", calculatorMetaFunction.getCurrencySymbol());
			jsonArray.add(jsonObject);
		}
		e.setAttribute("calculation", jsonArray.toString());

		return e;
	}

}
