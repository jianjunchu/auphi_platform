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
import org.pentaho.di.trans.steps.blockingstep.BlockingStepMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * A step that blocks throughput until the input ends, then it will either output the last row or the complete input.
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
@Component("BlockingStep")
@Scope("prototype")
public class BlockingStep extends AbstractStep {

	/**
	 * XML decode to BlockingStepMeta
	 * @param stepMetaInterface
	 * @param cell
	 * @param databases
	 * @param metaStore
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore, CurrentUserResponse user) throws Exception {
		BlockingStepMeta bsm = (BlockingStepMeta) stepMetaInterface;
		/**
		 * Pass all rows, or only the last one. Only the last row was the original behaviour.
		 * @param passAllRows
		 *          set to true if all rows should be passed and false if only the last one should be passed
		 */
		bsm.setPassAllRows("Y".equalsIgnoreCase(cell.getAttribute("pass_all_rows")));
		/** Directory to store the temp files
		 * Set the directory to store the temp files in.
		 * */
		bsm.setDirectory(cell.getAttribute("directory"));
		/** Temp files prefix...
		 * @param prefix
		 *          The prefix to set.
		 */
		bsm.setPrefix(cell.getAttribute("prefix"));
		/** The cache size: number of rows to keep in memory
		 * @param cacheSize
		 *          The cacheSize to set.
		 */
		bsm.setCacheSize(Const.toInt(cell.getAttribute("cache_size"), BlockingStepMeta.CACHE_SIZE));

		/**
		 * Compress files: if set to true, temporary files are compressed, thus reducing I/O at the cost of slightly higher
		 * CPU usage
		 * @param compressFiles
		 *          Whether to compress temporary files created during sorting
		 */
		bsm.setCompress("Y".equalsIgnoreCase(cell.getAttribute("compress")));
	}

	/**
	 * BlockingStepMeta encode to Document
	 * @param stepMetaInterface
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Element encode(StepMetaInterface stepMetaInterface, CurrentUserResponse user) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);

		BlockingStepMeta bsm = (BlockingStepMeta) stepMetaInterface;
		/**
		 * @return true when all rows are passed and false when only the last one is passed.
		 */
		e.setAttribute("pass_all_rows", bsm.isPassAllRows() ? "Y" : "N");

		/**
		 * @return The directory to store the temporary files in.
		 */
		e.setAttribute("directory", bsm.getDirectory());
		/**
		 * @return Returns the prefix.
		 */
		e.setAttribute("prefix", bsm.getPrefix());
		/**
		 * Returns the cacheSize.
		 */
		e.setAttribute("cache_size", bsm.getCacheSize() + "");
		/**
		 * Returns whether temporary files should be compressed
		 */
		e.setAttribute("compress", bsm.getCompress() ? "Y" : "N");

		return e;
	}

}
