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

package com.icesoft.faces.env;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.executor.FlowExecutor;

public class SpringWebFlowInstantiationServlet extends HttpServlet {
    protected static Log log = LogFactory.getLog(SpringWebFlowInstantiationServlet.class);
    private static String CONFIG_PARAM_NAME = "contextConfigLocation";
    private static FlowExecutor flowExecutor = null;
    private ConfigurableWebApplicationContext container;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            initFlowExecutor(servletConfig);
        } catch (Throwable t)  {
            if (log.isErrorEnabled()) {
                log.error("Unable to initialize SpringWebFlowInstantiationServlet ", t );
            }
            throw new ServletException(
                "Unable to initialize SpringWebFlowInstantiationServlet ", t );
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //this servlet is for instantiation SpringWebFlow but does not handle 
        //requests
    }

    public void destroy() {
        container.close();
    }

    public static FlowExecutor getFlowExecutor()  {
        return flowExecutor;
    }

    void initFlowExecutor(ServletConfig config)  {
        container = new XmlWebApplicationContext();
        container.setConfigLocations(getConfigLocations(config));
        container.setServletConfig(config);
        container.setServletContext(config.getServletContext());
        container.refresh();
        flowExecutor = lookupFlowExecutor(container);
    }

    private String[] getConfigLocations(ServletConfig config) {
        String configLocations = config.getInitParameter(CONFIG_PARAM_NAME);
        if (configLocations != null) {
            return StringUtils.tokenizeToStringArray(config.getInitParameter(CONFIG_PARAM_NAME),
                    ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
        } else {
            return new String[] { "/WEB-INF/config/web-application-config.xml" };
        }
    }

    private FlowExecutor lookupFlowExecutor(WebApplicationContext container) {
        String[] beanNames = container.getBeanNamesForType(FlowExecutor.class);
        if (beanNames.length == 0) {
            throw new IllegalStateException("No bean of type FlowExecutor defined in context");
        } else if (beanNames.length > 1) {
            throw new IllegalStateException("More than one bean of type FlowExecutor defined in context.");
        } else {
            return (FlowExecutor) container.getBean(beanNames[0]);
        }
    }


}
