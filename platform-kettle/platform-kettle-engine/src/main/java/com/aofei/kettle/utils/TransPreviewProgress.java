package com.aofei.kettle.utils;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.debug.BreakPointListener;
import org.pentaho.di.trans.debug.StepDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMeta;
import org.pentaho.di.trans.step.StepMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Takes care of displaying a dialog that will handle the wait while previewing a transformation...
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class TransPreviewProgress {

	  private TransMeta transMeta;
	  private String[] previewStepNames;
	  private int[] previewSize;
	  private Trans trans;

	  private String loggingText;
	  private TransDebugMeta transDebugMeta;

	/**
	 * Creates a new dialog that will handle the wait while previewing a transformation...
	 * @param transMeta
	 * @param previewStepNames
	 * @param previewSize
	 * @throws Exception
	 */
	  public TransPreviewProgress( TransMeta transMeta, String[] previewStepNames, int[] previewSize ) throws Exception {
	    this.transMeta = transMeta;
	    this.previewStepNames = previewStepNames;
	    this.previewSize = previewSize;
	    try {
	    	doPreview();
	    } catch(Exception e) {

	    } finally {
	    	// Capture preview activity to a String:
		    loggingText = KettleLogStore.getAppender().getBuffer( trans.getLogChannel().getLogChannelId(), true ).toString();
	    }
	  }

	  private void doPreview() throws KettleException {
	    // This transformation is ready to run in preview!
	    trans = new Trans( transMeta );
	    trans.setPreview( true );

	    // Prepare the execution...
	    //
		trans.prepareExecution(null);

	    // Add the preview / debugging information...
	    //
	    transDebugMeta = new TransDebugMeta( transMeta );
		for (int i = 0; i < previewStepNames.length; i++) {
			StepMeta stepMeta = transMeta.findStep(previewStepNames[i]);
			StepDebugMeta stepDebugMeta = new StepDebugMeta(stepMeta);
			stepDebugMeta.setReadingFirstRows(true);
			stepDebugMeta.setRowCount(previewSize[i]);
			transDebugMeta.getStepDebugMetaMap().put(stepMeta, stepDebugMeta);
		}

	    // set the appropriate listeners on the transformation...
	    //
	    transDebugMeta.addRowListenersToTransformation( trans );

	    // Fire off the step threads... start running!
	    //
		trans.startThreads();

//	    int previousPct = 0;
	    final List<String> previewComplete = new ArrayList<String>();

	    while ( previewComplete.size() < previewStepNames.length  && !trans.isFinished() ) {
	      // We add a break-point that is called every time we have a step with a full preview row buffer
	      // That makes it easy and fast to see if we have all the rows we need
	      //
			transDebugMeta.addBreakPointListers(new BreakPointListener() {
				public void breakPointHit(TransDebugMeta transDebugMeta, StepDebugMeta stepDebugMeta, RowMetaInterface rowBufferMeta, List<Object[]> rowBuffer) {
					String stepName = stepDebugMeta.getStepMeta().getName();
					previewComplete.add(stepName);
				}
			});


	      // Change the percentage...
	      try {
	        Thread.sleep( 500 );
	      } catch ( InterruptedException e ) {
	        // Ignore errors
	      }

	    }

	    trans.stopAll();


	  }

	/**
	 * @param stepname
	 *          the name of the step to get the preview rows for
	 * @return A list of rows as the result of the preview run.
	 */
	public List<Object[]> getPreviewRows(String stepname) {
		if (transDebugMeta == null) {
			return null;
		}

		for (StepMeta stepMeta : transDebugMeta.getStepDebugMetaMap().keySet()) {
			if (stepMeta.getName().equals(stepname)) {
				StepDebugMeta stepDebugMeta = transDebugMeta.getStepDebugMetaMap().get(stepMeta);
				return stepDebugMeta.getRowBuffer();
			}
		}
		return null;
	}

	/**
	 * @param stepname
	 *          the name of the step to get the preview rows for
	 * @return A description of the row (metadata)
	 */
	public RowMetaInterface getPreviewRowsMeta(String stepname) {
		if (transDebugMeta == null) {
			return null;
		}

		for (StepMeta stepMeta : transDebugMeta.getStepDebugMetaMap().keySet()) {
			if (stepMeta.getName().equals(stepname)) {
				StepDebugMeta stepDebugMeta = transDebugMeta.getStepDebugMetaMap().get(stepMeta);
				return stepDebugMeta.getRowBufferMeta();
			}
		}
		return null;
	}

	/**
	 * @return The logging text from the latest preview run
	 */
	public String getLoggingText() {
		return loggingText;
	}

	/**
	 *
	 * @return The transformation object that executed the preview TransMeta
	 */
	public Trans getTrans() {
		return trans;
	}

	/**
	 * @return the transDebugMeta
	 */
	public TransDebugMeta getTransDebugMeta() {
		return transDebugMeta;
	}

}
