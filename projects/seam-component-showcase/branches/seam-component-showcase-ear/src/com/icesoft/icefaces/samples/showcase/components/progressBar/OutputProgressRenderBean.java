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
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.component.outputprogress.OutputProgress;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;
import static org.jboss.seam.ScopeType.PAGE;

import java.io.Serializable;

/**
 * <p>The OutputProgressRenderBean backs the determinate mode of the
 * outputProgress component.</p>
 *
 * @see OutputProgressPropertyBean
 * @since 1.0
 */
@Scope(ScopeType.SESSION)
@Name("progress")
public class OutputProgressRenderBean implements Serializable {

    /**
     * Renderable Interface
     */
    private PersistentFacesState state;


    private boolean keepRunning;


    public OutputProgressRenderBean() {

    } 

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
    }


    // flag to disable start button when progress bar is started
    private boolean disableStartButton = false;

    // binding back to jsp page
    private OutputProgress progressBar;

    // value bound to component as an indicator of progress
    private int percent = 0;

       /**
     * Gets the disabled state for the start button.
     *
     * @return true if the button should be disabled; false otherwise.
     */
    public boolean isDisableStartButton() {
        return disableStartButton;
    }

    /**
     * Start a new thread to do some work which is monitored for progress.
     */
    public void start(ActionEvent event) {

        Thread testThread = new Thread(new LongOperationRunner(this));
        testThread.start();
    }

    /**
     * Get the current percent value.
     *
     * @return percent complete of progress bar
     */
    public int getPercent() {
        state = PersistentFacesState.getInstance();
        return percent;
    }

    /**
     * Sets the current percent value.
     *
     * @param percent percent value of progress bar state.
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Gets the progress bar binding.
     *
     * @return bound progress bar.
     */
    public OutputProgress getProgressBar() {
        return progressBar;
    }

    /**
     * Sets the progress bar binding.
     *
     * @param progressBar progress bar to bind to this bean.
     */
    public void setProgressBar(OutputProgress progressBar) {
        this.progressBar = progressBar;
    }


    /**
     * Helper class that simulates a long running task.  The progress bar
     * updates based on this task via requestRender() calls.
     */
    protected class LongOperationRunner implements Runnable {

        PersistentFacesState state = null;
        OutputProgressRenderBean outputBean;

        public LongOperationRunner(OutputProgressRenderBean outputBean) {
            disableStartButton = true;
            this.outputBean = outputBean;
        }

        public void run() {

            state = PersistentFacesState.getInstance();

            try {
                for (int i = 0; i <= 100; i += 10) {
                    // pause the thread
                    Thread.sleep(300);
                    // update the percent value
                    percent = i;
                    // call a render to update the component state
                    try {                        

                        state.execute();
                        state.render();
                        

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // now that thread work is complete enable "start" button
                disableStartButton = false;

                // one last render to update start button
                try {

                    state.execute();
                    state.render();

                    OutputProgressRenderBean.this.keepRunning = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determine whether the progress bar is active.
     *
     * @return the activity status
     */
    public boolean isRunningTask() {
        return disableStartButton;
    }

    /**
     * Set whether the progress bar is active.
     *
     * @param runningTask the new activity status
     */
    public void setRunningTask(boolean runningTask) {
        disableStartButton = runningTask;
    }

}
