package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;
import com.icesoft.faces.webapp.http.core.SessionExpiredResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

public class SessionVerifier implements PseudoServlet {
    private static final PseudoServlet SessionExpiredServlet = new BasicAdaptingServlet(new ResponseHandlerServer(SessionExpiredResponse.Handler));
    private PseudoServlet servlet;
    private Pattern pattern;

    public SessionVerifier(String blockSessionCreationPathExpression, PseudoServlet servlet) {
        this.servlet = servlet;
        this.pattern = Pattern.compile(blockSessionCreationPathExpression);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.isRequestedSessionIdValid() || !pattern.matcher(request.getRequestURI()).find()) {
            servlet.service(request, response);
        } else {
            SessionExpiredServlet.service(request, response);
        }
    }

    public void shutdown() {
        servlet.shutdown();
    }
}
