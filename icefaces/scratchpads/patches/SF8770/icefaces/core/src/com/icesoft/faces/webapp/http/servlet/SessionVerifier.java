package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;
import com.icesoft.faces.webapp.http.core.SessionExpiredResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionVerifier implements PseudoServlet {
    private final static Log log = LogFactory.getLog(SessionVerifier.class);
    private static final PseudoServlet SessionExpiredServlet = new BasicAdaptingServlet(new ResponseHandlerServer(SessionExpiredResponse.Handler));
    private PseudoServlet servlet;

    public SessionVerifier(PseudoServlet servlet) {
        this.servlet = servlet;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.isRequestedSessionIdValid()) {
            servlet.service(request, response);
        } else {
System.out.println("SessionVerifier responding with session-expired for invalid JSESSIONID " + request.getRequestedSessionId());
            SessionExpiredServlet.service(request, response);
        }
    }

    public void shutdown() {
        servlet.shutdown();
    }
}
