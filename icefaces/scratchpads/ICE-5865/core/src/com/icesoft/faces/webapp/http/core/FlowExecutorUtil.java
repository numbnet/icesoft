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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 */
 
package com.icesoft.faces.webapp.http.core;

import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.executor.FlowExecutionResult;
import org.springframework.webflow.executor.FlowExecutor;

/**
 *
 * Helper class used when launching or resuming Spring Web Flows
 *
 */
public class FlowExecutorUtil {
	private FlowExecutor flowExecutor;
	private ExternalContext externalContext;


	public FlowExecutorUtil(FlowExecutor flowExecutor, ExternalContext externalContext){
		this.flowExecutor = flowExecutor;
		this.externalContext = externalContext;
	}

    public FlowExecutor getFlowExecutor() {
		return flowExecutor;
	}

	public void setFlowExecutor(FlowExecutor flowExecutor) {
		this.flowExecutor = flowExecutor;
	}

	public ExternalContext getExternalContext() {
		return externalContext;
	}

	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	protected MutableAttributeMap defaultFlowExecutionInputMap(HttpServletRequest request) {
        LocalAttributeMap inputMap = new LocalAttributeMap();
        Map parameterMap = request.getParameterMap();
        Iterator it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();
            if (values.length == 1) {
                inputMap.put(name, values[0]);
            } else {
                inputMap.put(name, values);
            }
        }
        return inputMap;
    }

    protected MutableAttributeMap defaultFlowExecutionInputMapPortlet(PortletRequest request) {
    	//Could probably be combined with the defaultFlowExecutionInputMap method who would then
    	//need to check what type of response was sent in. This would require the attribute
    	//to be of type Object though!
    	LocalAttributeMap inputMap = new LocalAttributeMap();
    	Map parameterMap = request.getParameterMap();
    	Iterator it = parameterMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry entry = (Map.Entry) it.next();
    		String name = (String) entry.getKey();
    		String[] values = (String[]) entry.getValue();
    		if (values.length == 1) {
    			inputMap.put(name, values[0]);
    		} else {
    			inputMap.put(name, values);
    		}
    	}
    	return inputMap;
    }


    protected FlowExecutionResult resumeExecution(String flowExecutionKey) {
		return flowExecutor.resumeExecution(flowExecutionKey, externalContext);
	}

	protected FlowExecutionResult launchExecution(MutableAttributeMap input, String flowId) {
        return flowExecutor.launchExecution(flowId, input, externalContext);
	}

}
