/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */
package org.icefaces.application.showcase.view.bean.examples.component.progressBar;

import com.icesoft.faces.async.render.SessionRenderer;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import javax.faces.event.ActionEvent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import java.io.Serializable;

/**
 * <p>The OutputProgressController is responsible for handling all user actions
 * for the OutputProgress demo.  This includes the the starting a long
 * running process to show how the progress bar can be used to monitor a
 * process on the server. </p>
 *
 * @see com.icesoft.faces.async.render.RenderManager
 * @since 1.7
 */
@ManagedBean(name = "outputProgressController")
@ViewScoped
public class OutputProgressController implements Serializable{

    public static Logger log = Logger.getLogger("OutputProgressController");

    // long running thread will sleep 10 times for this duration.
    public static final long PROCCESS_SLEEP_LENGTH = 300;
  
    // A thread pool is used to make this demo a little more scalable then
    // just creating a new thread for each user.
    protected static ThreadPoolExecutor longRunningTaskThreadPool =
            new ThreadPoolExecutor(5, 15, 30, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(20), new ThreadFactory() {
                        int i = 0;
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            t.setName("progressControllerWorker-" + (i++));
                            return t;
                        }
                    });
    
    // Model where we store the dynamic properties associated with outputProgress
    private OutputProgressModel outputProgressModel;

    /**
     * Default constructor where a reference to the PersistentFacesState is made
     * as well as the creation of the OutputProgressModel.  A reference to
     * PersistentFacesState is needed when implementing the Renderable
     * interface.
     */
    public OutputProgressController() {
        outputProgressModel = new OutputProgressModel();
        SessionRenderer.addCurrentSession("progressExample");
    }

    /**
     * A long running task is started when this method is called.  Actually not
     * that long around 10 seconds.   This long process {@link LongOperationRunner}
     * is responsible for updating the percent complete in the model class.
     *
     * @param event
     */
    public void startLongProcress(ActionEvent event) {
    	longRunningTaskThreadPool.execute(new LongOperationRunner(outputProgressModel));
    }
 
    /**
     * Gets the outputProgressModel for this instance.
     *
     * @return OutputProgressModel which contains the state of various
     *         dynamic properties that are manipulated by this example.
     */
    public OutputProgressModel getOutputProgressModel() {
        return outputProgressModel;
    }

    /**
     * Utility class to represent some server process that we want to monitor
     * using ouputProgress and server push.
     */
    protected class LongOperationRunner implements Runnable {
        private OutputProgressModel ouputProgressModel;

         public LongOperationRunner(OutputProgressModel ouputProgressModel) {
            this.ouputProgressModel = ouputProgressModel;
        }

        /**
         * Routine that takes time and updates percentage as it runs.
         */
        public void run() {
             ouputProgressModel.setPogressStarted(true);
            try {
                for (int i = 0; i <= 100; i += 10) {
                    // pause the thread
                    Thread.sleep(PROCCESS_SLEEP_LENGTH);
                    // update the percent value
                    ouputProgressModel.setPercentComplete(i);
                        SessionRenderer.render("progressExample");
                }
            }
            catch (InterruptedException e) { }
            ouputProgressModel.setPogressStarted(false);
        }

    }

    @PreDestroy
    public void dispose()  {
        longRunningTaskThreadPool.shutdown();
    }
   
}
