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
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.append.AppendMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Read all rows from a hop until the end, and then read the rows from another hop.
 *
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
@Component("Append")
@Scope("prototype")
public class Append extends AbstractStep {


	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		AppendMeta appendMeta = (AppendMeta) stepMetaInterface;

		List<StreamInterface> infoStreams = appendMeta.getStepIOMeta().getInfoStreams();
		StreamInterface headStream = infoStreams.get(0);
		StreamInterface tailStream = infoStreams.get(1);

		headStream.setSubject(cell.getAttribute("head_name"));
		tailStream.setSubject(cell.getAttribute("tail_name"));
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		AppendMeta appendMeta = (AppendMeta) stepMetaInterface;

		List<StreamInterface> infoStreams = appendMeta.getStepIOMeta().getInfoStreams();
		e.setAttribute("head_name", infoStreams.get(0).getStepname());
		e.setAttribute("tail_name", infoStreams.get(1).getStepname());

		return e;
	}

}
