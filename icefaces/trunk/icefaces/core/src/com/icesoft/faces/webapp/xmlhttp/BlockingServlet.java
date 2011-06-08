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

package com.icesoft.faces.webapp.xmlhttp;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.icesoft.faces.webapp.http.core.SessionExpiredException;

/**
 * The BlockingServlet was the original entry point for push-related
 * Ajax requests.  It is now deprecated in favour of
 * {@link com.icesoft.faces.webapp.http.servlet.MainServlet}.
 * <p/>
 * <b>Note: </b>The API of this class is is not intended for general usage by developers
 * but is included in the JavaDocs to provide visibility as it is an external entry point
 * into the ICEfaces Core framework.
 */
public class BlockingServlet extends HttpServlet {
    private ServletContext context;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        context = servletConfig.getServletContext();
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        Servlet servlet = (PersistentFacesServlet) context.getAttribute(PersistentFacesServlet.class.getName());
        try {
            servlet.service(servletRequest, servletResponse);
        } catch (SessionExpiredException e)  {
            //Look at moving this into MainServlet instead
            ((HttpServletResponse) servletResponse)
                    .sendError(500, e.getMessage());
        }
    }
}
