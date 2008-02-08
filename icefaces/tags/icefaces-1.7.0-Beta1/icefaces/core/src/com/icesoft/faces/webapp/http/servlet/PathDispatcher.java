package com.icesoft.faces.webapp.http.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class PathDispatcher implements PseudoServlet {

    private static Log log = LogFactory.getLog(PathDispatcher.class);

    private List matchers = new ArrayList();

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getRequestURI();

        if( log.isDebugEnabled() ){
            log.debug( "path: " + path );
        }

        Iterator i = matchers.iterator();
        boolean matched = false;
        while (!matched && i.hasNext()) {
            matched = ((Matcher) i.next()).serviceOnMatch(path, request, response);
        }

        if (!matched) {
            response.sendError(404, "Resource not found.");
        }
    }

    public void dispatchOn(String pathExpression, PseudoServlet toServer) {
        matchers.add(new Matcher(pathExpression, toServer));
    }

    private class Matcher {
        private Pattern pattern;
        private PseudoServlet server;

        public Matcher(String expression, PseudoServlet server) {
            this.pattern = Pattern.compile(expression);
            this.server = server;
        }

        boolean serviceOnMatch(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
            if (pattern.matcher(path).find()) {
                server.service(request, response);
                return true;
            } else {
                return false;
            }
        }

        void shutdown() {
            server.shutdown();
        }
    }

    public void shutdown() {
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            Matcher matcher = (Matcher) i.next();
            matcher.shutdown();
        }
    }
}
