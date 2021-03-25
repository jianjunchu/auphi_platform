package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.abort.AbortMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;


/**
 *
 * Step that will abort after having seen 'x' number of rows on its input.
 *
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Component("Abort")
@Scope("prototype")
public class Abort extends AbstractStep {

	/**
	 * XML decode to AbortMeta
	 * @param stepMetaInterface
	 * @param cell
	 * @param databases
	 * @param metaStore
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		AbortMeta abortMeta = (AbortMeta) stepMetaInterface;
		abortMeta.setRowThreshold(cell.getAttribute("row_threshold"));
		abortMeta.setMessage(cell.getAttribute("message"));
		abortMeta.setAlwaysLogRows("Y".equals(cell.getAttribute("always_log_rows")));
	}

	/**
	 * AbortMeta encode XML
	 * @param stepMetaInterface
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		AbortMeta abortMeta = (AbortMeta) stepMetaInterface;

		e.setAttribute("row_threshold", abortMeta.getRowThreshold());
		e.setAttribute("message", abortMeta.getMessage());
		e.setAttribute("always_log_rows", abortMeta.isAlwaysLogRows() ? "Y" : "N");

		return e;
	}

}
