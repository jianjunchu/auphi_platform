package org.firzjb.kettle.trans.steps;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.kettle.trans.step.AbstractStep;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.openapiclient.OpenAPIClientMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("OpenAPIClient")
@Scope("prototype")
public class OpenAPIClient extends AbstractStep {

    @Override
    public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
        OpenAPIClientMeta openAPIClientMeta = (OpenAPIClientMeta) stepMetaInterface;
        openAPIClientMeta.setAPIServer( cell.getAttribute("wAPIServer") );
        openAPIClientMeta.setMethodName( cell.getAttribute("wMethodName") );
        openAPIClientMeta.setAccessKey( cell.getAttribute("wAccessKey") );
        openAPIClientMeta.setSecret( cell.getAttribute("wSecret") );
        openAPIClientMeta.setuid( cell.getAttribute("wuid") );
        openAPIClientMeta.setbindId( cell.getAttribute("wBindId") );
        openAPIClientMeta.setBoName(cell.getAttribute("wBoName"));
        openAPIClientMeta.setBatchNumber(cell.getAttribute("wBatchNumber"));
    }

    @Override
    public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
        Document doc = mxUtils.createDocument();
        Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
        OpenAPIClientMeta openAPIClientMeta = (OpenAPIClientMeta) stepMetaInterface;
        e.setAttribute("wAPIServer", openAPIClientMeta.getAPIServer());
        e.setAttribute("wMethodName", openAPIClientMeta.getMethodName());
        e.setAttribute("wAccessKey", openAPIClientMeta.getAccessKey());
        e.setAttribute("wSecret", openAPIClientMeta.getSecret());
        e.setAttribute("wuid", openAPIClientMeta.getuid());
        e.setAttribute("wBindId", openAPIClientMeta.getbindId());
        e.setAttribute("wBoName", openAPIClientMeta.getBoName());
        e.setAttribute("wBatchNumber", openAPIClientMeta.getBatchNumber());
        return e;
    }

}
