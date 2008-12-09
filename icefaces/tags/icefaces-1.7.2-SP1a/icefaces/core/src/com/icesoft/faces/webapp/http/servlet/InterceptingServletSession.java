package com.icesoft.faces.webapp.http.servlet;

import javax.servlet.http.HttpSession;

public class InterceptingServletSession extends ProxyHttpSession {
    private final SessionDispatcher.Monitor sessionMonitor;

    public InterceptingServletSession(HttpSession session, SessionDispatcher.Monitor sessionMonitor) {
        super(session);
        this.sessionMonitor = sessionMonitor;
    }

    public void invalidate() {
        //invalidate session right away!
        //see ICE-2731 -- delaying session invalidation doesn't work since JBoss+Catalina resuses session objects and
        //IDs which causes a lot of confusion in applications that have logout processes (invalidate session and
        //immediately initiate new session)
        sessionMonitor.shutdown();
    }
}
