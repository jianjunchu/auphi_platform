package com.aofei.kettle.core;

import com.aofei.kettle.core.row.ValueMetaAndDataCodec;
import com.aofei.kettle.utils.JSONArray;
import com.aofei.kettle.utils.JSONObject;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaAndData;

/**
 * This class describes a condition in a general meaning.
 *
 * A condition can either be
 * <p>
 * <p>
 * 1) Atomic (a=10, B='aa')
 * <p>
 * 2) Composite ( NOT Condition1 AND Condition2 OR Condition3 )
 * <p>
 * <p>
 * If the nr of atomic conditions is 0, the condition is atomic, otherwise it's Composit.
 * <p>
 * Precedence doesn't exist. Conditions are evaluated in the order in which they are found.
 * <p>
 * A condition can be negated or not.
 * <p>
 * <p>
 * Condition转换类
 *
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class ConditionCodec {

	/**
	 * Condition对象 encode to JOSN
	 * @param condition
	 * @return
	 * @throws KettleValueException
	 */
	public static JSONObject encode(Condition condition) throws KettleValueException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("negated", condition.isNegated());
		jsonObject.put("operator", condition.getOperator());

	    if ( condition.isAtomic() ) {
	    	jsonObject.put("left_valuename", condition.getLeftValuename());
	    	jsonObject.put("func", condition.getFunction());
	    	jsonObject.put("right_valuename", condition.getRightValuename());

	      if ( condition.getRightExact() != null ) {
	    	  ValueMetaAndData rightExact = condition.getRightExact();
	    	  jsonObject.put("right_exact", ValueMetaAndDataCodec.encode(rightExact));
	      }
	    } else {
	    	JSONArray conditions = new JSONArray();
			for (int i = 0; i < condition.nrConditions(); i++) {
				Condition child = condition.getCondition(i);
				conditions.add(encode(child));
			}

			jsonObject.put("conditions", conditions);
	    }

	    return jsonObject;
	}

	public static Condition decode(JSONObject jsonObject) throws KettleValueException {
		Condition condition = new Condition();
		condition.setNegated(jsonObject.optBoolean("negated"));
		condition.setOperator(jsonObject.optInt("operator"));

		JSONArray conditions = jsonObject.optJSONArray("conditions");
		if (conditions == null || conditions.size() == 0) {
			condition.setLeftValuename(jsonObject.optString("left_valuename"));
			condition.setFunction(jsonObject.optInt("func"));
			condition.setRightValuename(jsonObject.optString("right_valuename"));
			JSONObject right_exact = jsonObject.optJSONObject("right_exact");
			if (right_exact != null) {
				ValueMetaAndData exact = ValueMetaAndDataCodec.decode(right_exact);
				condition.setRightExact(exact);
			}
		} else {
			for (int i = 0; i < conditions.size(); i++) {
				JSONObject child = conditions.getJSONObject(i);
				condition.addCondition(decode(child));
			}
		}

		return condition;
	}

}
