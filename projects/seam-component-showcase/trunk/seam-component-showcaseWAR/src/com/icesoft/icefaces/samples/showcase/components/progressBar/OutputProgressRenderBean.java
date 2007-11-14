/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.icefaces.samples.showcase.components.progressBar;

import javax.faces.event.ActionEvent;

import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.async.render.IntervalRenderer;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;


import java.io.Serializable;

/**
 * <p>The OutputProgressRenderBean backs the determinate mode of the
 * outputProgress component.</p>
 *
 * @see OutputProgressPropertyBean
 * @since 1.0
 */
@Scope(ScopeType.PAGE)
@Name("progress")
public class OutputProgressRenderBean implements Renderable,  Serializable {
	   private static Log log =
           LogFactory.getLog(OutputProgressRenderBean.class);	
    /**
     * Renderable Interface
     */
//	public static final String RENDERER_NAME = "demand";
    private PersistentFacesState state;
    private boolean doneSetup;
    private int myId; 
    private static int id;
    private IntervalRenderer ir;
//    private OnDemandRenderer renderer;

   
    private RenderManager renderManager;

    /**
     * Get the PersistentFacesState.
     *
     * @return state the PersistantFacesState
     */
    public PersistentFacesState getState() {
        return state;
    }

    /**
     * Handles rendering exceptions for the progress bar.
     *
     * @param renderingException the exception that occured
     */
    public void renderingException(RenderingException renderingException) {
        renderingException.printStackTrace();
        cleanup();
    }

    /**
     * Sets the Render Manager.
     *
     * @param renderManager
     */
    @In
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
        log.info("setRenderManager() and renderManager="+this.renderManager);
//        if (renderManager !=null){
//          renderer = renderManager.getDelayRenderer(RENDERER_NAME);
//          renderer.add(this);
//        }else 
//        	log.info("\t\t renderManager is null");
    }

    /**
     * Gets RenderManager, just try to satisfy WAS
     *
     * @return RenderManager null
     */
    public RenderManager getRenderManager() {
    	log.info("getRenderManager rm="+this.renderManager);
        return this.renderManager;
    }

    // progress active status
    private boolean runningTask=true;

	    // value bound to component as an indicator of progress
	private int percent = 0;

    
    	    // flag to disable start button when progress bar is started
	private boolean disableStartButton = false;
	
	/**
	* Default construction for the backing bean.
	*/
	 public OutputProgressRenderBean() {
	        state = PersistentFacesState.getInstance();
	        myId = ++id;  //can keep track of the renderable thread id for debug
	    }
     /* 
     * @return the current percentage
     * Since this is what is actually changes to push the render, get
     * a fresh state each time
     */
    public int getPercent() {
    	//need to get state again in case seam has redirected to same view
    	PersistentFacesState _state=PersistentFacesState.getInstance();
    	if (_state!=null)state=_state;
	   	if (percent==100){
	   		if (!ir.isEmpty()){
	   			ir.requestRender();
	   			ir.requestStop();
	   			this.disableStartButton = false;
	   		}
	   		ir.requestRender();
	   		ir.requestStop();
	   	}
        return percent;
    }

	private void setupIntervalRenderer() {
		if (log.isDebugEnabled()){
    			log.debug(">>> new OutputProgressRenderBean renderable..."+myId);
    	}
    	ir = renderManager.getIntervalRenderer("org.icesoft.clock.clockRenderer");
    	ir.setInterval(1000);
    	ir.add(this);
    	ir.requestRender();
	}
	

    /**
     * Sets the percentage.
     *
     * @param percent the new percentage
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Determine whether the progress bar is active.
     *
     * @return the activity status
     */
    public boolean isRunningTask() {
        return runningTask;
    }

    /**
     * Set whether the progress bar is active.
     *
     * @param runningTask the new activity status
     */
    public void setRunningTask(boolean runningTask) {
        this.runningTask = runningTask;
    }
	    /**
	     * Gets the disabled state for the start button.
	     *
	     * @return true if the button should be disabled; false otherwise.
	     */
	    public boolean isDisableStartButton() {
	        return disableStartButton;
	    }
	    public void setDisableStartButton(boolean dsb){
	    	this.disableStartButton = dsb;
	    }

	    /**
	     * Start a new thread to do some work which is monitored for progress.
	     */
	    public void start(ActionEvent event) {
	    	log.info("start the action");
	    	setPercent(0);
	    	if (!doneSetup){
	    		log.info("setup the IR");
	    	    setupIntervalRenderer();
	    	}else {
	    		if (ir!=null)
	    		     ir.requestRender();
	    		else log.info("why is ir null????");
	    	}
	    	doneSetup = true;	    	
	        Thread testThread = new Thread(new LongOperationRunner(this));
	        testThread.start();	  
	    }

	    /**
	     * Helper class that simulates a long running task.  The progress bar
	     * updates based on this task via requestRender() calls.
	     */
	    static class LongOperationRunner implements Runnable {

	        OutputProgressRenderBean outputBean;

	        public LongOperationRunner(OutputProgressRenderBean outputBean) {
	            this.outputBean = outputBean;
	            log.info(" thread constructor & outputBean="+outputBean);
	            
	        }

	        public void run() {
	     
	            outputBean.setDisableStartButton(true);
	            try {
	                for (int i = 0; i <= 100; i += 10) {
	                    // pause the thread
	                	log.info(" \t in loop & i="+i+" for outputBean="+outputBean);
	                    // update the percent value
	                    if (i>0)outputBean.setPercent(i);
	                    Thread.sleep(300);
	                }
	                // now that thread work is complete enable "start" button
	                outputBean.setRunningTask(false);
	                outputBean.setDisableStartButton(false);
 	            }
	            catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    

	    /**
	     * cleanup of interval renderer
	     * 
	     */
	    private void cleanup() {
	    	log.info("cleanup()");
	        if (renderManager != null) {
	            ir.remove(this);
//	            renderer.remove(this);
	            if (ir.isEmpty() ) {
	                if(log.isDebugEnabled() ) { 
	                   log.debug("*** IntervalRenderer Stopped " );
	                } 
	                ir.requestStop();
	            }
	        }
	        else if (!ir.isEmpty()){
	        	ir.remove(this);
	        	ir.requestStop();
	        }
	        log.info("interval renderer stopped");
	    } 	    
	    
		@Destroy 
		public void destroy(){
			cleanup();
		} 
	}
