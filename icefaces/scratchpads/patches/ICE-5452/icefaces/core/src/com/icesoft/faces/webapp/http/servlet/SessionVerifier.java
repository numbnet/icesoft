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

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.standard.EmptyResponse;
import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;
import com.icesoft.faces.webapp.http.core.SessionExpiredResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionVerifier implements PseudoServlet {
    private static final Log log = LogFactory.getLog(SessionVerifier.class);
    private static final PseudoServlet SessionExpiredServlet = new BasicAdaptingServlet(new ResponseHandlerServer(SessionExpiredResponse.Handler));
    private static final PseudoServlet EmptyResponseServlet = new BasicAdaptingServlet(new ResponseHandlerServer(EmptyResponse.Handler));
    private PseudoServlet servlet;
    private boolean xmlResponse;

    public SessionVerifier(PseudoServlet servlet, boolean xmlResponse) {
        this.servlet = servlet;
        this.xmlResponse = xmlResponse;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.isRequestedSessionIdValid()) {
            servlet.service(request, response);
        } else {
System.out.println("SessionVerifier Responding with SessionExpired due to invalid or old SessionID from client");
            if (xmlResponse) {
                SessionExpiredServlet.service(request, response);
            } else {
                log.debug("Request for " + request.getRequestURI() + " belongs to an expired session. Dropping connection...");
                EmptyResponseServlet.service(request, response);
            }
        }
    }

    public void shutdown() {
        servlet.shutdown();
    }
}
