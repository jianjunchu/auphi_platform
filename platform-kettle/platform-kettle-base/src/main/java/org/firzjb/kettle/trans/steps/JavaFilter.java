package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.firzjb.kettle.utils.StringEscapeHelper;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.javafilter.JavaFilterMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("JavaFilter")
@Scope("prototype")
public class JavaFilter extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		JavaFilterMeta javaFilterMeta = (JavaFilterMeta) stepMetaInterface;

		List<StreamInterface> targetStreams = javaFilterMeta.getStepIOMeta().getTargetStreams();
		targetStreams.get( 0 ).setSubject( cell.getAttribute( "send_true_to" ) );
	    targetStreams.get( 1 ).setSubject( cell.getAttribute( "send_false_to" ) );
	    javaFilterMeta.setCondition(StringEscapeHelper.decode(cell.getAttribute("condition")));
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		JavaFilterMeta javaFilterMeta = (JavaFilterMeta) stepMetaInterface;

		List<StreamInterface> targetStreams = javaFilterMeta.getStepIOMeta().getTargetStreams();
		e.setAttribute("send_true_to", targetStreams.get(0).getStepname());
		e.setAttribute("send_false_to", targetStreams.get(1).getStepname());
		e.setAttribute("condition", StringEscapeHelper.encode(javaFilterMeta.getCondition()));

		return e;
	}

}
