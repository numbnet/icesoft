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


import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.portlet.DefaultFlowUrlHandler;
import org.springframework.webflow.context.portlet.FlowUrlHandler;
import org.springframework.webflow.context.portlet.PortletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.FlowExecutionOutcome;
import org.springframework.webflow.executor.FlowExecutionResult;
import org.springframework.webflow.executor.FlowExecutor;

import com.icesoft.faces.env.SpringWebFlowInstantiationServlet;


public class PortletFlowExecutionHandler implements FlowExecutionHandler {
    private static Log log = LogFactory.getLog(PortletFlowExecutionHandler.class);
	private FlowExecutionResult result;
	private PortletExternalContext portletExternalContext;
	private PortletRequest request;
	private PortletResponse response;
	private FlowExecutorUtil flowExecutorUtil;
	private ExternalContext externalContext;
	private FlowUrlHandler flowUrlHandler;
	private FacesContext facesContext;

	public PortletFlowExecutionHandler(ExternalContext externalContext, FlowExecutionResult result, PortletExternalContext portletExternalContext, PortletRequest request, PortletResponse response, FacesContext facesContext) {
		this.externalContext = externalContext;
		this.result = result;
		this.portletExternalContext = portletExternalContext;
		this.request = request;
		this.response = response;
		this.facesContext = facesContext;
		flowUrlHandler = new DefaultFlowUrlHandler();
		this.flowExecutorUtil = new FlowExecutorUtil((FlowExecutor) SpringWebFlowInstantiationServlet.getFlowExecutor(), portletExternalContext);
	}

	public void handleFlowExecutionResult() throws IOException {
		if (result.isPaused()) {
			if (portletExternalContext.getFlowExecutionRedirectRequested()) {
				sendFlowExecutionRedirect(result, portletExternalContext, request, response);
			}
			if (portletExternalContext.getFlowDefinitionRedirectRequested()) {
				sendFlowDefinitionRedirect(result, portletExternalContext, request, response);
			} else if (portletExternalContext.getExternalRedirectRequested()) {
				sendExternalRedirect(portletExternalContext.getExternalRedirectUrl(), request, response);
			}
		} else if (result.isEnded()) {
			if (portletExternalContext.getFlowDefinitionRedirectRequested()) {
				sendFlowDefinitionRedirect(result, portletExternalContext, request, response);
			} else if (portletExternalContext.getExternalRedirectRequested()) {
				sendExternalRedirect(portletExternalContext.getExternalRedirectUrl(), request, response);
			} else {
				/*
				 * What is the function of the handler? String location =
				 * handler.handleExecutionOutcome(result.getOutcome(), request,
				 * response); if (location != null) {
				 * sendExternalRedirect(location, request, response); } else {
				 */
				defaultHandleExecutionOutcome(portletExternalContext, result.getFlowId(), result.getOutcome(), request, response);
			}
		} else {
			throw new IllegalStateException("Execution result should have been one of [paused] or [ended]");
		}
	}



	protected void defaultHandleExecutionOutcome(PortletExternalContext portletExternalContext, String flowId, FlowExecutionOutcome outcome, PortletRequest request, PortletResponse response) throws IOException {
		/*
		 * In a "normal" web world, what happens is the following:
		 * A new flow url is created and a redirect is sent. This redirect
		 * is then caught in the SwfLifecycleExecutor who goes looking for
		 * a flow execution key (the hidden field in your form), and since none
		 * is found, it launches a new flow.
		 *
		 * This is not possible in the portlet version though since ICEfaces
		 * has cut off the ActionRequest and ActionResponse. So we fake it,
		 * skip the redirect and go straight on to launching a new flow here
		 * and now.
		 */

		MutableAttributeMap input = flowExecutorUtil.defaultFlowExecutionInputMapPortlet(request);
		flowExecutorUtil.launchExecution(input, flowId);
	}

	private void sendFlowExecutionRedirect(FlowExecutionResult result, PortletExternalContext portletExternalContext, PortletRequest request, PortletResponse response) throws IOException {
		String url = flowUrlHandler.createFlowExecutionUrl(result.getFlowId(), flowUrlHandler.getFlowExecutionKey(request), (RenderResponse)request);
		if (log.isDebugEnabled()) {
			log.debug("Sending flow execution redirect to '" + url + "'");
		}
		/*
		 * SWF Ajax popup features if (context.isAjaxRequest()) {
		 * ajaxHandler.sendAjaxRedirect(url, request, response,
		 * context.getRedirectInPopup()); } else {
		 */
		sendRedirect(url, request, response);
	}

	private void sendFlowDefinitionRedirect(FlowExecutionResult result, PortletExternalContext portletExternalContext, PortletRequest request, PortletResponse response) throws IOException {
		/*
		 * Same story as with defaultHandleExecutionOutcome
		 * We get the new flow id from portletExternalContext
		 * and then we launch execution of this new flow
		 */

		String newFlowId = portletExternalContext.getFlowRedirectFlowId();
		MutableAttributeMap input = portletExternalContext.getFlowRedirectFlowInput();
		if (result.isPaused()) {
			input.put("refererExecution", result.getPausedKey());
		}
		flowExecutorUtil.launchExecution(input, newFlowId);
	}

	private void sendExternalRedirect(String location, PortletRequest request, PortletResponse response) throws IOException {
		externalContext.redirect(location);
	}

	private void sendRedirect(String url, PortletRequest request, PortletResponse response) throws IOException {
		//your clever code here please
	}

	public PortletExternalContext getPortletExternalContext() {
		return portletExternalContext;
	}


}