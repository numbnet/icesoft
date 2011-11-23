/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.compat.progress;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;

@ManagedBean(name= LongTaskPool.BEAN_NAME)
@ApplicationScoped
public class LongTaskPool implements Serializable {
    public static final String BEAN_NAME = "longTaskPool";
    
    private ExecutorService threadPool;
    
    public ExecutorService getThreadPool() { return threadPool; }
    
    public void setThreadPool(ExecutorService threadPool) { this.threadPool = threadPool; }
    
	@PostConstruct
	private void init() {
        // Prep the thread pool
	    threadPool = Executors.newCachedThreadPool();
	}
	
	@PreDestroy
	private void deinit() {
	    // Cleanup the thread pool
	    if ((threadPool != null) &&
	        (!threadPool.isShutdown())) {
	        
	        threadPool.shutdown();
	        threadPool = null;
        }
	}
}
