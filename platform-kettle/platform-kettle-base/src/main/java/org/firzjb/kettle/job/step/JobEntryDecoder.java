package org.firzjb.kettle.job.step;

import java.util.List;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.metastore.api.IMetaStore;

import org.firzjb.base.model.response.CurrentUserResponse;
import com.mxgraph.model.mxCell;

public interface JobEntryDecoder {

	public JobEntryCopy decodeStep(mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception;

}
