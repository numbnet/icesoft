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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.servlet.DefaultFlowUrlHandler;
import org.springframework.webflow.context.servlet.FlowUrlHandler;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.FlowExecutionOutcome;
import org.springframework.webflow.executor.FlowExecutionResult;

public class ServletFlowExecutionHandler implements FlowExecutionHandler {
	private static Log log = LogFactory.getLog(ServletFlowExecutionHandler.class);
	private static final String SERVLET_RELATIVE_LOCATION_PREFIX = "servletRelative:";
	private static final String CONTEXT_RELATIVE_LOCATION_PREFIX = "contextRelative:";
	private static final String SERVER_RELATIVE_LOCATION_PREFIX = "serverRelative:";
	private ExternalContext externalContext;
	private FlowExecutionResult result;
	private ServletExternalContext servletExternalContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FacesContext facesContext;
	private FlowUrlHandler flowUrlHandler;

	public ServletFlowExecutionHandler(ExternalContext externalContext, FlowExecutionResult result, ServletExternalContext servletExternalContext, HttpServletRequest request, HttpServletResponse response, FacesContext facesContext) {
		this.externalContext = externalContext;
		this.result = result;
		this.servletExternalContext = servletExternalContext;
		this.request = request;
		this.response = response;
		this.facesContext = facesContext;
		flowUrlHandler = new DefaultFlowUrlHandler();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seecom.icesoft.faces.webapp.http.core.FlowExecutionHandler#
	 * handleFlowExecutionResult()
	 */
	public void handleFlowExecutionResult() throws IOException {
		if (result.isPaused()) {
			if (servletExternalContext.getFlowExecutionRedirectRequested()) {
				sendFlowExecutionRedirect(result, servletExternalContext, request, response);
			} else if (servletExternalContext.getFlowDefinitionRedirectRequested()) {
				sendFlowDefinitionRedirect(result, servletExternalContext, request, response);
			} else if (servletExternalContext.getExternalRedirectRequested()) {
				sendExternalRedirect(servletExternalContext.getExternalRedirectUrl(), request, response);
			}
		} else if (result.isEnded()) {
			if (servletExternalContext.getFlowDefinitionRedirectRequested()) {
				sendFlowDefinitionRedirect(result, servletExternalContext, request, response);
			} else if (servletExternalContext.getExternalRedirectRequested()) {
				sendExternalRedirect(servletExternalContext.getExternalRedirectUrl(), request, response);
			} else {
				/*
				 * What is the function of the handler? String location =
				 * handler.handleExecutionOutcome(result.getOutcome(), request,
				 * response); if (location != null) {
				 * sendExternalRedirect(location, request, response); } else {
				 */
				defaultHandleExecutionOutcome(result.getFlowId(), result.getOutcome(), request, response);
			}
		} else {
			throw new IllegalStateException("Execution result should have been one of [paused] or [ended]");
		}
	}

	protected void defaultHandleExecutionOutcome(String flowId, FlowExecutionOutcome outcome, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!response.isCommitted()) {
			response.sendRedirect(flowUrlHandler.createFlowDefinitionUrl(flowId, outcome.getOutput(), request));
		}
	}

	protected void defaultHandleExecutionOutcome(String flowId, FlowExecutionOutcome outcome, HttpServletRequest request, HttpServletResponse response, FacesContext facesContext) throws IOException {
		if (!response.isCommitted()) {
			sendRedirect(flowUrlHandler.createFlowDefinitionUrl(flowId, outcome.getOutput(), request), request, response, facesContext);
		}
	}

	private void sendFlowExecutionRedirect(FlowExecutionResult result, ServletExternalContext servletExternalContext, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = flowUrlHandler.createFlowExecutionUrl(result.getFlowId(), result.getPausedKey(), request);
		if (log.isDebugEnabled()) {
			log.debug("Sending flow execution redirect to '" + url + "'");
		}
		/*
		 * SWF Ajax popup features if (context.isAjaxRequest()) {
		 * ajaxHandler.sendAjaxRedirect(url, request, response,
		 * context.getRedirectInPopup()); } else {
		 */
		sendRedirect(url, request, response, facesContext);
	}

	private void sendFlowDefinitionRedirect(FlowExecutionResult result, ServletExternalContext servletExternalContext, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String flowId = servletExternalContext.getFlowRedirectFlowId();
		MutableAttributeMap input = servletExternalContext.getFlowRedirectFlowInput();
		if (result.isPaused()) {
			input.put("refererExecution", result.getPausedKey());
		}
		String url = flowUrlHandler.createFlowDefinitionUrl(flowId, input, request);
		if (log.isDebugEnabled()) {
			log.debug("Sending flow definition redirect to '" + url + "'");
		}
		sendRedirect(url, request, response, facesContext);
	}

	private void sendExternalRedirect(String location, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Sending external redirect to '" + location + "'");
		}
		if (location.startsWith(SERVLET_RELATIVE_LOCATION_PREFIX)) {
			sendServletRelativeRedirect(location.substring(SERVLET_RELATIVE_LOCATION_PREFIX.length()), request, response);
		} else if (location.startsWith(CONTEXT_RELATIVE_LOCATION_PREFIX)) {
			StringBuffer url = new StringBuffer(request.getContextPath());
			String contextRelativeUrl = location.substring(CONTEXT_RELATIVE_LOCATION_PREFIX.length());
			if (!contextRelativeUrl.startsWith("/")) {
				url.append('/');
			}
			url.append(contextRelativeUrl);
			sendRedirect(url.toString(), request, response, facesContext);
		} else if (location.startsWith(SERVER_RELATIVE_LOCATION_PREFIX)) {
			String url = location.substring(SERVER_RELATIVE_LOCATION_PREFIX.length());
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
			sendRedirect(url, request, response, facesContext);
		} else if (location.startsWith("http://") || location.startsWith("https://")) {
			sendRedirect(location, request, response, facesContext);
		} else {
			sendServletRelativeRedirect(location, request, response);
		}
	}

	private void sendServletRelativeRedirect(String location, HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuffer url = new StringBuffer(request.getContextPath());
		url.append(request.getServletPath());
		if (!location.startsWith("/")) {
			url.append('/');
		}
		url.append(location);
		sendRedirect(url.toString(), request, response, facesContext);
	}

	private void sendRedirect(String url, HttpServletRequest request, HttpServletResponse response, FacesContext facesContext) throws IOException {
		/*
		 * SWF Ajax popup features if (ajaxHandler.isAjaxRequest(request,
		 * response)) { ajaxHandler.sendAjaxRedirect(url, request, response,
		 * false); } else {
		 */
		/*
		 * SWF legacy HTTP support if (redirectHttp10Compatible) { // Always
		 * send status code 302.
		 * response.sendRedirect(response.encodeRedirectURL(url)); } else {
		 */
		ExternalContext ec = null;
		if ((facesContext != null) && ((ec = facesContext.getExternalContext()) != null)) {
			ec.redirect(response.encodeRedirectURL(url));
		} else {
			// I'm not sure if there is a case for this legacy redirection code.
			// Correct HTTP status code is 303, in particular for POST requests.
			response.setStatus(303);
			response.setHeader("Location", response.encodeRedirectURL(url));
		}
	}
}